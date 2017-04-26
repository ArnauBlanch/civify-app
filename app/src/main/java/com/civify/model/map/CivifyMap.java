package com.civify.model.map;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.IssueDetailsFragment;
import com.civify.adapter.LocationAdapter;
import com.civify.adapter.UpdateLocationListener;
import com.civify.model.issue.Issue;
import com.civify.service.issue.ListIssuesSimpleCallback;
import com.civify.utils.AdapterFactory;
import com.civify.utils.ConfirmDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;

import java.util.List;

public class CivifyMap implements UpdateLocationListener, OnMapReadyCallback {

    private static final String TAG = CivifyMap.class.getSimpleName();

    private static final int DEFAULT_ZOOM = 18;

    private GoogleMap mGoogleMap;
    private SupportMapFragment mMapFragment;
    private CivifyMarkers mMarkers;
    private boolean mPlayerSet;
    private final LocationAdapter mLocationAdapter;

    public CivifyMap(@NonNull DrawerActivity context) {
        this(new LocationAdapter(context));
    }

    CivifyMap(@NonNull LocationAdapter locationAdapter) {
        mLocationAdapter = locationAdapter;
        setRefreshMillis(LocationAdapter.Priority.HIGH_ACCURACY,
                LocationAdapter.Priority.HIGH_ACCURACY.getPeriodMillis(), 0L);
        setRefreshLocations(true);
    }

    @NonNull
    public SupportMapFragment getMapFragment() {
        if (isMapFragmentSet() && isMapReady()) return mMapFragment;
        SupportMapFragment mapFragment = newMapFragment();
        setMapFragment(mapFragment);
        return mapFragment;
    }

    public boolean isMapReady() {
        return mMarkers != null && mPlayerSet;
    }

    public boolean isMapFragmentSet() {
        return mMapFragment != null;
    }

    private static SupportMapFragment newMapFragment() {
        return SupportMapFragment.newInstance(new GoogleMapOptions()
                .camera(new CameraPosition.Builder()
                        .target(LocationAdapter.ZERO)
                        .zoom(DEFAULT_ZOOM)
                        .build()));
    }

    public void setMapFragment(@NonNull SupportMapFragment mapFragment) {
        mMapFragment = mapFragment;
        mMapFragment.getMapAsync(this);
        Log.d(TAG, "Map fragment created. Requesting Google Map...");
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "Map received. Loading Civify Map...");
        mGoogleMap = googleMap;
        setMapSettings();
        mLocationAdapter.setOnPermissionsRequestedListener(new Runnable() {
            @Override
            public void run() {
                enableLocation();
            }
        });
        mLocationAdapter.setOnUpdateLocationListener(this);
        Log.d(TAG, "Civify Map loaded.");
    }

    public UiSettings getUiSettings() {
        return mGoogleMap.getUiSettings();
    }

    private void setMapSettings() {
        UiSettings settings = getUiSettings();
        settings.setScrollGesturesEnabled(true);
        settings.setRotateGesturesEnabled(true);
        settings.setCompassEnabled(true);
        settings.setZoomControlsEnabled(false);
        settings.setTiltGesturesEnabled(false);
        settings.setMapToolbarEnabled(false);
        settings.setMyLocationButtonEnabled(false);
        setMarkers();
    }

    private void setMarkers() {
        if (mMarkers == null) {
            mMarkers = new CivifyMarkers(this);
            refreshIssues();
        } else mMarkers.setMap(this);
    }

    public void refreshIssues() {
        AdapterFactory.getInstance().getIssueAdapter(getContext())
                .getIssues(new ListIssuesSimpleCallback() {
                    @Override
                    public void onSuccess(List<Issue> issues) {
                        mMarkers.addAll(IssueMarker.getMarkers(issues, CivifyMap.this));
                    }

                    @Override
                    public void onFailure() {
                        ConfirmDialog.show(getContext(), "Error",
                                "Issues cannot be retrieved, please try again later.");
                    }
                });
    }

    public void addIssueMarker(@NonNull Issue issue) throws MapNotReadyException {
        if (isMapReady()) mMarkers.add(new IssueMarker(issue, this));
        else throw new MapNotReadyException();
    }

    public CivifyMarkers getMarkers() {
        return mMarkers;
    }

    public void showIssueDetails(CivifyMarker<?> issueMarker){
        Fragment issueDetailsFragment = IssueDetailsFragment.newInstance(issueMarker);
        getContext().setFragment(issueDetailsFragment, issueDetailsFragment.getId());
    }

    GoogleMap getGoogleMap() {
        return mGoogleMap;
    }

    DrawerActivity getContext() {
        return (DrawerActivity) mLocationAdapter.getContext();
    }

    public void enableLocation() {
        try {
            mGoogleMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            Log.wtf(TAG, "Permissions should be checked before call enableLocationMarker()", e);
        }
    }

    public Location getCurrentLocation() {
        return mLocationAdapter.getLastLocation();
    }

    public void center() throws MapNotReadyException {
        if (isMapReady()) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(
                    LocationAdapter.getLatLng(mLocationAdapter.getLastLocation())));
        } else throw new MapNotReadyException();
    }

    @Override
    public void onUpdateLocation(@NonNull Location location) {
        if (!mPlayerSet) {
            mPlayerSet = true;
            try {
                center();
                addExampleIssues();
            } catch (MapNotReadyException e) {
                Log.wtf(TAG, e);
            }
        }
    }

    private void addExampleIssues() {
        /*
        List<Issue> issues = Issue.getExamples(LocationAdapter.getLatLng(getCurrentLocation()));
        List<IssueMarker> markers = new ArrayList<>(issues.size());
        for (Issue issue : issues) markers.add(new IssueMarker(issue, this));
        mMarkers.addAll(markers);
        */
    }

    public final void setRefreshLocations(boolean refreshLocations) {
        mLocationAdapter.setAutoRefresh(refreshLocations);
    }

    public final void setRefreshMillis(@NonNull LocationAdapter.Priority priority,
                                 long millisPeriod, long millisMinimumPeriod) {
        mLocationAdapter.setUpdateIntervals(priority, millisPeriod, millisMinimumPeriod);
    }

    public void disable() {
        if (!mLocationAdapter.isRequestingPermissions()) mLocationAdapter.disconnect();
    }

    public void enable() {
        mLocationAdapter.connect();
    }

    public void outdateToBeRefreshed() {
        mPlayerSet = false;
        mMapFragment = null;
    }

    public void onRequestPermissionsResult(int requestCode, int[] grantResults) {
        if (requestCode == LocationAdapter.PERMISSION_ACCESS_LOCATION
                // If request is cancelled, the result arrays are empty
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mLocationAdapter.checkForPermissions();
        }
    }

    public void onMapSettingsResults(int requestCode, int resultCode) {
        mLocationAdapter.onMapSettingsResults(requestCode, resultCode);
    }
}
