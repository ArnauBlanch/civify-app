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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collection;
import java.util.LinkedList;

public class IssueMarker implements CivifyMarker<Issue> {

    private static final String TAG = IssueMarker.class.getSimpleName();

    private CivifyMap mMap;
    private GoogleMap mAttached;
    private Issue mIssue;
    private Marker mMarker;
    private boolean mPresent;

    IssueMarker(@NonNull Issue issue, @NonNull CivifyMap map) {
        mIssue = issue;
        mMap = map;
        attachToMap(map.getGoogleMap());
    }

    @Override
    public void attachToMap(@NonNull GoogleMap googleMap) {
        if (isPresent()) mMarker.remove();
        mAttached = googleMap;
        addToMap();
    }

    private void addToMap() {
        mMarker = mAttached.addMarker(new MarkerOptions()
                .position(LocationAdapter.getLatLng(mIssue.getLatitude(), mIssue.getLongitude()))
                .title(mIssue.getTitle())
                .draggable(false)
                .flat(false)
        );
        mMarker.setTag(mIssue.getIssueAuthToken());
        // FIXME: Resize icons
        // setIcon(mIssue.getCategory().getMarker());
        mPresent = true;
    }

    @Override
    public GoogleMap getAttachedMap() {
        return mAttached;
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
                LocationAdapter.getLocation(getPosition()),
                callback);
    }

    /** @return distance in meters between the current geolocated position and this marker. */
    @Override
    public float getDistanceFromCurrentLocation() {
        return mMap.getCurrentLocation().distanceTo(
                LocationAdapter.getLocation(getPosition()));
    }

    @NonNull
    @Override
    public final IssueMarker setIcon(@DrawableRes int markerIcon) {
        Bitmap icon = BitmapFactory.decodeResource(mMap.getContext().getResources(), markerIcon);
        if (icon != null) {
            mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
        }
        return this;
    }

    @Override
    public LatLng getPosition() {
        return mMarker.getPosition();
    }

    @NonNull
    @Override
    public IssueMarker setPosition(@NonNull LatLng position) {
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
            mAttached = null;
            String thisMarker = getTag();
            CivifyMarkers markers = mMap.getMarkers();
            if (markers != null && markers.get(thisMarker) != null) markers.remove(thisMarker);
            Log.v(TAG, "Removed marker " + getTag());
        }
    }

    @Override
    public boolean isPresent() {
        return mPresent;
    }

    @Override
    public String toString() {
        return getTag();
    }

    public static Collection<IssueMarker> getMarkers(Collection<Issue> issues, CivifyMap map) {
        Collection<IssueMarker> markers = new LinkedList<>();
        for (Issue issue : issues) markers.add(new IssueMarker(issue, map));
        return markers;
    }
}
