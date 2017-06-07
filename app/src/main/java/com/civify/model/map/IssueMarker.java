package com.civify.model.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.Log;

import com.civify.adapter.LocationAdapter;
import com.civify.model.issue.Issue;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;

public class IssueMarker implements ClusterItem {

    private static final String TAG = IssueMarker.class.getSimpleName();

    private CivifyMap mMap;
    private Issue mIssue;
    private MarkerOptions mMarkerOptions;
    private String mTag;
    private boolean mPresent;

    protected IssueMarker(@NonNull Issue issue, @NonNull CivifyMap map) {
        mIssue = issue;
        mTag = issue.getIssueAuthToken();
        mMap = map;
    }

    protected void setup(MarkerOptions markerOptions) {
        mMarkerOptions = markerOptions;
        mMarkerOptions.position(
                LocationAdapter.getLatLng(mIssue.getLatitude(), mIssue.getLongitude()))
                .title(mIssue.getTitle())
                .draggable(false)
                .flat(false);
        setIcon(mIssue.getCategory().getMarker());
        mPresent = true;
    }

    @NonNull
    public String getTag() {
        return mTag;
    }

    @NonNull
    public Issue getIssue() {
        return mIssue;
    }

    public void setIssue(@NonNull Issue issue) {
        mIssue = issue;
    }

    @NonNull
    public final IssueMarker setIcon(@DrawableRes int markerIcon) {
        Bitmap icon = BitmapFactory.decodeResource(mMap.getContext().getResources(), markerIcon);
        if (icon != null) {
            mMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }
        return this;
    }

    @Override
    public LatLng getPosition() {
        return mIssue.getPosition();
    }

    @NonNull
    public IssueMarker setPosition(@NonNull LatLng position) {
        mMarkerOptions.position(position);
        mIssue.setLatitude((float) position.latitude);
        mIssue.setLongitude((float) position.longitude);
        return this;
    }

    public float getRotation() {
        return mMarkerOptions.getRotation();
    }

    @NonNull
    public IssueMarker setRotation(float rotation) {
        mMarkerOptions.rotation(rotation);
        return this;
    }

    public boolean isVisible() {
        return mMarkerOptions.isVisible();
    }

    @NonNull
    public IssueMarker setVisible(boolean visible) {
        mMarkerOptions.visible(visible);
        return this;
    }

    public boolean isDraggable() {
        return mMarkerOptions.isDraggable();
    }

    @NonNull
    public IssueMarker setDraggable(boolean draggable) {
        mMarkerOptions.draggable(draggable);
        return this;
    }

    void remove() {
        if (isPresent()) {
            mPresent = false;
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
