package com.civify.service;

import com.civify.model.Issue;

import java.util.List;

public interface ListIssuesSimpleCallback {
    void onSuccess(List<Issue> issues);

    void onFailure();
}
