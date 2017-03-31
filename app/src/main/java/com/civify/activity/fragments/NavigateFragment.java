package com.civify.activity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.civify.R;
import com.civify.model.map.CivifyMap;
import com.civify.model.map.MapNotReadyException;

public class NavigateFragment extends Fragment {

    private CivifyMap mCivifyMap;

    public NavigateFragment() {
        // Required empty public constructor
    }

    public static NavigateFragment newInstance() {
        return new NavigateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setMap() {
        mCivifyMap = new CivifyMap(getActivity());
        Fragment mapFragment = mCivifyMap.getMapFragment();
        mCivifyMap.enableLocationUpdates();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.map_fragment_placeholder, mapFragment)
                .commit();
    }

    @Override
    // Called after onCreate
    public void onResume() {
        super.onResume();
        mCivifyMap.enableLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCivifyMap.disableLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        mCivifyMap.onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCivifyMap.onMapSettingsResults(requestCode, resultCode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View mapView = inflater.inflate(R.layout.fragment_navigate, container, false);
        FloatingActionButton fabAdd = (FloatingActionButton) mapView.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.not_implemented_yet, Snackbar.LENGTH_LONG)
                        .setAction(R.string.action, null).show();
            }
        });

        setMap();

        FloatingActionButton fabLocation = (FloatingActionButton)
                mapView.findViewById(R.id.fab_location);
        fabLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mCivifyMap.center();
                } catch (MapNotReadyException ignore) {
                    Snackbar.make(view, "Map loading, please wait...", Snackbar.LENGTH_LONG)
                            .setAction(R.string.action, null).show();
                }
            }
        });
        return mapView;
    }
}
