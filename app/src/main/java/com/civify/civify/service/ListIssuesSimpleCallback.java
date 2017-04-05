package com.civify.civify.service;

import com.civify.civify.model.Issue;

import java.util.List;

public interface ListIssuesSimpleCallback {
    void onSuccess(List<Issue> issues);

    void onFailure();
}
