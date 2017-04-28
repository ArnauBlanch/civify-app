package com.civify.activity.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.civify.R;
import com.civify.adapter.issue.IssueAdapter;
import com.civify.model.issue.Issue;
import com.civify.service.issue.ListIssuesSimpleCallback;
import com.civify.utils.AdapterFactory;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WallFragment extends Fragment {

    private IssueAdapter mIssueAdapter;
    private IssuesViewFragment mIssuesViewFragment;
    private ProgressBar mProgressBar;

    public WallFragment() {
        // Required empty public constructor
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences("USERPREFS", Context.MODE_PRIVATE);
        AdapterFactory adapterFactory = AdapterFactory.getInstance();
        mIssueAdapter = adapterFactory.getIssueAdapter(sharedPreferences);
        mIssuesViewFragment = new IssuesViewFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.wall_container, mIssuesViewFragment)
                .commit();
        // CivifyMap.refreshIssues(add ListIssuesSimpleCallback)
        mIssueAdapter.getIssues(new ListIssuesSimpleCallback() {
            @Override
            public void onSuccess(List<Issue> issues) {
                mProgressBar.setVisibility(View.GONE);
                mIssuesViewFragment.setIssuesList(issues);
            }

            @Override
            public void onFailure() {
                // TODO: do something
            }
        });
    }
}
