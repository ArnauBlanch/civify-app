package com.civify.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.civify.adapter.LocalityCallback;
import com.google.android.gms.maps.model.LatLng;

public interface CivifyMarker<E extends Issue> {
    @NonNull
    String getId();

    @NonNull
    E getIssue();

    void getAddress(LocalityCallback callback);

    float getDistanceFromCurrentLocation();

    @NonNull
    CivifyMarker<E> setMarkerIcon(@DrawableRes int markerIcon);

    LatLng getMarkerPosition();

    @NonNull
    CivifyMarker<E> setMarkerPosition(@NonNull LatLng position);

    float getRotation();

    @NonNull
    CivifyMarker<E> setRotation(float rotation);

    boolean isDraggable();

    @NonNull
    CivifyMarker<E> setDraggable(boolean draggable);

    boolean isVisible();

    @NonNull
    CivifyMarker<E> setVisible(boolean visible);

    void remove();

    boolean isPresent();
}
