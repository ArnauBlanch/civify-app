package com.civify.model.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.Log;

import com.civify.adapter.LocationAdapter;
import com.civify.model.issue.Issue;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class IssueMarker {

    private static final String TAG = IssueMarker.class.getSimpleName();

    private CivifyMap mMap;
    private GoogleMap mAttached;
    private Issue mIssue;
    private Marker mMarker;
    private String mTag;
    private boolean mPresent;

    IssueMarker(@NonNull Issue issue, @NonNull CivifyMap map) {
        mIssue = issue;
        mTag = issue.getIssueAuthToken();
        mMap = map;
    }

    public void attachToMap(@NonNull GoogleMap googleMap) {
        if (isPresent()) mMarker.remove();
        mAttached = googleMap;
        mMarker = mAttached.addMarker(new MarkerOptions().position(
                LocationAdapter.getLatLng(mIssue.getLatitude(), mIssue.getLongitude()))
                .title(mIssue.getTitle())
                .draggable(false)
                .flat(false));
        mMarker.setTag(mTag);
        setIcon(mIssue.getCategory().getMarker());
        mPresent = true;
    }

    public void attachToMap() {
        attachToMap(mMap.getGoogleMap());
    }

    public GoogleMap getAttachedMap() {
        return mAttached;
    }

    @NonNull
    public String getTag() {
        return mTag;
    }

    @NonNull
    public Issue getIssue() {
        return mIssue;
    }

    @NonNull
    public final IssueMarker setIcon(@DrawableRes int markerIcon) {
        Bitmap icon = BitmapFactory.decodeResource(mMap.getContext().getResources(), markerIcon);
        if (icon != null) {
            mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
        }
        return this;
    }

    public LatLng getPosition() {
        return mMarker.getPosition();
    }

    @NonNull
    public IssueMarker setPosition(@NonNull LatLng position) {
        mMarker.setPosition(position);
        mIssue.setLatitude((float) position.latitude);
        mIssue.setLongitude((float) position.longitude);
        return this;
    }

    public float getRotation() {
        return mMarker.getRotation();
    }

    @NonNull
    public IssueMarker setRotation(float rotation) {
        mMarker.setRotation(rotation);
        return this;
    }

    public boolean isVisible() {
        return mMarker.isVisible();
    }

    @NonNull
    public IssueMarker setVisible(boolean visible) {
        mMarker.setVisible(visible);
        return this;
    }

    public boolean isDraggable() {
        return mMarker.isDraggable();
    }

    @NonNull
    public IssueMarker setDraggable(boolean draggable) {
        mMarker.setDraggable(draggable);
        return this;
    }

    void remove() {
        if (isPresent()) {
            mMarker.remove();
            mPresent = false;
            mAttached = null;
            Log.v(TAG, "Removed marker " + getTag() + '(' + getIssue().getTitle() + ')');
        }
    }

    public boolean isPresent() {
        return mPresent;
    }

    @Override
    public String toString() {
        return getTag();
    }
}
