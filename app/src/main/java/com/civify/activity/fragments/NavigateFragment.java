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
import com.civify.model.map.MapNotReadyException;
import com.civify.utils.AdapterFactory;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class NavigateFragment extends BasicFragment {

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
        super.onResume();
        CivifyMap.getInstance().enable();
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

        } else CivifyMap.getInstance().onMapSettingsResults(requestCode, resultCode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View mapView = inflater.inflate(R.layout.fragment_navigate, container, false);

        setMap();

        FloatingActionButton fabLocation = (FloatingActionButton)
                mapView.findViewById(R.id.fab_location);
        fabLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    CivifyMap.getInstance().center(true);
                } catch (MapNotReadyException ignore) {
                    showMapLoadingWarning(view);
                }
            }
        });

        FloatingActionButton fabCreateIssue = (FloatingActionButton)
                mapView.findViewById(R.id.fab_add);
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

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Location loc = LocationAdapter.getLocation(place.getLatLng());
                try {
                    CivifyMap.getInstance().center(loc, true);
                } catch (MapNotReadyException ignore) {
                    showMapLoadingWarning(mapView);
                }
                Log.d("place", "Place: " + place.getName());
            }
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.d("place", "An error occurred: " + status);
            }
        });

        return mapView;
    }

    private static void showMapLoadingWarning(View view) {
        Snackbar.make(view, R.string.mapLoading, Snackbar.LENGTH_LONG)
                .setAction(R.string.action, null).show();
    }
}
