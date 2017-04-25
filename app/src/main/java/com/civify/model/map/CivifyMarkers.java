package com.civify.model.map;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class CivifyMarkers implements Iterable<CivifyMarker<?>>, OnMarkerClickListener {

    public static final String TAG = CivifyMarkers.class.getSimpleName();

    private HashMap<String, CivifyMarker<?>> mMarkers = new HashMap<>();

    CivifyMarkers(@NonNull CivifyMap map) {
        setMap(map);
    }

    public final void setMap(@NonNull CivifyMap map) {
        map.getGoogleMap().setOnMarkerClickListener(this);
        if (!mMarkers.isEmpty()) {
            for (CivifyMarker<?> marker : mMarkers.values()) marker.setMap(map);
        }
    }

    @Nullable
    public CivifyMarker<?> get(@NonNull String tag) {
        String key = idify(tag);
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
        String tag = civifyMarker.getTag();
        mMarkers.put(idify(tag), civifyMarker);
        Log.v(TAG, "Added marker " + tag);
    }

    public void addAll(@NonNull Collection<? extends CivifyMarker<?>> civifyMarkers) {
        for (CivifyMarker<?> marker : civifyMarkers) add(marker);
    }

    public void remove(@NonNull String tag) {
        CivifyMarker<?> civifyMarker = mMarkers.remove(idify(tag));
        if (civifyMarker != null) civifyMarker.remove();
        else Log.v(TAG, tag + " not found.");
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

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Object markerTag = marker.getTag();
        if (markerTag != null) {
            String tag = markerTag.toString();
            CivifyMarker<?> civifyMarker = mMarkers.get(idify(tag));
            if (civifyMarker != null) {
                Log.v(TAG, "Marker " + tag + " clicked.");
                
                // return true;
            }
        }
        return false;
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
