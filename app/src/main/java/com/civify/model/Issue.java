package com.civify.model;

import android.support.annotation.NonNull;

import com.civify.adapter.LocationAdapter;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Temporal Issue (Stub)
public class Issue {

    private static final int EXAMPLE_N = 10;
    private static final int EXAMPLE_DIST = 20;

    private final String mAuthToken;
    private String mTitle;
    private double mLatitude, mLongitude;

    public Issue(String authToken, String title, double latitude, double longitude) {
        mTitle = title;
        mAuthToken = authToken;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public String getAuthToken() {
        return mAuthToken;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public static List<Issue> getExamples(@NonNull LatLng center) {
        List<Issue> examples = new ArrayList<>();
        int pi = 1, pj = 1;
        int dist = 0;
        for (int i = 0; i < EXAMPLE_N; ++i) {
            if (i % 4 == 0) dist += EXAMPLE_DIST;
            LatLng position = LocationAdapter.move(center, dist * pi, dist * pj);
            examples.add(new Issue(String.valueOf(System.nanoTime()),
                    "Issue " + i, position.latitude, position.longitude));
            if (i % 2 == 0) pi = -pi;
            else pj = -pj;
        }
        return examples;
    }

    public Date getDate() {
        return new Date();
    }

    public boolean getRisk() {
        return true;
    }
}
