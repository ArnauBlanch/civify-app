package com.civify.activity.fragments.profile;

import android.os.Bundle;

import com.civify.activity.fragments.wall.WallFragment;
import com.civify.adapter.UserAdapter;
import com.civify.model.User;
import com.civify.model.issue.Issue;

import java.util.ArrayList;
import java.util.List;

public class IssuesProfileFragment extends WallFragment {

    private User mUser;

    public IssuesProfileFragment() { }

    public static IssuesProfileFragment newInstance(User user) {
        IssuesProfileFragment fragment = new IssuesProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(ProfileInfoFragment.TAG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUser();
    }

    @Override
    protected List<Issue> filterIssues(List<Issue> issues) {
        List<Issue> filteredList = new ArrayList<>();
        for (int i = 0; i < issues.size(); ++i) {
            Issue currentIssue = issues.get(i);
            String userAuthToken = currentIssue.getUserAuthToken();
            if (userAuthToken.equals(mUser.getUserAuthToken())) {
                filteredList.add(currentIssue);
            }
        }
        return filteredList;
    }

    private void setUser() {
        mUser = getArguments() == null ? UserAdapter.getCurrentUser()
                : (User) getArguments().getSerializable(ProfileInfoFragment.TAG_USER);
    }
}
