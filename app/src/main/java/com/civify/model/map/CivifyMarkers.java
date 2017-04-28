package com.civify.model.map;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CivifyMarkers implements Iterable<IssueMarker>, OnMarkerClickListener {

    public static final String TAG = CivifyMarkers.class.getSimpleName();

    private HashMap<String, IssueMarker> mMarkers = new HashMap<>();

    CivifyMarkers(@NonNull CivifyMap map) {
        attachToMap(map.getGoogleMap());
    }

    public final void attachToMap(@NonNull GoogleMap map) {
        map.setOnMarkerClickListener(this);
        if (!mMarkers.isEmpty()) {
            for (IssueMarker marker : this) marker.attachToMap(map);
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
        for (IssueMarker marker : this) {
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
        Iterator<IssueMarker> it = iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
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
                // TODO: Go to details
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
    public Iterator<IssueMarker> iterator() {
        return new CivifyIssueMarkerIterator(mMarkers.values());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{\n");
        for (IssueMarker marker : this) builder.append(marker).append('\n');
        return builder.append('}').toString();
    }

    private static final class CivifyIssueMarkerIterator implements Iterator<IssueMarker> {

        private final Iterator<IssueMarker> mOriginal;
        private IssueMarker mLast;

        private CivifyIssueMarkerIterator(@NonNull Collection<IssueMarker> issueMarkers) {
            mOriginal = issueMarkers.iterator();
        }

        @Override
        public boolean hasNext() {
            return mOriginal.hasNext();
        }

        @Override
        public IssueMarker next() {
            mLast = mOriginal.next();
            return mLast;
        }

        @Override
        public void remove() {
            mLast.remove();
        }
    }

}
