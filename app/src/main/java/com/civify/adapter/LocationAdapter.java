package com.civify.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.civify.R;
import com.civify.utils.ConfirmDialog;
import com.civify.utils.ListenerQueue;
import com.civify.utils.NetworkController;
import com.civify.utils.RootUtils;
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
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

public class LocationAdapter implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final LatLng ZERO = new LatLng(0, 0);

    public static final int PERMISSION_ACCESS_LOCATION = 700;

    public static final boolean DEFAULT_MOCKS_ENABLED = false;
    public static final boolean DEFAULT_AUTO_REFRESH = false;

    private static final String TAG = LocationAdapter.class.getSimpleName();

    private static final int REQUEST_CHECK_SETTINGS = 800;
    private static final int CONNECTION_FAILURE_RESOLUTION = 900;

    private static final int ACCURACY_THRESHOLD = 600;
    private static final int LOCATION_UPDATE_TIMEOUT_MULTIPLIER = 4;
    private static final int LOCATION_FIRST_UPDATE_TIMEOUT_MULTIPLIER = 5;

    private static final int KM = 1000;
    private static final double EARTH_RADIUS = 6366198;

    private final Activity mContext;
    private final GoogleApiClient mGoogleApiClient;
    private final ListenerQueue mOnPermissionsChangedListeners;
    private Runnable mOnConnectedUpdateCallback;
    private Location mLastLocation, mLastMockLocation, mMockLocation;
    private LocationRequest mLocationRequest;
    private UpdateLocationListener mUpdateLocationListener;
    private Timeout mOnUpdateTimeout;

    private boolean mConnecting, mHasPermissions, mAutoRefresh, mRequestingPermissions,
            mLowConnectionWarning, mMockLocationsEnabled, mMockWarning, mRootWarning;
    private long mIntervalRefresh, mFastIntervalRefresh;

    public LocationAdapter(@NonNull Activity context) {
        mContext = context;
        mOnPermissionsChangedListeners = new ListenerQueue();
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mLocationRequest = null;
        mMockLocationsEnabled = DEFAULT_MOCKS_ENABLED;
        setAutoRefresh(DEFAULT_AUTO_REFRESH);
        checkForPermissions();
    }

    public Activity getContext() {
        return mContext;
    }

    public void connect() {
        if (!isConnecting()) {
            if (!isConnected()) {
                Log.i(TAG, "Connecting location services...");
                mConnecting = true;
                mGoogleApiClient.connect();
            }
            repeatWarningsWhenNeeded();
            checkForPermissions();
        }
    }

    public boolean isConnecting() {
        return mConnecting;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mConnecting = false;
        updateLocation();
        if (mOnConnectedUpdateCallback != null) mOnConnectedUpdateCallback.run();
        Log.i(TAG, "Location services connected.");
    }

    public boolean isConnected() {
        return mGoogleApiClient.isConnected();
    }

    public void disconnect() {
        if (isConnected()) {
            removeUpdates();
            mGoogleApiClient.disconnect();
        }
        if (isLocationUpdateTimeoutSet()) mOnUpdateTimeout.cancel();
        Log.i(TAG, "Location services disconnected.");
    }

    private boolean checkConnection() {
        if (!isConnected()) {
            Log.w(TAG, "GoogleApiClient is not connected!");
            connect();
            return false;
        }
        return true;
    }

    private void removeUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        Log.i(TAG, "Removed location udpates.");
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

    public boolean setMockLocation(@Nullable Location mockLocation) {
        if (!isMockLocationsEnabled()) return false;
        mMockLocation = mockLocation;
        if (mockLocation == null) setAutoRefresh(DEFAULT_AUTO_REFRESH);
        else {
            setAutoRefresh(false);
            handleNewLocation(mockLocation);
        }
        return true;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (mMockLocation == null) handleNewLocation(location);
    }

    private void handleNewLocation(@NonNull Location location) {
        Log.d(TAG, "Location received: " + location);
        if (isLocationPlausible(location)) {
            Location previous = mLastLocation;
            mLastLocation = location;
            if (previous == null && hasPermissions()) mOnPermissionsChangedListeners.run();
            cancelLocationUpdateTimeout();
            executeUpdateLocationListener();
            rescheduleLocationUpdateTimeout();
        } else Log.d(TAG, "Location discarded.");
        if (!isAutoRefresh()) setAutoRefresh(false);
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

    public Location getLastLocation() {
        return mLastLocation == null ? getLocation(ZERO) : mLastLocation;
    }

    public void getLocalityFromCurrentLocation(@NonNull LocalityCallback callback) {
        GeocoderAdapter.getLocality(mContext, mLastLocation, callback);
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

    public boolean isAutoRefresh() {
        return mAutoRefresh;
    }

    public void setAutoRefresh(boolean autoRefresh) {
        mAutoRefresh = autoRefresh;
        boolean connected = isConnected();
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
            cancelLocationUpdateTimeout();
        }
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

    private LocationRequest getLocationRequest() {
        if (mLocationRequest == null) {
            setUpdateIntervals(Priority.LOW_POWER, Priority.LOW_POWER.getPeriodMillis(),
                    Priority.LOW_POWER.getFastestPeriodMillis());
        }
        return mLocationRequest;
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
                } else checkForPermissions();
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
                    Runnable requestUpdatesTask = new OnPermissionsTask();
                    if (isConnected()) requestUpdatesTask.run();
                    else {
                        mOnConnectedUpdateCallback = requestUpdatesTask;
                        Log.d(TAG, "Google API is not connected. "
                                + "Updates delayed until Google API is connected again.");
                    }
                } else if (status.getStatusCode() == CommonStatusCodes.RESOLUTION_REQUIRED) {
                    Log.i(TAG, "Location settings are not satisfied. "
                            + "Showing the user a dialog to upgrade location settings.");
                    try {
                        setRequestingPermissions(true);
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in the onActivityResult() of mContext
                        status.startResolutionForResult(mContext, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        Log.wtf(TAG, "PendingIntent unable to execute request", e);
                    }
                } else if (status.getStatusCode()
                        == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                    if (checkNetwork()) {
                        Log.w(TAG, "Location settings can't be changed to meet the requirements");
                        setHasPermissions(false);
                        setRequestingPermissions(false);
                    }
                }
            }
        };
    }

    public void onMapSettingsResults(int requestCode, int resultCode) {
        setRequestingPermissions(false);
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION:
                if (resultCode == Activity.RESULT_OK) {
                    Log.i(TAG, mContext.getString(
                            R.string.user_location_settings_agreed));
                    // Reconnect
                    connect();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.w(TAG, mContext.getString(R.string.location_connection_failed));
                    rescheduleLocationUpdateTimeout();
                }
                break;
            case REQUEST_CHECK_SETTINGS:
                if (resultCode == Activity.RESULT_OK) {
                    Log.i(TAG, mContext.getString(R.string.user_location_settings_agreed));
                    updateLocation();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.i(TAG, mContext.getString(R.string.user_location_settings_canceled));
                    updateLocation();
                }
                break;
            default:
        }
    }

    private void setRequestingPermissions(boolean requestingPermissions) {
        mRequestingPermissions = requestingPermissions;
        if (requestingPermissions) {
            cancelLocationUpdateTimeout();
        } else if (hasPermissions()) {
            rescheduleLocationUpdateTimeout();
        }
    }

    public boolean isRequestingPermissions() {
        return mRequestingPermissions;
    }

    private void setHasPermissions(boolean hasPermissions) {
        if (mHasPermissions != hasPermissions) {
            mHasPermissions = hasPermissions;
            if (!hasPermissions) {
                cancelLocationUpdateTimeout();
                mOnPermissionsChangedListeners.run();
            } else if (mLastLocation != null) {
                mOnPermissionsChangedListeners.run();
            }
            Log.i(TAG, TAG
                    + (hasPermissions ? " with permissions." : " without permissions."));
        }
    }

    public boolean hasPermissions() {
        return mHasPermissions;
    }

    public void addOnPermissionsChangedListener(@NonNull Runnable onPermissionsChanged) {
        mOnPermissionsChangedListeners.enqueue(onPermissionsChanged);
        if (hasPermissions() && !isRequestingPermissions()) {
            onPermissionsChanged.run();
        }
    }

    public void removeOnPermissionsChangedListener(@NonNull Runnable onPermissionsChanged) {
        mOnPermissionsChangedListeners.remove(onPermissionsChanged);
    }

    public void checkForPermissions() {
        if (!isRequestingPermissions()) {
            setRequestingPermissions(true);
            setHasPermissions(false);
            if (verifyRootPermitted() && verifyMockGpsPermissions()) {
                // If we don't have permissions we request them on runtime
                if (ContextCompat.checkSelfPermission(mContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, TAG + " without permissions! Requesting them if available.");
                    ActivityCompat.requestPermissions(mContext,
                            new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_ACCESS_LOCATION);
                } else if (checkNetwork()) {
                    setHasPermissions(mMockLocation == null);
                    setRequestingPermissions(false);
                    if (isConnected()) updateLocation();
                }
            }
        }
    }

    private boolean isLocationUpdateTimeoutSet() {
        return mOnUpdateTimeout != null;
    }

    private void setLocationUpdateTimeout() {
        if (!isLocationUpdateTimeoutSet()) {
            mOnUpdateTimeout = Timeout.schedule("LocationUpdateTimeout", new Runnable() {
                @Override
                public void run() {
                    if (!mLowConnectionWarning) {
                        checkForPermissions();
                        if (hasPermissions()) {
                            mLowConnectionWarning = true;
                            ConfirmDialog.show(mContext,
                                    mContext.getString(R.string.cannot_retrieve_location_title),
                                    mContext.getString(R.string.cannot_retrieve_location),
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface d, int w) {
                                            checkForPermissions();
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

    private void cancelLocationUpdateTimeout() {
        if (isLocationUpdateTimeoutSet()) {
            mOnUpdateTimeout.cancel();
            mOnUpdateTimeout = null;
        }
    }

    private long getLocationUpdateTimeoutTime(int multiplier) {
        return mIntervalRefresh * multiplier;
    }

    public boolean isMockLocationsEnabled() {
        return mMockLocationsEnabled;
    }

    public void setMockLocationsEnabled(boolean enabled) {
        mMockLocationsEnabled = enabled;
        if (!enabled) verifyMockGpsPermissions();
    }

    private static boolean isMockSettingsEnabled(Context context) {
        return !"0".equals(Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ALLOW_MOCK_LOCATION));
    }

    /**
     * Trusted only for Android < 18
     * @return true if has permissions to use mocked locations,
     * false if mocked locations are denied (a dialog will pop up)
     * @see #isLocationPlausible(Location)
     */
    private boolean verifyMockGpsPermissions() {
        if (!isMockLocationsEnabled() && isMockSettingsEnabled(getContext())) {
            showMockSettingsDialog();
            return false;
        }
        return true;
    }

    private boolean isMocked(@NonNull Location location) {
        boolean mocked = false;
        if (isMockLocationsEnabled()) {
            // If we are not sure, mark as permitted mock
            mocked = true;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (location.isFromMockProvider()) {
                mocked = true;
                showMockSettingsDialog();
            }
        } else if (!verifyMockGpsPermissions()) {
            mocked = true;
        }
        return mocked;
    }

    private boolean isLocationPlausible(@Nullable Location location) {
        if (location == null) return false;

        // isFromMockProvider may give wrong response for some applications
        // Warning: With rooted devices we cannot ensure that the location is not mocked
        boolean mocked = isMocked(location);

        if (mocked) mLastMockLocation = location;

        boolean permittedMock = !mocked || isMockLocationsEnabled();

        if (!permittedMock) setHasPermissions(false);

        boolean permitted = permittedMock
                && (mLastLocation == null || location.getAccuracy() < ACCURACY_THRESHOLD);

        if (mLastMockLocation == null) return permitted;

        if (location.distanceTo(mLastMockLocation) > KM) {
            mLastMockLocation = null;
            return permitted;
        }
        return false;
    }

    private void showMockSettingsDialog() {
        // Uncomment for repeat the dialog
        if (mMockWarning) return;
        setHasPermissions(false);
        setRequestingPermissions(true);
        String title = mContext.getString(R.string.fake_locations_prohibited);
        ConfirmDialog mockLocationsDialog = new ConfirmDialog(getContext(), title,
                mContext.getString(R.string.mocked_gps_prohibited));
        mockLocationsDialog.setPositiveButton(null, new OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int w) {
                setRequestingPermissions(false);
                checkForPermissions();
                setRequestingPermissions(false);
            }
        });
        mockLocationsDialog.setNegativeButton(getContext().getString(R.string.settings),
                new OnClickListener() {
                @Override
                public void onClick(DialogInterface d, int w) {
                    setRequestingPermissions(false);
                    getContext().startActivityForResult(new Intent(
                            Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS), 0);
                }
            }
        );
        mockLocationsDialog.show();
        mMockWarning = true;
    }

    private boolean verifyRootPermitted() {
        // Uncomment for repeat the dialog
        if (mRootWarning) return RootUtils.isRootPermitted();
        Runnable onProhibit = new Runnable() {
            @Override
                public void run() {
                setRequestingPermissions(true);
                setHasPermissions(false);
                cancelLocationUpdateTimeout();
            }
        };
        Runnable onConfirm = new Runnable() {
            @Override
            public void run() {
                setRequestingPermissions(false);
            }
        };
        mRootWarning = true;
        return RootUtils.verifyRootPermitted(getContext(), onProhibit, onConfirm);
    }

    public void repeatWarningsWhenNeeded() {
        if (!isRequestingPermissions()) {
            mRootWarning = false;
            mMockWarning = false;
            mLowConnectionWarning = false;
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
                }
            });
    }

    private void onPermissionsSuccess() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    getLocationRequest(), this);
            Log.v(TAG, "Updates requested successfully.");
        } catch (SecurityException e) {
            setHasPermissions(false);
            Log.e(TAG, "Permissions restricted due to SecurityException", e);
        }
    }

    private class OnPermissionsTask implements Runnable {
        @Override
        public void run() {
            if (!hasPermissions()) {
                throw new RuntimeException("Called OnPermissionTask without permissions!");
            }
            onPermissionsSuccess();
            mOnConnectedUpdateCallback = null;
            setRequestingPermissions(false);
        }
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
}
