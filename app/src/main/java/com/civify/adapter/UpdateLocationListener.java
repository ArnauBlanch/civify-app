package com.civify.adapter;

import android.location.Location;
import android.support.annotation.NonNull;

public interface UpdateLocationListener {
    void onUpdateLocation(@NonNull Location location);
}
