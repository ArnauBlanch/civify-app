package com.civify.activity.fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.createissue.CreateIssueActivity;
import com.civify.adapter.LocationAdapter;
import com.civify.adapter.UserSimpleCallback;
import com.civify.model.IssueReward;
import com.civify.model.User;
import com.civify.model.map.CivifyMap;
import com.civify.model.map.MapNotLoadedException;
import com.civify.utils.AdapterFactory;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

public class NavigateFragment extends BasicFragment {

    private static final String TAG = NavigateFragment.class.getSimpleName();

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int RESULT_OK = -1;

    private float mLastZoom;
    private Snackbar mSarchCenterSnackbar;

    public NavigateFragment() { }

    public static NavigateFragment newInstance() {
        return new NavigateFragment();
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.NAVIGATE_ID;
    }

    private void setMap() {
        CivifyMap.setContext((DrawerActivity) getActivity());
        Fragment mapFragment = CivifyMap.getInstance().getMapFragment();
        CivifyMap.getInstance().enable();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.map_fragment_placeholder, mapFragment)
                .commit();
    }

    @Override
    // Called after onCreate
    public void onResume() {
        setHasOptionsMenu(true);
        super.onResume();
        if (mSarchCenterSnackbar == null) CivifyMap.getInstance().enable();
    }

    @Override
    public void onPause() {
        CivifyMap.getInstance().disable();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        CivifyMap.getInstance().onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CreateIssueActivity.ISSUE_CREATION) {
            if (resultCode == CreateIssueActivity.ISSUE_CREATED) {
                IssueReward issueReward =
                        (IssueReward) data.getExtras().getSerializable("issueReward");
                try {
                    CivifyMap.getInstance().addIssueMarker(issueReward.getIssue());
                    final DrawerActivity activity = (DrawerActivity) getActivity();
                    AdapterFactory.getInstance().getUserAdapter(getContext())
                            .showRewardDialog(activity, issueReward.getReward(),
                                    new UserSimpleCallback() {
                                        @Override
                                        public void onSuccess(User user) {
                                            activity.setUserHeader();
                                        }

                                        @Override
                                        public void onFailure() { }
                                    });
                    Snackbar.make(getView(), getString(R.string.issue_created),
                            Snackbar.LENGTH_SHORT).show();
                } catch (MapNotLoadedException ignore) {
                    Log.wtf(NavigateFragment.class.getSimpleName(), "Creating issues must be "
                            + "only enabled if the map is loaded");
                }
            }
            CivifyMap.getInstance().setCanBeDisabled(true);

        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            handleSearch(resultCode, data);
        } else CivifyMap.getInstance().onMapSettingsResults(requestCode, resultCode);
    }

    private void handleSearch(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(getActivity(), data);
            Location loc = LocationAdapter.getLocation(place.getLatLng());
            try {
                CivifyMap.getInstance().disableLocation();
                mLastZoom = CivifyMap.getInstance().getCurrentCameraPosition().zoom;
                CivifyMap.getInstance().center(loc, CivifyMap.DEFAULT_ZOOM, true);
                mSarchCenterSnackbar = Snackbar.make(getView(),
                        getString(R.string.search_disable_center), Snackbar.LENGTH_INDEFINITE);
                mSarchCenterSnackbar.show();
            } catch (MapNotLoadedException ignore) {
                Snackbar.make(getView(), getString(R.string.error_ocurred),
                        Snackbar.LENGTH_SHORT).show();
            }
        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Snackbar.make(getView(), getString(R.string.error_ocurred),
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View mapView = inflater.inflate(R.layout.fragment_navigate, container, false);

        setMap();
        setCenterButton(mapView);
        setCreateIssueButton(mapView);

        return mapView;
    }

    private void setCenterButton(View v) {
        FloatingActionButton fabLocation = (FloatingActionButton)
                v.findViewById(R.id.fab_location);
        fabLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Float zoom = null;
                    if (mSarchCenterSnackbar != null) {
                        CivifyMap.getInstance().enable();
                        mSarchCenterSnackbar.dismiss();
                        mSarchCenterSnackbar = null;
                        zoom = mLastZoom;
                    }
                    CivifyMap.getInstance().center(
                            CivifyMap.getInstance().getCurrentLocation(), zoom, true);
                } catch (MapNotLoadedException ignore) {
                    showMapLoadingWarning(view);
                }
            }
        });
    }

    private void setCreateIssueButton(View v) {
        FloatingActionButton fabCreateIssue = (FloatingActionButton)
                v.findViewById(R.id.fab_add);
        fabCreateIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CivifyMap.getInstance().isMapReady()) {
                    CivifyMap.getInstance().setCanBeDisabled(false);
                    Intent intent = new Intent(getActivity().getApplicationContext(),
                            CreateIssueActivity.class);
                    startActivityForResult(intent, CreateIssueActivity.ISSUE_CREATION);
                } else showMapLoadingWarning(view);
            }
        });
    }

    private static void showMapLoadingWarning(View view) {
        Snackbar.make(view, R.string.mapLoading, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_place:
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    Snackbar.make(getView(), R.string.service_error, Snackbar.LENGTH_LONG)
                            .setAction(R.string.action, null).show();
                } catch (GooglePlayServicesNotAvailableException e) {
                    Snackbar.make(getView(), R.string.service_not_available, Snackbar.LENGTH_LONG)
                            .setAction(R.string.action, null).show();
                }
                break;
            default:
                break;
        }
        return false;
    }
}
