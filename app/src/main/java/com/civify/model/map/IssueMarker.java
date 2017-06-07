package com.civify.model.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.Log;

import com.civify.adapter.LocationAdapter;
import com.civify.model.issue.Issue;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

public class IssueMarker implements ClusterItem {

    private static final String TAG = IssueMarker.class.getSimpleName();

    private CivifyMap mMap;
    private Issue mIssue;
    private Marker mMarker;
    private String mTag;
    private boolean mUpdated;

    protected IssueMarker(@NonNull Issue issue, @NonNull CivifyMap map) {
        mIssue = issue;
        mTag = issue.getIssueAuthToken();
        mMap = map;
    }

    protected void setup(MarkerOptions markerOptions) {
        markerOptions.position(
                LocationAdapter.getLatLng(mIssue.getLatitude(), mIssue.getLongitude()))
                .title(mIssue.getTitle())
                .draggable(false)
                .flat(false);
        setIcon(mIssue.getCategory().getMarker(), markerOptions);
        mUpdated = true;
    }

    protected void render(@NonNull Marker marker, @NonNull ClusterManager<IssueMarker> manager) {
        mMarker = marker;
        if (!mUpdated) {
            manager.cluster();
            mUpdated = true;
        }
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
        if (isRendered()) {
            setPosition(issue.getPosition());
            setIcon(issue.getCategory().getMarker());
            mMarker.setTitle(issue.getTitle());
            mUpdated = false;
        }
    }

    @NonNull
    public final IssueMarker setIcon(@DrawableRes int markerIcon) {
        checkRendered();
        setIcon(markerIcon, new IconCallback() {
            @Override
            public void setIcon(@NonNull BitmapDescriptor icon) {
                mMarker.setIcon(icon);
                Log.d(TAG, "Icon set to marker");
            }
        });
        return this;
    }

    @NonNull
    private IssueMarker setIcon(@DrawableRes int markerIcon, final MarkerOptions options) {
        setIcon(markerIcon, new IconCallback() {
            @Override
            public void setIcon(@NonNull BitmapDescriptor icon) {
                options.icon(icon);
            }
        });
        return this;
    }

    private IssueMarker setIcon(@DrawableRes int markerIcon, IconCallback callback) {
        Bitmap icon = BitmapFactory.decodeResource(mMap.getContext().getResources(), markerIcon);
        if (icon != null) callback.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
        return this;
    }

    @Override
    public LatLng getPosition() {
        return mIssue.getPosition();
    }

    @NonNull
    public IssueMarker setPosition(@NonNull LatLng position) {
        checkRendered();
        mMarker.setPosition(position);
        mIssue.setLatitude((float) position.latitude);
        mIssue.setLongitude((float) position.longitude);
        return this;
    }

    public float getRotation() {
        checkRendered();
        return mMarker.getRotation();
    }

    @NonNull
    public IssueMarker setRotation(float rotation) {
        checkRendered();
        mMarker.setRotation(rotation);
        return this;
    }

    public boolean isVisible() {
        checkRendered();
        return mMarker.isVisible();
    }

    @NonNull
    public IssueMarker setVisible(boolean visible) {
        checkRendered();
        mMarker.setVisible(visible);
        return this;
    }

    public boolean isDraggable() {
        checkRendered();
        return mMarker.isDraggable();
    }

    @NonNull
    public IssueMarker setDraggable(boolean draggable) {
        checkRendered();
        mMarker.setDraggable(draggable);
        return this;
    }

    protected void remove() {
        if (isRendered()) mMarker = null;
    }

    public boolean isRendered() {
        return mMarker != null;
    }

    private void checkRendered() {
        if (!isRendered()) throw new RuntimeException("This marker is not rendered!");
    }

    public String getTagWithTitle() {
        return getIssue().getTitle() + " (" + getTag() + ')';
    }

    @Override
    public String toString() {
        return getTagWithTitle();
    }

    private interface IconCallback {
        void setIcon(@NonNull BitmapDescriptor icon);
    }
}
