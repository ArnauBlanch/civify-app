package com.civify.activity.fragments.profile;

import com.civify.activity.fragments.issue.WallFragment;
import com.civify.adapter.UserAdapter;
import com.civify.model.User;
import com.civify.model.issue.Issue;

import java.util.ArrayList;
import java.util.List;

public class IssuesProfileFragment extends WallFragment {

    @Override
    protected List<Issue> filterIssues(List<Issue> issues) {
        User currentUser = UserAdapter.getCurrentUser();
        List<Issue> filteredList = new ArrayList<>();
        for (int i = 0; i < issues.size(); ++i) {
            Issue currentIssue = issues.get(i);
            String userAuthToken = currentIssue.getUserAuthToken();
            if (currentUser != null) {
                if (currentUser.getUserAuthToken().equals(userAuthToken)) {
                    filteredList.add(currentIssue);
                }
            }
        }
        return filteredList;
    }
}
