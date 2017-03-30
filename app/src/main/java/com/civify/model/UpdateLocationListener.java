package com.civify.model;

import android.location.Location;
import android.support.annotation.NonNull;

public interface UpdateLocationListener {
    void onUpdateLocation(@NonNull Location location);
}
