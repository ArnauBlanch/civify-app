package com.civify.activity.fragments;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.civify.R;
import com.civify.adapter.issue.IssuesViewAdapter;
import com.civify.model.issue.Issue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IssuesViewFragment extends Fragment {

    private List<Issue> mIssuesList;
    private RecyclerView mRecyclerView;
    private IssuesViewAdapter mIssuesViewAdapter;

    public IssuesViewFragment() {
        // Required empty public constructor
        mIssuesList = new ArrayList<>();
    }

    public void setIssuesList(List<Issue> issuesList) {
        mIssuesList.clear();
        List<Issue> intermediateList = new ArrayList<>(issuesList);
        Collections.reverse(intermediateList);
        mIssuesList.addAll(intermediateList);
        mIssuesViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_issues_view, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mIssuesViewAdapter = new IssuesViewAdapter(getContext(), mIssuesList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mIssuesViewAdapter);

        return view;
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int mSpanCount;
        private int mSpacing;
        private boolean mIncludeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.mSpanCount = spanCount;
            this.mSpacing = spacing;
            this.mIncludeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % mSpanCount;

            if (mIncludeEdge) {
                outRect.left = mSpacing
                        - column * mSpacing
                        / mSpanCount;
                outRect.right = (column + 1) * mSpacing
                        / mSpanCount;

                if (position < mSpanCount) {
                    outRect.top = mSpacing;
                }
                outRect.bottom = mSpacing;
            } else {
                outRect.left =
                        column * mSpacing / mSpanCount;
                outRect.right = mSpacing
                        - (column + 1) * mSpacing
                        / mSpanCount;
                if (position >= mSpanCount) {
                    outRect.top = mSpacing;
                }
            }
        }
    }


}
