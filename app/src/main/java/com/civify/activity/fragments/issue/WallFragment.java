package com.civify.activity.fragments.issue;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;
import com.civify.adapter.issue.IssueAdapter;
import com.civify.model.issue.Issue;
import com.civify.model.map.CivifyMap;
import com.civify.model.map.MapNotLoadedException;
import com.civify.service.issue.ListIssuesSimpleCallback;
import com.civify.utils.AdapterFactory;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WallFragment extends BasicFragment {

    private IssueAdapter mIssueAdapter;
    private IssuesViewFragment mIssuesViewFragment;
    private ProgressBar mProgressBar;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
            }

            @Override
            public void onFailure() {
                mProgressBar.setVisibility(View.GONE);
                try {
                    mIssuesViewFragment.setIssuesList(CivifyMap.getInstance().getIssues());
                } catch (MapNotLoadedException e) {
                    Snackbar.make(view, "Couldn't retrieve updated issues.", Snackbar.LENGTH_LONG)
                            .setAction(R.string.action, null).show();
                }
            }
        });
    }

    protected List<Issue> filterIssues(List<Issue> issues) {
        return issues;
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
                }

                @Override
                public void onFailure() {

                }
            });
        }
    }
}
