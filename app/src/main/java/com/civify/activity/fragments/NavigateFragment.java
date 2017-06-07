package com.civify.activity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.createissue.CreateIssueActivity;
import com.civify.activity.fragments.wall.FilterDialogFragment;
import com.civify.adapter.UserSimpleCallback;
import com.civify.adapter.issue.IssueAdapter;
import com.civify.model.IssueReward;
import com.civify.model.User;
import com.civify.model.issue.Issue;
import com.civify.model.map.CivifyMap;
import com.civify.model.map.MapNotLoadedException;
import com.civify.model.map.MapNotReadyException;
import com.civify.service.issue.ListIssuesSimpleCallback;
import com.civify.utils.AdapterFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NavigateFragment extends BasicFragment {

    public static final int REQUEST_DIALOG = 9;
    private int mStatusSelected;
    private int mRiskSelected;
    private ArrayList<String> mCategoriesSelected;
    private IssueAdapter mIssueAdapter;

    public NavigateFragment() {
    }

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
        getChildFragmentManager().beginTransaction()
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
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
                    AdapterFactory.getInstance()
                            .getUserAdapter(getContext())
                            .showRewardDialog(activity, issueReward.getReward(),
                                    new UserSimpleCallback() {
                                        @Override
                                        public void onSuccess(User user) {
                                            activity.setUserHeader();
                                        }

                                        @Override
                                        public void onFailure() {
                                        }
                                    });

                    Snackbar.make(getView(), getString(R.string.issue_created),
                            Snackbar.LENGTH_SHORT).show();
                } catch (MapNotLoadedException ignore) {
                    Log.wtf(NavigateFragment.class.getSimpleName(),
                            "Creating issues must be " + "only enabled if the map is loaded");
                }
            }
            CivifyMap.getInstance().setCanBeDisabled(true);
        } else if (requestCode == REQUEST_DIALOG) {
            applyFilters(data);
        } else {
            CivifyMap.getInstance().onMapSettingsResults(requestCode, resultCode);
        }
    }

    private void applyFilters(Intent data) {
        final int oldStatusSelected = mStatusSelected;
        final int oldRiskSelected = mRiskSelected;
        final ArrayList<String> oldFilteredCategories = new ArrayList<>(mCategoriesSelected);
        mStatusSelected = data.getIntExtra(FilterDialogFragment.STATUS, 0);
        mCategoriesSelected = data.getStringArrayListExtra(FilterDialogFragment.CATEGORIES);
        final int defaultRisk = 3;
        mRiskSelected = data.getIntExtra(FilterDialogFragment.RISK, defaultRisk);
        Set<String> oldSet = new HashSet<>(oldFilteredCategories);
        Set<String> newSet = new HashSet<>(mCategoriesSelected);
        if (oldStatusSelected != mStatusSelected
                || !oldSet.equals(newSet)
                || oldRiskSelected != mRiskSelected) {
            refreshIssues();
        }
    }

    private void refreshIssues() {
        ArrayList<String> categories;
        if (!mCategoriesSelected.isEmpty()) {
            categories = mCategoriesSelected;
        } else {
            categories = null;
        }
        mIssueAdapter.getIssues(new ListIssuesSimpleCallback() {
            @Override
            public void onSuccess(List<Issue> issues) {
                try {
                    CivifyMap.getInstance().setIssues(issues);
                } catch (MapNotLoadedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {
                // TODO: error
            }
        }, mStatusSelected, categories, mRiskSelected);
    }

    private void setupFilterItems() {
        mCategoriesSelected = new ArrayList<>();
        mCategoriesSelected.add("road_signs");
        mCategoriesSelected.add("illumination");
        mCategoriesSelected.add("grove");
        mCategoriesSelected.add("street_furniture");
        mCategoriesSelected.add("trash_and_cleaning");
        mCategoriesSelected.add("public_transport");
        mCategoriesSelected.add("suggestion");
        mCategoriesSelected.add("other");
        mRiskSelected = IssueAdapter.RISK_ALL;
        mStatusSelected = IssueAdapter.UNRESOLVED;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View mapView = inflater.inflate(R.layout.fragment_navigate, container, false);
        mIssueAdapter = AdapterFactory.getInstance().getIssueAdapter(getContext());
        setMap();

        FloatingActionButton fabLocation =
                (FloatingActionButton) mapView.findViewById(R.id.fab_location);
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

        FloatingActionButton fabCreateIssue =
                (FloatingActionButton) mapView.findViewById(R.id.fab_add);
        fabCreateIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CivifyMap.getInstance().isMapReady()) {
                    CivifyMap.getInstance().setCanBeDisabled(false);
                    Intent intent = new Intent(getActivity().getApplicationContext(),
                            CreateIssueActivity.class);
                    startActivityForResult(intent, CreateIssueActivity.ISSUE_CREATION);
                } else {
                    showMapLoadingWarning(view);
                }
            }
        });

        setupFilterFloating(mapView);
        setupFilterItems();

        return mapView;
    }

    private void setupFilterFloating(View view) {
        FloatingActionButton fabFilterIssues =
                (FloatingActionButton) view.findViewById(R.id.fab_filter);
        fabFilterIssues.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterDialogFragment filterDialogFragment =
                        FilterDialogFragment.newInstance(mStatusSelected, mCategoriesSelected,
                                mRiskSelected);
                filterDialogFragment.setTargetFragment(NavigateFragment.this, REQUEST_DIALOG);
                filterDialogFragment.show(getActivity());
            }
        });
    }

    private static void showMapLoadingWarning(View view) {
        Snackbar.make(view, R.string.mapLoading, Snackbar.LENGTH_LONG).show();
    }
}
