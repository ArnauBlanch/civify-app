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
import com.civify.adapter.IssuesViewAdapter;
import com.civify.model.issue.Issue;

import java.util.List;

public class IssuesViewFragment extends Fragment {

    private List<Issue> mIssuesList;
    private RecyclerView recyclerView;
    private IssuesViewAdapter adapter;

    public IssuesViewFragment() {
        // Required empty public constructor
    }

    private void setIssuesList(List<Issue> issuesList) {
        mIssuesList = issuesList;
    }

    public static IssuesViewFragment newInstance(List<Issue> issuesList) {
        IssuesViewFragment fragment = new IssuesViewFragment();
        fragment.setIssuesList(issuesList);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.issues_view, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new IssuesViewAdapter(this.getContext(), mIssuesList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
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
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % mSpanCount; // item column

            if (mIncludeEdge) {
                outRect.left = mSpacing
                        - column * mSpacing
                        / mSpanCount; // mSpacing - column * ((1f / mSpanCount) * mSpacing)
                outRect.right = (column + 1) * mSpacing
                        / mSpanCount; // (column + 1) * ((1f / mSpanCount) * mSpacing)

                if (position < mSpanCount) { // top edge
                    outRect.top = mSpacing;
                }
                outRect.bottom = mSpacing; // item bottom
            } else {
                outRect.left =
                        column * mSpacing / mSpanCount; // column * ((1f / mSpanCount) * mSpacing)
                outRect.right = mSpacing
                        - (column + 1) * mSpacing
                        / mSpanCount; // mSpacing - (column + 1) * ((1f /    mSpanCount) * mSpacing)
                if (position >= mSpanCount) {
                    outRect.top = mSpacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
