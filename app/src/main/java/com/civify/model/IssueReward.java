package com.civify.model;

import com.civify.model.issue.Issue;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IssueReward implements Serializable {

    @Expose
    @SerializedName("issue")
    private Issue mIssue;

    @Expose(serialize = false)
    @SerializedName("rewards")
    private Reward mReward;

    public IssueReward() { }

    public IssueReward(Issue issue, Reward reward) {
        mIssue = issue;
        mReward = reward;
    }

    public Issue getIssue() {
        return mIssue;
    }

    public Reward getReward() {
        return mReward;
    }
}
