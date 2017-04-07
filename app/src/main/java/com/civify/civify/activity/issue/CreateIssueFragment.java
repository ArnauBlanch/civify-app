package com.civify.civify.activity.issue;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.civify.civify.R;

public class CreateIssueFragment extends Fragment {
    private static final int ISSUE_TITLE = 0;
    private static final int ISSUE_CATEGORY = 1;
    private static final int ISSUE_PHOTO = 2;
    private static final int ISSUE_RISK = 3;

    private View mView;
    private int mPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mPage = getArguments().getInt("page");
        mView = getActivity().getLayoutInflater()
                .inflate(getLayoutResource(mPage), container, false);
        mView.setTag(mPage);
        return mView;
    }

    private static int getLayoutResource(int pageId) {
        switch (pageId) {
            case ISSUE_TITLE:
                return R.layout.create_issue_title;
            case ISSUE_CATEGORY:
                return R.layout.create_issue_category;
            case ISSUE_PHOTO:
                return R.layout.create_issue_photo;
            case ISSUE_RISK:
                return R.layout.create_issue_risk;
            default:
                return R.layout.create_issue_description;
        }
    }
}
