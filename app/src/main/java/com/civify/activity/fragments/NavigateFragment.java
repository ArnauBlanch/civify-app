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
import com.civify.activity.createissue.CreateIssueActivity;
import com.civify.activity.createissue.CreateIssueListener;
import com.civify.model.issue.Issue;
import com.civify.model.map.CivifyMap;
import com.civify.model.map.MapNotReadyException;

public class NavigateFragment extends Fragment {

    public static final String LISTENER = "listener";
    private CivifyMap mCivifyMap;
    private CreateIssueListenerImpl mCreateIssueListener;

    public NavigateFragment() {
        // Required empty public constructor
    }

    public static NavigateFragment newInstance() {
        return new NavigateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCreateIssueListener = new CreateIssueListenerImpl();
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

        FloatingActionButton fabCreateIssue = (FloatingActionButton)
                mapView.findViewById(R.id.fab_add);
        fabCreateIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(),
                        CreateIssueActivity.class);
                intent.putExtra(LISTENER, mCreateIssueListener);
                startActivity(intent);
            }
        });

        return mapView;
    }

    private static class CreateIssueListenerImpl implements CreateIssueListener {
        private static final long serialVersionUID = -853835573599268942L;

        @Override
        public void onIssueCreated(Issue issue) {
            // Set issue details fragment using 'issue'
        }
    }
}
