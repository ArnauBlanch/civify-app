package com.civify.model.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.Log;

import com.civify.adapter.GeocoderAdapter;
import com.civify.adapter.LocalityCallback;
import com.civify.adapter.LocationAdapter;
import com.civify.model.issue.Issue;
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
                .title(issue.getTitle())
                .draggable(false)
                .flat(false)
        );
        mMarker.setTag(issue.getIssueAuthToken());
        mPresent = true;
    }

    @NonNull
    @Override
    public String getTag() {
        Object tag = mMarker.getTag();
        if (tag != null) return tag.toString();
        Log.wtf(TAG, "Marker without tag cannot be referenced!");
        return "";
    }

    @NonNull
    @Override
    public Issue getIssue() {
        return mIssue;
    }

    @Override
    public void getAddress(@NonNull LocalityCallback callback) {
        GeocoderAdapter.getLocality(mMap.getContext(),
                LocationAdapter.getLocation(getMarkerPosition()),
                callback);
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
        mIssue.setLatitude((float) position.latitude);
        mIssue.setLongitude((float) position.longitude);
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
            Log.v(TAG, "Removed marker " + getTag());
        } else Log.v(TAG, getTag() + " already removed.");
    }

    @Override
    public boolean isPresent() {
        return mPresent;
    }
}
