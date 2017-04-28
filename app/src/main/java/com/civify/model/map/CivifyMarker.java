package com.civify.model.map;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.civify.adapter.LocalityCallback;
import com.civify.model.issue.Issue;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public interface CivifyMarker<E extends Issue> extends Serializable {
    void setMap(@NonNull CivifyMap map);

    @NonNull
    String getTag();

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
