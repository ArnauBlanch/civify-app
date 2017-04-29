package com.civify.service.issue;

import com.civify.model.issue.Issue;

import java.util.List;

public interface ListIssuesSimpleCallback {
    void onSuccess(List<Issue> issues);

    void onFailure();
}
