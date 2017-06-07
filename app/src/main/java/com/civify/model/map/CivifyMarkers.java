package com.civify.model.map;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.civify.model.issue.Issue;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CivifyMarkers
        extends ClusterManager<IssueMarker>
        implements ClusterManager.OnClusterItemClickListener<IssueMarker> {

    public static final String TAG = CivifyMarkers.class.getSimpleName();

    private HashMap<String, IssueMarker> mMarkers = new HashMap<>();

    protected CivifyMarkers(@NonNull CivifyMap map) {
        super(map.getContext(), map.getGoogleMap());
        attachToMap(map);
    }

    public final void attachToMap(@NonNull CivifyMap map) {
        setUpClusterer(map);
        addItems(getAllRenderedMarkers());
    }

    @Nullable
    public IssueMarker get(@NonNull String tag) {
        String key = idify(tag);
        IssueMarker marker = mMarkers.get(key);
        if (marker != null) return marker;
        return null;
    }

    public Set<IssueMarker> get(@NonNull LatLng position) {
        Set<IssueMarker> atSamePosition = new HashSet<>();
        for (IssueMarker marker : getAllRenderedMarkers()) {
            if (marker.getPosition().equals(position)) atSamePosition.add(marker);
        }
        return atSamePosition;
    }

    @Nullable
    private static IssueMarker filterRendered(@Nullable IssueMarker marker) {
        return marker != null && marker.isRendered() ? marker : null;
    }

    public Collection<IssueMarker> getAllRenderedMarkers() {
        Collection<IssueMarker> issueMarkers = mMarkers.values();
        List<IssueMarker> renderedIssueMarkers = new LinkedList<>();
        for (IssueMarker issueMarker : issueMarkers) {
            IssueMarker filtered = filterRendered(issueMarker);
            if (filtered != null) renderedIssueMarkers.add(filtered);
        }
        return renderedIssueMarkers;
    }

    public Collection<Issue> getAllIssues() {
        Collection<IssueMarker> issueMarkers = mMarkers.values();
        List<Issue> issues = new LinkedList<>();
        for (IssueMarker marker : issueMarkers) issues.add(marker.getIssue());
        return issues;
    }

    @Override
    public void addItem(@NonNull IssueMarker issueMarker) {
        super.addItem(issueMarker);
        addInternal(issueMarker);
        cluster();
    }

    private void addInternal(@NonNull IssueMarker issueMarker) {
        mMarkers.put(idify(issueMarker.getTag()), issueMarker);
        Log.v(TAG, "Added marker " + issueMarker.getTagWithTitle());
    }

    @Override
    public void addItems(@NonNull Collection<IssueMarker> issueMarkers) {
        super.addItems(issueMarkers);
        for (IssueMarker marker : issueMarkers) addInternal(marker);
        cluster();
    }

    public void removeItem(@NonNull String tag) {
        IssueMarker issueMarker = mMarkers.get(idify(tag));
        if (issueMarker != null) removeItem(issueMarker);
    }

    @Override
    public void removeItem(@NonNull IssueMarker issueMarker) {
        super.removeItem(issueMarker);
        removeInternal(issueMarker);
        cluster();
    }

    private void removeInternal(@NonNull IssueMarker issueMarker) {
        mMarkers.remove(idify(issueMarker.getTag()));
        issueMarker.remove();
        Log.v(TAG, "Removed marker " + issueMarker.getTagWithTitle());
    }

    @Override
    public void clearItems() {
        super.clearItems();
        for (IssueMarker marker : mMarkers.values()) removeInternal(marker);
        cluster();
    }

    public boolean isEmpty() {
        return mMarkers.isEmpty();
    }

    public int size() {
        return mMarkers.size();
    }

    @Override
    public boolean onClusterItemClick(final IssueMarker issueMarker) {
        Log.v(TAG, "Marker " + issueMarker.getTagWithTitle() + " clicked.");
        issueMarker.getIssue().showIssueDetails();
        return true;
    }

    @NonNull
    private static String idify(@NonNull String possibleKey) {
        return possibleKey.toLowerCase();
    }

    private void setUpClusterer(@NonNull CivifyMap map) {
        GoogleMap googleMap = map.getGoogleMap();
        setRenderer(new IssueMarkerClusterRenderer(map, this));
        setOnClusterItemClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnCameraIdleListener(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{\n");
        for (IssueMarker marker : mMarkers.values()) builder.append(marker).append('\n');
        return builder.append('}').toString();
    }

}
