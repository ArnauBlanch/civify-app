package com.civify.model;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.civify.adapter.LocalityCallback;
import com.civify.adapter.LocationAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;

public class CivifyMap implements UpdateLocationListener, OnMapReadyCallback {

    private static final String TAG = CivifyMap.class.getSimpleName();

    private static final int DEFAULT_ZOOM = 18;

    private final Activity mContext;
    private GoogleMap mGoogleMap;
    private SupportMapFragment mMapFragment;
    private CivifyMarkers mMarkers;
    private boolean mPlayerSet;
    private final LocationAdapter mLocationAdapter;

    public CivifyMap(@NonNull Activity context) {
        mContext = context;
        mLocationAdapter = new LocationAdapter(context, this);
        setRefreshMillis(LocationAdapter.Priority.HIGH_ACCURACY,
                LocationAdapter.Priority.HIGH_ACCURACY.getPeriodMillis(), 0L);
        setRefreshLocations(true);
    }

    @NonNull
    public SupportMapFragment getMapFragment() {
        return isMapFragmentSet() && isMapReady() ? mMapFragment : setNewMapFragment(this);
    }

    public boolean isMapReady() {
        return mMarkers != null && mPlayerSet;
    }

    public boolean isMapFragmentSet() {
        return mMapFragment != null;
    }

    public SupportMapFragment setNewMapFragment(@NonNull OnMapReadyCallback onMapReadyCallback) {
        mMapFragment = SupportMapFragment.newInstance(new GoogleMapOptions()
                .camera(new CameraPosition.Builder()
                    .target(LocationAdapter.ZERO)
                    .zoom(DEFAULT_ZOOM)
                    .build()));
        mMapFragment.getMapAsync(onMapReadyCallback);
        Log.d(TAG, "Map fragment created. Requesting Google Map...");
        return mMapFragment;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "Map received. Loading Civify Map...");
        mGoogleMap = googleMap;
        setMapSettings();
        mLocationAdapter.setOnUpdateLocationListener(this);
        Log.d(TAG, "Civify Map loaded.");
    }

    public UiSettings getUiSettings() {
        return mGoogleMap.getUiSettings();
    }

    private void setMapSettings() {
        UiSettings settings = getUiSettings();
        settings.setScrollGesturesEnabled(true);
        settings.setMapToolbarEnabled(true);
        settings.setZoomControlsEnabled(false);
        settings.setRotateGesturesEnabled(true);
        settings.setTiltGesturesEnabled(false);
        settings.setCompassEnabled(true);
        mMarkers = new CivifyMarkers();
    }

    public void addIssueMarker(@NonNull Issue issue) throws MapNotReadyException {
        if (isMapReady()) mMarkers.add(new IssueMarker(issue, this));
        else throw new MapNotReadyException();
    }

    public CivifyMarkers getMarkers() {
        return mMarkers;
    }

    GoogleMap getGoogleMap() {
        return mGoogleMap;
    }

    Activity getContext() {
        return mContext;
    }

    public void getAddressFromCurrentPosition(@NonNull LocalityCallback callback) {
        mLocationAdapter.getLocalityFromCurrentPosition(callback);
    }

    public void getAddress(@NonNull Location location, @NonNull LocalityCallback callback) {
        mLocationAdapter.getLocality(location, callback);
    }

    public void enableLocationButton() {
        try {
            mGoogleMap.setMyLocationEnabled(true);
            getUiSettings().setMyLocationButtonEnabled(true);
        } catch (SecurityException e) {
            Log.wtf(TAG, "Permissions should be checked before call enableLocationMarker()", e);
        }
    }

    public Location getCurrentLocation() {
        return mLocationAdapter.getLastLocation();
    }

    @Override
    public void onUpdateLocation(@NonNull Location location) {
        if (!mPlayerSet) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(
                    LocationAdapter.getLatLng(mLocationAdapter.getLastLocation())));
            mPlayerSet = true;
        }
    }

    public final void setRefreshLocations(boolean refreshLocations) {
        mLocationAdapter.setAutoRefresh(refreshLocations);
    }

    public final void setRefreshMillis(@NonNull LocationAdapter.Priority priority,
                                 long millisPeriod, long millisMinimumPeriod) {
        mLocationAdapter.setUpdateIntervals(priority, millisPeriod, millisMinimumPeriod);
    }

    public void enableLocationUpdates() {
        mLocationAdapter.connect();
    }

    public void disableLocationUpdates() {
        if (!mLocationAdapter.isRequestingPermissions()) {
            mLocationAdapter.disconnect();
            mPlayerSet = false;
            mMapFragment = null;
        }
    }

    public void onRequestPermissionsResult(int requestCode, int[] grantResults) {
        if (requestCode == LocationAdapter.PERMISSION_ACCESS_LOCATION
                // If request is cancelled, the result arrays are empty
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mLocationAdapter.checkForPermissions();
        }
    }

    public void onMapSettingsResults(int requestCode, int resultCode) {
        mLocationAdapter.onMapSettingsResults(requestCode, resultCode);
    }
}
