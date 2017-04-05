package com.civify.civify.service;

import com.civify.civify.model.Issue;

public interface IssueSimpleCallback {
    void onSuccess(Issue issue);

    void onFailure();
}
