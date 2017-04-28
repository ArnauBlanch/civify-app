package com.civify.activity.createissue;

import android.Manifest.permission;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.civify.R;

public abstract class CameraGalleryLocationActivity extends CameraGalleryActivity
        implements LocationListener {
    public static final int MIN_TIME = 400;

    private LocationManager mLocationManager;
    private String mLocationProvider;
    private Location mLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mLocationProvider = mLocationManager.getBestProvider(criteria, false);
        if (checkAndRequestPermission(permission.ACCESS_FINE_LOCATION,
                R.string.need_to_allow_location_permission)) {
            mLocation = mLocationManager.getLastKnownLocation(mLocationProvider);

            // Initialize the location fields
            if (mLocation != null) {
                onLocationChanged(mLocation);
            } else {
                mLocationManager.requestLocationUpdates(mLocationProvider, MIN_TIME, 1, this);
                showError(R.string.couldnt_get_location);
            }
        }
    }

    protected Location getCurrentLocation() {
        return mLocation;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkAndRequestPermission(permission.ACCESS_FINE_LOCATION, R.string
                .need_to_allow_location_permission)) {
            mLocationManager.requestLocationUpdates(mLocationProvider, MIN_TIME, 1, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
