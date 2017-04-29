package com.civify.model.map;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CivifyMarkers implements OnMarkerClickListener {

    public static final String TAG = CivifyMarkers.class.getSimpleName();

    private HashMap<String, IssueMarker> mMarkers = new HashMap<>();
    private CivifyMap mMap;

    CivifyMarkers(@NonNull CivifyMap map) {
        attachToMap(map);
    }

    public final void attachToMap(@NonNull CivifyMap map) {
        mMap = map;
        map.getGoogleMap().setOnMarkerClickListener(this);
        if (!mMarkers.isEmpty()) {
            for (IssueMarker marker : mMarkers.values()) marker.attachToMap(map.getGoogleMap());
        }
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
        for (IssueMarker marker : mMarkers.values()) {
            if (marker.getPosition().equals(position)) atSamePosition.add(marker);
        }
        return atSamePosition;
    }

    public void add(@NonNull IssueMarker issueMarker) {
        String tag = issueMarker.getTag();
        mMarkers.put(idify(tag), issueMarker);
        issueMarker.attachToMap();
        Log.v(TAG, "Added marker " + tag + '(' + issueMarker.getIssue().getTitle() + ')');
    }

    public void addAll(@NonNull Collection<? extends IssueMarker> issueMarkers) {
        for (IssueMarker marker : issueMarkers) add(marker);
    }

    public void remove(@NonNull String tag) {
        IssueMarker issueMarker = mMarkers.remove(idify(tag));
        if (issueMarker != null && issueMarker.isPresent()) issueMarker.remove();
    }

    public void clear() {
        Collection<IssueMarker> markers = mMarkers.values();
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

    @NonNull
    private static String idify(@NonNull String possibleKey) {
        return possibleKey.toLowerCase();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{\n");
        for (IssueMarker marker : mMarkers.values()) builder.append(marker).append('\n');
        return builder.append('}').toString();
    }

}
