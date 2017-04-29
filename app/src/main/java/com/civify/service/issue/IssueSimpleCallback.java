package com.civify.service.issue;

import com.civify.model.issue.Issue;

public interface IssueSimpleCallback {
    void onSuccess(Issue issue);

    void onFailure();
}
