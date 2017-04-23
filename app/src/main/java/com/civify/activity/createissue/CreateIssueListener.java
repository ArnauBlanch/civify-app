package com.civify.activity.createissue;

import com.civify.model.issue.Issue;

import java.io.Serializable;

public interface CreateIssueListener extends Serializable {
    void onIssueCreated(Issue issue);
}
