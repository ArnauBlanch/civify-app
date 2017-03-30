package com.civify.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.Log;

import com.civify.adapter.LocalityCallback;
import com.civify.adapter.LocationAdapter;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class IssueMarker implements CivifyMarker<Issue> {

    private static final String TAG = IssueMarker.class.getSimpleName();

    private CivifyMap mMap;
    private Issue mIssue;
    private Marker mMarker;
    private boolean mPresent;

    IssueMarker(@NonNull Issue issue, @NonNull CivifyMap map) {
        mIssue = issue;
        mMap = map;
        mMarker = mMap.getGoogleMap().addMarker(new MarkerOptions()
                .position(LocationAdapter.getLatLng(issue.getLatitude(), issue.getLongitude()))
                .draggable(false)
                .flat(false)
        );
        mPresent = true;
    }

    @NonNull
    @Override
    public String getId() {
       return mIssue.getAuthToken();
    }

    @NonNull
    @Override
    public Issue getIssue() {
        return mIssue;
    }

    @Override
    public void getAddress(@NonNull LocalityCallback callback) {
        mMap.getAddress(LocationAdapter.getLocation(getMarkerPosition()), callback);
    }

    /** @return distance in meters between the current geolocated position and this marker. */
    @Override
    public float getDistanceFromCurrentLocation() {
        return mMap.getCurrentLocation().distanceTo(
                LocationAdapter.getLocation(getMarkerPosition()));
    }

    @NonNull
    @Override
    public IssueMarker setMarkerIcon(@DrawableRes int markerIcon) {
        Bitmap icon = BitmapFactory.decodeResource(mMap.getContext().getResources(), markerIcon);
        if (icon != null) {
            mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
        }
        return this;
    }

    @Override
    public LatLng getMarkerPosition() {
        return mMarker.getPosition();
    }

    @NonNull
    @Override
    public IssueMarker setMarkerPosition(@NonNull LatLng position) {
        mMarker.setPosition(position);
        mIssue.setLatitude(position.latitude);
        mIssue.setLongitude(position.longitude);
        return this;
    }

    @Override
    public float getRotation() {
        return mMarker.getRotation();
    }

    @NonNull
    @Override
    public IssueMarker setRotation(float rotation) {
        mMarker.setRotation(rotation);
        return this;
    }

    @Override
    public boolean isVisible() {
        return mMarker.isVisible();
    }

    @NonNull
    @Override
    public IssueMarker setVisible(boolean visible) {
        mMarker.setVisible(visible);
        return this;
    }

    @Override
    public boolean isDraggable() {
        return mMarker.isDraggable();
    }

    @NonNull
    @Override
    public IssueMarker setDraggable(boolean draggable) {
        mMarker.setDraggable(draggable);
        return this;
    }

    @Override
    public void remove() {
        if (isPresent()) {
            mMarker.remove();
            mPresent = false;
            Log.v(TAG, "Removed marker " + getId());
        } else Log.v(TAG, getId() + " already removed.");
    }

    @Override
    public boolean isPresent() {
        return mPresent;
    }
}
