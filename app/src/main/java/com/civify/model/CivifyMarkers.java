package com.civify.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class CivifyMarkers implements Iterable<CivifyMarker<?>> {

    public static final String TAG = CivifyMarkers.class.getSimpleName();

    private HashMap<String, CivifyMarker<?>> mMarkers = new HashMap<>();

    @Nullable
    public CivifyMarker<?> get(@NonNull String id) {
        String key = idify(id);
        CivifyMarker<?> marker = mMarkers.get(key);
        if (marker != null) {
            if (marker.isPresent()) {
                return marker;
            }
            mMarkers.remove(key);
        }
        return null;
    }

    public void add(@NonNull CivifyMarker<?> civifyMarker) {
        String authToken = civifyMarker.getId();
        mMarkers.put(idify(authToken), civifyMarker);
        Log.v(TAG, "Added marker " + authToken);
    }

    public void remove(@NonNull String id) {
        CivifyMarker<?> civifyMarker = mMarkers.remove(idify(id));
        if (civifyMarker != null) civifyMarker.remove();
        else Log.v(TAG, id + " not found.");
    }

    public void clear() {
        mMarkers.clear();
    }

    public boolean isEmpty() {
        return mMarkers.isEmpty();
    }

    public int size() {
        return mMarkers.size();
    }

    @NonNull
    private static String idify(@NonNull String possibleKey) {
        return possibleKey.toLowerCase();
    }

    @Override
    public Iterator<CivifyMarker<?>> iterator() {
        return new CivifyIssueMarkerIterator(mMarkers.values());
    }

    private static final class CivifyIssueMarkerIterator implements Iterator<CivifyMarker<?>> {

        private final Iterator<CivifyMarker<?>> mOriginal;
        private CivifyMarker<?> mLast;

        private CivifyIssueMarkerIterator(@NonNull Collection<CivifyMarker<?>> civifyMarkers) {
            mOriginal = civifyMarkers.iterator();
        }

        @Override
        public boolean hasNext() {
            return mOriginal.hasNext();
        }

        @Override
        public CivifyMarker<?> next() {
            mLast = mOriginal.next();
            return mLast;
        }

        @Override
        public void remove() {
            mLast.remove();
        }
    }

}
