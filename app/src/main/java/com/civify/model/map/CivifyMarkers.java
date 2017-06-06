package com.civify.model.map;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CivifyMarkers implements OnMarkerClickListener {

    public static final String TAG = CivifyMarkers.class.getSimpleName();

    private HashMap<String, IssueMarker> mMarkers = new HashMap<>();
    private ClusterManager<IssueMarker> mClusterManager;

    CivifyMarkers(@NonNull CivifyMap map) {
        attachToMap(map);
    }

    public final void attachToMap(@NonNull CivifyMap map) {
        setUpClusterer(map);
        addAll(getAll());
    }

    @Nullable
    public IssueMarker get(@NonNull String tag) {
        String key = idify(tag);
        IssueMarker marker = mMarkers.get(key);
        if (marker != null) {
            if (marker.isPresent()) {
                return marker;
            }
            mMarkers.remove(key);
        }
        return null;
    }

    public Set<IssueMarker> get(@NonNull LatLng position) {
        Set<IssueMarker> atSamePosition = new HashSet<>();
        for (IssueMarker marker : getAll()) {
            if (marker.getPosition().equals(position)) atSamePosition.add(marker);
        }
        return atSamePosition;
    }

    public void add(@NonNull IssueMarker issueMarker) {
        String tag = issueMarker.getTag();
        mMarkers.put(idify(tag), issueMarker);

        //issueMarker.attachToMap();
        mClusterManager.addItem(issueMarker);
        mClusterManager.cluster();
        Log.v(TAG, "Added marker " + tag + '(' + issueMarker.getIssue().getTitle() + ')');
    }

    public void addAll(@NonNull Collection<? extends IssueMarker> issueMarkers) {
        for (IssueMarker marker : issueMarkers) add(marker);
    }

    public void remove(@NonNull String tag) {
        IssueMarker issueMarker = mMarkers.remove(idify(tag));
        if (issueMarker != null && issueMarker.isPresent()) {
            mClusterManager.removeItem(issueMarker);
            mClusterManager.cluster();
            //issueMarker.remove();
        }
    }

    public void clear() {
        Collection<IssueMarker> markers = getAll();
        for (IssueMarker marker : markers) marker.remove();
        markers.clear();
    }

    public boolean isEmpty() {
        return mMarkers.isEmpty();
    }

    public int size() {
        return mMarkers.size();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        mClusterManager.onMarkerClick(marker);
        Object markerTag = marker.getTag();
        if (markerTag != null) {
            String tag = markerTag.toString();
            IssueMarker issueMarker = mMarkers.get(idify(tag));
            if (issueMarker != null) {
                Log.v(TAG, "Marker " + tag + " clicked.");
                issueMarker.getIssue().showIssueDetails();
                return true;
            }
        }
        return false;
    }

    public Collection<IssueMarker> getAll() {
        return mMarkers.values();
    }

    @NonNull
    private static String idify(@NonNull String possibleKey) {
        return possibleKey.toLowerCase();
    }

    private void setUpClusterer(@NonNull CivifyMap map) {
        GoogleMap googleMap = map.getGoogleMap();
        mClusterManager = new ClusterManager<>(map.getContext(), googleMap);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnCameraIdleListener(mClusterManager);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{\n");
        for (IssueMarker marker : getAll()) builder.append(marker).append('\n');
        return builder.append('}').toString();
    }

}
