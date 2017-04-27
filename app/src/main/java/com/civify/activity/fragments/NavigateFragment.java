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
import com.civify.activity.DrawerActivity;
import com.civify.activity.createissue.CreateIssueActivity;
import com.civify.model.issue.Issue;
import com.civify.model.map.CivifyMap;
import com.civify.model.map.MapNotReadyException;

public class NavigateFragment extends Fragment {

    private CivifyMap mCivifyMap;
    private boolean mCreatingIssue;

    public NavigateFragment() {
        // Required empty public constructor
    }

    public static NavigateFragment newInstance() {
        return new NavigateFragment();
    }

    private void setMap() {
        mCivifyMap = new CivifyMap((DrawerActivity) getActivity());
        Fragment mapFragment = mCivifyMap.getMapFragment();
        mCivifyMap.enable();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.map_fragment_placeholder, mapFragment)
                .commit();
    }

    @Override
    // Called after onCreate
    public void onResume() {
        super.onResume();
        mCivifyMap.enable();
    }

    @Override
    public void onPause() {
        mCivifyMap.disable();
        if (!mCreatingIssue) mCivifyMap.outdateToBeRefreshed();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        mCivifyMap.onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CreateIssueActivity.ISSUE_CREATION) {
            if (resultCode == CreateIssueActivity.ISSUE_CREATED) {
                Issue issue = (Issue) data.getExtras().getSerializable("issue");
                try {
                    mCivifyMap.addIssueMarker(issue);
                } catch (MapNotReadyException ignore) {
                    // Button to create issues is only enabled if the map is ready
                }
                Snackbar.make(getView(), getString(R.string.issue_created),
                        Snackbar.LENGTH_SHORT).show();
                mCreatingIssue = false;
            }
        } else {
            mCivifyMap.onMapSettingsResults(requestCode, resultCode);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View mapView = inflater.inflate(R.layout.fragment_navigate, container, false);

        setMap();

        FloatingActionButton fabLocation = (FloatingActionButton)
                mapView.findViewById(R.id.fab_location);
        fabLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mCivifyMap.center();
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
                if (mCivifyMap.isMapReady()) {
                    mCreatingIssue = true;
                    Intent intent = new Intent(getActivity().getApplicationContext(),
                            CreateIssueActivity.class);
                    startActivityForResult(intent, CreateIssueActivity.ISSUE_CREATION);
                } else showMapLoadingWarning(view);
            }
        });

        return mapView;
    }

    private static void showMapLoadingWarning(View view) {
        Snackbar.make(view, "Map loading, please wait...", Snackbar.LENGTH_LONG)
                .setAction(R.string.action, null).show();
    }
}
