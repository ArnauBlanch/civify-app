package com.civify.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.civify.R;
import com.civify.utils.ConfirmDialog;
import com.civify.utils.NetworkController;
import com.civify.utils.Timeout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;

public class LocationAdapter implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final LatLng ZERO = new LatLng(0, 0);

    public static final int PERMISSION_ACCESS_LOCATION = 700;

    private static final String TAG = LocationAdapter.class.getSimpleName();

    private static final int REQUEST_CHECK_SETTINGS = 800;
    private static final int CONNECTION_FAILURE_RESOLUTION = 900;

    private static final int ACCURACY_THRESHOLD = 600;
    private static final int LOCATION_UPDATE_TIMEOUT_MULTIPLIER = 4;
    private static final int LOCATION_FIRST_UPDATE_TIMEOUT_MULTIPLIER = 5;

    private static final double EARTH_RADIUS = 6366198;

    private final Activity mContext;
    private final GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private UpdateLocationListener mUpdateLocationListener;
    private Timeout mOnUpdateTimeout;
    private Runnable mOnConnectedUpdateCallback, mOnPermissionsRequested;

    private boolean mHasPermissions, mAutoRefresh, mRequestingPermissions, mLowConnectionWarning;
    private long mIntervalRefresh, mFastIntervalRefresh;

    public LocationAdapter(@NonNull Activity context) {
        mContext = context;
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = null;
        setAutoRefresh(false);
        setRequestingPermissions(false);
        checkForPermissions();
    }

    public void setOnPermissionsRequestedListener(@Nullable Runnable onPermissions) {
        mOnPermissionsRequested = onPermissions;
        if (!isRequestingPermissions() && hasPermissions()) mOnPermissionsRequested.run();
    }

    public void connect() {
        if (!isConnected()) {
            Log.i(TAG, "Connecting location services...");
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        updateLocation();
        if (mOnConnectedUpdateCallback != null) mOnConnectedUpdateCallback.run();
        Log.i(TAG, "Location services connected.");
    }

    public boolean isConnected() {
        return mGoogleApiClient.isConnected();
    }

    public void disconnect() {
        if (mGoogleApiClient.isConnected()) {
            removeUpdates();
            mGoogleApiClient.disconnect();
        }
        if (isLocationUpdateTimeoutSet()) mOnUpdateTimeout.cancel();
        Log.i(TAG, "Location services disconnected.");
    }

    public Activity getContext() {
        return mContext;
    }

    private void removeUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        Log.d(TAG, "Removed location udpates.");
    }

    public boolean isAutoRefresh() {
        return mAutoRefresh;
    }

    public void setAutoRefresh(boolean autoRefresh) {
        mAutoRefresh = autoRefresh;
        boolean connected = mGoogleApiClient.isConnected();
        if (isAutoRefresh()) {
            if (mLocationRequest != null) {
                mLocationRequest.setInterval(mIntervalRefresh)
                        .setFastestInterval(mFastIntervalRefresh);
            }
            if (isLocationUpdateTimeoutSet()) {
                rescheduleLocationUpdateTimeout(
                        getLocationUpdateTimeoutTime(LOCATION_UPDATE_TIMEOUT_MULTIPLIER)
                                - mOnUpdateTimeout.getLapsedMillisFromSchedule());
            } else rescheduleLocationUpdateTimeout();
            if (connected) updateLocation();
        } else {
            if (connected) {
                removeUpdates();
                mLocationRequest = null;
            }
            if (isLocationUpdateTimeoutSet()) {
                mOnUpdateTimeout.cancel();
                mOnUpdateTimeout = null;
            }
        }
    }

    private LocationRequest getLocationRequest() {
        if (mLocationRequest == null) {
            setUpdateIntervals(Priority.LOW_POWER, Priority.LOW_POWER.getPeriodMillis(),
                    Priority.LOW_POWER.getFastestPeriodMillis());
        }
        return mLocationRequest;
    }

    public void setUpdateIntervals(@NonNull Priority priority,
                                   long updateMillis, long fastestUpdateMillis) {
        mIntervalRefresh = updateMillis;
        mFastIntervalRefresh = fastestUpdateMillis;
        setLocationPriority(priority);
        setAutoRefresh(isAutoRefresh());
        Log.d(TAG, "Location updates priority set to " + priority
                + " [" + mFastIntervalRefresh + ',' + mIntervalRefresh + ']');
    }

    private void setLocationPriority(@NonNull Priority priority) {
        int locationRequestPriority = priority.getLocationRequestPriority();
        if (mLocationRequest == null || mLocationRequest.getPriority() != locationRequestPriority) {
            mLocationRequest = LocationRequest.create().setPriority(locationRequestPriority);
        }
    }

    private boolean isDelayedUpdate() {
        return mOnConnectedUpdateCallback != null;
    }

    private void updateLocation() {
        Log.v(TAG, "Trying to update location...");
        if (isDelayedUpdate()) Log.v(TAG, "Detected delayed updates.");
        else {
            if (checkConnection()) {
                if (hasPermissions()) {
                    setRequestingPermissions(true);
                    LocationSettingsRequest.Builder builder =
                            new LocationSettingsRequest.Builder().addLocationRequest(
                                    getLocationRequest()).setAlwaysShow(true);

                    LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                            builder.build()).setResultCallback(getLocationSettingsResultCallback());
                    Log.v(TAG, "Settings verification requested.");
                } else {
                    checkForPermissions();
                }
            }
        }
    }

    @NonNull
    private ResultCallback<Result> getLocationSettingsResultCallback() {
        return new ResultCallback<Result>() {
            @Override
            public void onResult(@NonNull Result result) {
                Status status = result.getStatus();
                if (status.getStatusCode() == CommonStatusCodes.SUCCESS) {
                    Log.i(TAG, "All location settings are satisfied.");
                    Runnable requestUpdatesTask = new RequestUpdatesTask();
                    if (mGoogleApiClient.isConnected()) requestUpdatesTask.run();
                    else {
                        mOnConnectedUpdateCallback = requestUpdatesTask;
                        Log.d(TAG, "Google API is not connected. "
                                + "Updates delayed until Google API is connected again.");
                    }
                } else if (status.getStatusCode() == CommonStatusCodes.RESOLUTION_REQUIRED) {
                    Log.i(TAG, "Location settings are not satisfied. "
                            + "Showing the user a dialog to upgrade location settings.");
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in the onActivityResult() of mContext
                        status.startResolutionForResult(mContext, REQUEST_CHECK_SETTINGS);
                        setRequestingPermissions(true);
                    } catch (IntentSender.SendIntentException e) {
                        Log.wtf(TAG, "PendingIntent unable to execute request", e);
                    }
                } else if (status.getStatusCode()
                        == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                    if (checkNetwork()) {
                        Log.wtf(TAG, 
                                "Location settings can't be changed to meet the requirements");
                    }
                }
            }
        };
    }

    public void onMapSettingsResults(int requestCode, int resultCode) {
        if (requestCode == CONNECTION_FAILURE_RESOLUTION) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i(TAG, mContext.getString(
                        R.string.user_location_settings_agreed));
                // Reconnect
                connect();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.w(TAG, mContext.getString(R.string.location_connection_failed));
                rescheduleLocationUpdateTimeout();
            }
        } else if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i(TAG, mContext.getString(R.string.user_location_settings_agreed));
                updateLocation();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, mContext.getString(R.string.user_location_settings_canceled));
                updateLocation();
            }
        }
    }

    private boolean checkConnection() {
        if (!mGoogleApiClient.isConnected()) {
            Log.w(TAG, "GoogleApiClient is not connected!");
            connect();
            return false;
        }
        return true;
    }

    private void setRequestingPermissions(boolean requestingPermissions) {
        mRequestingPermissions = requestingPermissions;
    }

    public boolean isRequestingPermissions() {
        return mRequestingPermissions;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(mContext, CONNECTION_FAILURE_RESOLUTION);
            } catch (IntentSender.SendIntentException e) {
                Log.wtf(TAG, "Location services connection failed.", e);
            }
        } else {
            Log.wtf(TAG, "Location services connection failed with code "
                    + connectionResult.getErrorCode());
        }
    }

    private boolean hasPermissions() {
        return mHasPermissions;
    }

    public void checkForPermissions() {
        if (!isRequestingPermissions()) {
            mHasPermissions = false;
            setRequestingPermissions(true);
            // If we don't have permissions we request them on runtime
            if (ContextCompat.checkSelfPermission(mContext,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, TAG + " without permissions! Requesting them if available.");
                ActivityCompat.requestPermissions(mContext, new String[]
                        {android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_ACCESS_LOCATION);
            } else if (checkNetwork()) {
                Log.i(TAG, TAG + " with permissions.");
                mHasPermissions = true;
                setRequestingPermissions(false);
                if (mGoogleApiClient.isConnected()) updateLocation();
            }
        }
    }

    private boolean checkNetwork() {
        return NetworkController.checkNetwork(mContext,
            new Runnable() {
                @Override
                public void run() {
                    setRequestingPermissions(true);
                }
            },
            new Runnable() {
                @Override
                public void run() {
                    setRequestingPermissions(false);
                    rescheduleLocationUpdateTimeout();
                }
            });
    }

    public void setOnUpdateLocationListener(@NonNull UpdateLocationListener listener) {
        mUpdateLocationListener = listener;
        executeUpdateLocationListener();
    }

    private void executeUpdateLocationListener() {
        if (mLastLocation != null && mUpdateLocationListener != null) {
            mUpdateLocationListener.onUpdateLocation(mLastLocation);
        }
    }

    private void handleNewLocation(@NonNull Location location) {
        mLastLocation = location;
        Log.d(TAG, "Location updated: " + mLastLocation);
        executeUpdateLocationListener();
        if (location.getAccuracy() < ACCURACY_THRESHOLD) rescheduleLocationUpdateTimeout();
        if (!isAutoRefresh()) setAutoRefresh(false);
    }

    public Location getLastLocation() {
        return mLastLocation;
    }

    private boolean isLocationUpdateTimeoutSet() {
        return mOnUpdateTimeout != null;
    }

    private void setLocationUpdateTimeout() {
        if (!isLocationUpdateTimeoutSet()) {
            mLowConnectionWarning = false;
            mOnUpdateTimeout = Timeout.schedule("LocationUpdateTimeout", new Runnable() {
                @Override
                public void run() {
                    if (!mLowConnectionWarning) {
                        checkForPermissions();
                        if (hasPermissions()) {
                            mLowConnectionWarning = true;
                            ConfirmDialog.show(mContext, "Cannot retrieve location",
                                    "It appears the map is taking too long to retrieve "
                                            + "your location.\n\nYour network connection "
                                            + "or GPS may be slow or unavailable.",
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface d, int w) {
                                            checkForPermissions();
                                            // Uncomment to repeat message
                                            // mLowConnectionWarning = false;
                                        }
                                    }, null);
                        }
                    }
                }
            }, getLocationUpdateTimeoutTime(LOCATION_FIRST_UPDATE_TIMEOUT_MULTIPLIER));
        }
    }

    private void rescheduleLocationUpdateTimeout() {
        rescheduleLocationUpdateTimeout(
                getLocationUpdateTimeoutTime(LOCATION_UPDATE_TIMEOUT_MULTIPLIER));
    }

    private void rescheduleLocationUpdateTimeout(long millis) {
        if (isLocationUpdateTimeoutSet()) mOnUpdateTimeout.reschedule(millis);
        else setLocationUpdateTimeout();
    }

    private long getLocationUpdateTimeoutTime(int multiplier) {
        return mIntervalRefresh * multiplier;
    }

    @NonNull
    public static LatLng getLatLng(@NonNull Location location) {
        return getLatLng(location.getLatitude(), location.getLongitude());
    }

    @NonNull
    public static LatLng getLatLng(double latitude, double longitude) {
        return new LatLng(latitude, longitude);
    }

    @NonNull
    public static Location getLocation(@NonNull LatLng latitudeLongitude) {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latitudeLongitude.latitude);
        location.setLongitude(latitudeLongitude.longitude);
        return location;
    }

    public static LatLng move(LatLng start, double toNorth, double toEast) {
        double lonDiff = meterToLongitude(toEast, start.latitude);
        double latDiff = meterToLatitude(toNorth);
        return new LatLng(start.latitude + latDiff, start.longitude
                + lonDiff);
    }

    private static double meterToLongitude(double meterToEast, double latitude) {
        double latArc = Math.toRadians(latitude);
        double radius = Math.cos(latArc) * EARTH_RADIUS;
        double rad = meterToEast / radius;
        return Math.toDegrees(rad);
    }

    private static double meterToLatitude(double meterToNorth) {
        double rad = meterToNorth / EARTH_RADIUS;
        return Math.toDegrees(rad);
    }

    public enum Priority {
        HIGH_ACCURACY(LocationRequest.PRIORITY_HIGH_ACCURACY, 3000, 1000),
        LOW_POWER(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, 9000,
                HIGH_ACCURACY.getPeriodMillis());

        private final int mPriority;
        private final long mPeriodMillis, mFastestPeriodMillis;

        Priority(int priority, long periodMillis, long fastestPeriodMillis) {
            this.mPriority = priority;
            mPeriodMillis = periodMillis;
            mFastestPeriodMillis = fastestPeriodMillis;
        }

        private int getLocationRequestPriority() {
            return mPriority;
        }

        public long getPeriodMillis() {
            return mPeriodMillis;
        }

        public long getFastestPeriodMillis() {
            return mFastestPeriodMillis;
        }
    }

    public void getLocalityFromCurrentPosition(@NonNull LocalityCallback callback) {
        GeocoderAdapter.getLocality(mContext, mLastLocation, callback);
    }

    private class RequestUpdatesTask implements Runnable {
        @Override
        public void run() {
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, getLocationRequest(),
                        LocationAdapter.this);
                if (mOnPermissionsRequested != null) mOnPermissionsRequested.run();
            } catch (SecurityException e) {
                mHasPermissions = false;
                Log.e(TAG, "Permissions restricted due to SecurityException", e);
            }
            mOnConnectedUpdateCallback = null;
            Log.v(TAG, "Updates requested successfully.");
            setRequestingPermissions(false);
            rescheduleLocationUpdateTimeout();
        }
    }
}
