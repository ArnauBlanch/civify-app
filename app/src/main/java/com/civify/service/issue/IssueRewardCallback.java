package com.civify.service.issue;

import com.civify.model.IssueReward;

public interface IssueRewardCallback {
    void onSuccess(IssueReward issueReward);

    void onFailure();
}
