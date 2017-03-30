package com.civify.adapter;

import android.support.annotation.NonNull;

public interface LocalityCallback {
    void onLocalityResponse(@NonNull String address);

    void onLocalityError();
}
