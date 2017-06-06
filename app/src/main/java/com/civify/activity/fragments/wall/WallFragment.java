package com.civify.activity.fragments.wall;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;
import com.civify.activity.fragments.issue.IssuesViewFragment;
import com.civify.adapter.issue.IssueAdapter;
import com.civify.model.issue.Issue;
import com.civify.model.map.CivifyMap;
import com.civify.model.map.MapNotLoadedException;
import com.civify.service.issue.ListIssuesSimpleCallback;
import com.civify.utils.AdapterFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WallFragment extends BasicFragment {

    public static final int ASCENDING = 0;
    public static final int DESCENDING = 1;
    public static final int PROXIMITY = 2;
    public static final int NUM_CONFIRM = 3;
    public static final int REQUEST_DIALOG = 9;
    public static final String STATUS = "status";

    private static final String TAG = "WallFragment";
    private IssueAdapter mIssueAdapter;
    private IssuesViewFragment mIssuesViewFragment;
    private ProgressBar mProgressBar;
    private int mSortSelected;
    private boolean mLoaded;
    private List<Issue> mIssues;
    private ArrayList<String> mFilteredCategories;
    private int mStatusSelected;


    public WallFragment() {
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.WALL_ID;
    }

    public static WallFragment newInstance() {
        return new WallFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mSortSelected = ASCENDING;
        mStatusSelected = IssueAdapter.UNRESOLVED;
        initCategories();
        mLoaded = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wall, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.loading_wall);
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        AdapterFactory adapterFactory = AdapterFactory.getInstance();
        mIssueAdapter = adapterFactory.getIssueAdapter(getContext());
        mIssuesViewFragment = new IssuesViewFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.wall_container, mIssuesViewFragment)
                .commit();
        mIssueAdapter.getIssues(new ListIssuesSimpleCallback() {
            @Override
            public void onSuccess(List<Issue> issues) {
                mProgressBar.setVisibility(View.GONE);
                mIssuesViewFragment.setIssuesList(filterIssues(issues));
                try {
                    CivifyMap.getInstance().setIssues(issues);
                } catch (MapNotLoadedException ignore) {
                    // Don't refresh map
                }
                mIssues = issues;
                mLoaded = true;
            }

            @Override
            public void onFailure() {
                mProgressBar.setVisibility(View.GONE);
                try {
                    mIssuesViewFragment.setIssuesList(CivifyMap.getInstance().getIssues());
                } catch (MapNotLoadedException e) {
                    Snackbar.make(view, "Couldn't retrieve updated issues.", Snackbar.LENGTH_LONG)
                            .setAction(R.string.action, null)
                            .show();
                }
                mLoaded = false;
            }
        }, IssueAdapter.UNRESOLVED);
    }

    protected List<Issue> filterIssues(List<Issue> issues) {
        return issues;
    }

    public void initCategories() {
        mFilteredCategories = new ArrayList<>();
        mFilteredCategories.add("road_signs");
        mFilteredCategories.add("illumination");
        mFilteredCategories.add("grove");
        mFilteredCategories.add("street_furniture");
        mFilteredCategories.add("trash_and_cleaning");
        mFilteredCategories.add("public_transport");
        mFilteredCategories.add("suggestion");
        mFilteredCategories.add("other");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mLoaded) {
            switch (item.getItemId()) {
                case R.id.action_filter_issues:
                    FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance(mStatusSelected, mFilteredCategories);
                    filterDialogFragment.setTargetFragment(this, REQUEST_DIALOG);
                    filterDialogFragment.show(getActivity());
                    return false;
                case R.id.action_sort_issues:
                    SortDialogFragment sortDialogFragment = SortDialogFragment.newInstance(mSortSelected, this);
                    sortDialogFragment.show(getActivity());
                    return false;
                default:
                    break;
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DIALOG) {
            mStatusSelected = data.getIntExtra(FilterDialogFragment.STATUS, 0);
            mFilteredCategories = data.getStringArrayListExtra(FilterDialogFragment.CATEGORIES);
            // TODO: refresh issues with new filters
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mIssueAdapter.getIssues(new ListIssuesSimpleCallback() {
                @Override
                public void onSuccess(List<Issue> issues) {
                    mIssuesViewFragment.setIssuesList(filterIssues(issues));
                    try {
                        CivifyMap.getInstance().setIssues(issues);
                    } catch (MapNotLoadedException ignore) {
                        // Don't refresh map
                    }
                    mLoaded = true;
                    mIssues = issues;
                }

                @Override
                public void onFailure() {

                }
            }, IssueAdapter.UNRESOLVED);
        }
    }

    public void dialogSelectedSort(int selectedSort) {
        switch (selectedSort) {
            case ASCENDING:
                sortByAscending();
                mSortSelected = ASCENDING;
                Toast.makeText(getContext(), "Ascending", Toast.LENGTH_LONG).show();
                break;
            case DESCENDING:
                sortByDescending();
                mSortSelected = DESCENDING;
                Toast.makeText(getContext(), "Descending", Toast.LENGTH_LONG).show();
                break;
            case PROXIMITY:
                sortByProximity();
                mSortSelected = PROXIMITY;
                Toast.makeText(getContext(), "proximity", Toast.LENGTH_LONG).show();
                break;
            case NUM_CONFIRM:
                sortByConfirms();
                mSortSelected = NUM_CONFIRM;
                Toast.makeText(getContext(), "num_confirm", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    private void sortByAscending() {
        if (mLoaded && mIssues != null) {
            Collections.sort(mIssues, new Comparator<Issue>() {
                @Override
                public int compare(Issue firstIssue, Issue secondIssue) {
                    return firstIssue.getCreatedAt().compareTo(secondIssue.getCreatedAt());
                }
            });
        }
        mIssuesViewFragment.setIssuesList(mIssues);
    }

    private void sortByDescending() {
        if (mLoaded && mIssues != null) {
            Collections.sort(mIssues, new Comparator<Issue>() {
                @Override
                public int compare(Issue firstIssue, Issue secondIssue) {
                    return secondIssue.getCreatedAt().compareTo(firstIssue.getCreatedAt());
                }
            });
        }
        mIssuesViewFragment.setIssuesList(mIssues);
    }

    private void sortByProximity() {
        if (mLoaded && mIssues != null) {
            Collections.sort(mIssues, new Comparator<Issue>() {
                @Override
                public int compare(Issue firstIssue, Issue secondIssue) {
                    if (firstIssue != null && secondIssue != null) {
                        int first = Math.round(firstIssue.getDistanceFromCurrentLocation());
                        int second = Math.round(secondIssue.getDistanceFromCurrentLocation());
                        return second - first;
                    }
                    return 0;
                }
            });
        }
        mIssuesViewFragment.setIssuesList(mIssues);
    }

    private void sortByConfirms() {
        if (mLoaded && mIssues != null) {
            Collections.sort(mIssues, new Comparator<Issue>() {
                @Override
                public int compare(Issue firstIssue, Issue secondIssue) {
                    return firstIssue.getConfirmVotes() - secondIssue.getConfirmVotes();
                }
            });
        }
        mIssuesViewFragment.setIssuesList(mIssues);
    }
}
