package com.civify.model.map;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.civify.activity.DrawerActivity;
import com.civify.adapter.LocationAdapter;
import com.civify.adapter.UpdateLocationListener;
import com.civify.adapter.issue.IssueAdapter;
import com.civify.model.issue.Issue;
import com.civify.service.issue.ListIssuesSimpleCallback;
import com.civify.utils.AdapterFactory;
import com.civify.utils.ConfirmDialog;
import com.google.android.gms.maps.CameraUpdate;
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

    private static CivifyMap sInstance;

    private GoogleMap mGoogleMap;
    private SupportMapFragment mMapFragment;
    private CivifyMarkers mMarkers;
    private boolean mPlayerSet;
    private final LocationAdapter mLocationAdapter;
    private final IssueAdapter mIssueAdapter;

    private CivifyMap(@NonNull DrawerActivity context) {
        this(new LocationAdapter(context), AdapterFactory.getInstance().getIssueAdapter(context));
    }

    // Dependency injection for tests
    CivifyMap(@NonNull LocationAdapter locationAdapter, @NonNull IssueAdapter issueAdapter) {
        mLocationAdapter = locationAdapter;
        mIssueAdapter = issueAdapter;
        setRefreshMillis(LocationAdapter.Priority.HIGH_ACCURACY,
                LocationAdapter.Priority.HIGH_ACCURACY.getPeriodMillis(), 0L);
        setRefreshLocations(true);
    }

    public static void setContext(@NonNull DrawerActivity context) {
        if (sInstance == null || sInstance.getContext() != context) {
            if (sInstance != null) {
                sInstance.disable();
            }
            sInstance = new CivifyMap(context);
        }
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

    public boolean isPlayerSet() {
        return mPlayerSet;
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
                enableGoogleMyLocation();
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
        mIssueAdapter.getIssues(new ListIssuesSimpleCallback() {
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

    public GoogleMap getGoogleMap() {
        return mGoogleMap;
    }

    public DrawerActivity getContext() {
        return (DrawerActivity) mLocationAdapter.getContext();
    }

    public void enableGoogleMyLocation() {
        try {
            mGoogleMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            Log.wtf(TAG, "Permissions should be checked before call enableGoogleMyLocation()", e);
        }
    }

    public Location getCurrentLocation() {
        return mLocationAdapter.getLastLocation();
    }

    public Location getCurrentCameraPosition() {
        return LocationAdapter.getLocation(mGoogleMap.getCameraPosition().target);
    }

    public void center() throws MapNotReadyException {
        if (isMapReady()) {
            mGoogleMap.animateCamera(getCameraUpdate(mLocationAdapter.getLastLocation()));
        } else throw new MapNotReadyException();
    }

    static CameraUpdate getCameraUpdate(Location location) {
        return CameraUpdateFactory.newLatLng(LocationAdapter.getLatLng(location));
    }

    @Override
    public void onUpdateLocation(@NonNull Location location) {
        if (!mPlayerSet) {
            mPlayerSet = true;
            try {
                center();
            } catch (MapNotReadyException e) {
                Log.wtf(TAG, e);
            }
        }
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

    public static CivifyMap getInstance() {
        if (sInstance == null) throw new RuntimeException("setContext(Activity) was not called!");
        return sInstance;
    }
}
