package com.civify.civify.service;

import com.civify.civify.model.Issue;

public interface IssueSimpleCallback {
    public void onSuccess(Issue issue);

    public void onFailure();
}
