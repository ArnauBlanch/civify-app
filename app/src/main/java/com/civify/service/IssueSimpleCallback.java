package com.civify.service;

import com.civify.model.Issue;

public interface IssueSimpleCallback {
    void onSuccess(Issue issue);

    void onFailure();
}
