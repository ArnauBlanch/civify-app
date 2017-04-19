package com.civify.model.issue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Issue {
    @Expose
    @SerializedName("title")
    private String mTitle;

    @Expose
    @SerializedName("description")
    private String mDescription;

    @Expose
    @SerializedName("category")
    private Category mCategory;

    @Expose
    @SerializedName("risk")
    private boolean mRisk;

    @Expose
    @SerializedName("longitude")
    private float mLongitude;

    @Expose
    @SerializedName("latitude")
    private float mLatitude;

    @Expose
    @SerializedName("confirm_votes")
    private int mConfirmVotes;

    @Expose
    @SerializedName("resolved_votes")
    private int mResolvedVotes;

    @Expose
    @SerializedName("resolved")
    private boolean mResolved;

    @Expose
    @SerializedName("reports")
    private int mReports;

    @Expose(serialize = false)
    @SerializedName("created_at")
    private Date mCreatedAt;

    @Expose(serialize = false)
    @SerializedName("updated_at")
    private Date mUpdatedAt;

    @Expose(serialize = false)
    @SerializedName("issue_auth_token")
    private String mIssueAuthToken;

    @Expose
    @SerializedName("user_auth_token")
    private String mUserAuthToken;

    @Expose
    @SerializedName("picture")
    private Picture mPicture;

    public Issue() {

    }

    public Issue(String title, String description, Category category, boolean risk, float longitude,
            float latitude, Picture picture, String userAuthToken) {
        mTitle = title;
        mDescription = description;
        mCategory = category;
        mRisk = risk;
        mLongitude = longitude;
        mLatitude = latitude;
        mPicture = picture;
        mResolved = false;
        mResolvedVotes = 0;
        mConfirmVotes = 0;
        mReports = 0;
        mUserAuthToken = userAuthToken;
    }

    public Issue(String title, String description, Category category, boolean risk, float longitude,
            float latitude, int confirmVotes, int resolvedVotes, boolean resolved, int reports,
            Date createdAt, Date updatedAt, String issueAuthToken, String userAuthToken,
            Picture picture) {
        mTitle = title;
        mDescription = description;
        mCategory = category;
        mRisk = risk;
        mLongitude = longitude;
        mLatitude = latitude;
        mConfirmVotes = confirmVotes;
        mResolvedVotes = resolvedVotes;
        mResolved = resolved;
        mReports = reports;
        mCreatedAt = createdAt;
        mUpdatedAt = updatedAt;
        mIssueAuthToken = issueAuthToken;
        mUserAuthToken = userAuthToken;
        mPicture = picture;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public Category getCategory() {
        return mCategory;
    }

    public void setCategory(Category category) {
        this.mCategory = category;
    }

    public boolean isRisk() {
        return mRisk;
    }

    public void setRisk(boolean risk) {
        this.mRisk = risk;
    }

    public float getLongitude() {
        return mLongitude;
    }

    public void setLongitude(float longitude) {
        this.mLongitude = longitude;
    }

    public float getLatitude() {
        return mLatitude;
    }

    public void setLatitude(float latitude) {
        this.mLatitude = latitude;
    }

    public int getConfirmVotes() {
        return mConfirmVotes;
    }

    public void setConfirmVotes(int confirmVotes) {
        this.mConfirmVotes = confirmVotes;
    }

    public int getResolvedVotes() {
        return mResolvedVotes;
    }

    public void setResolvedVotes(int resolvedVotes) {
        this.mResolvedVotes = resolvedVotes;
    }

    public boolean isResolved() {
        return mResolved;
    }

    public void setResolved(boolean resolved) {
        this.mResolved = resolved;
    }

    public int getReports() {
        return mReports;
    }

    public void setReports(int reports) {
        this.mReports = reports;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.mCreatedAt = createdAt;
    }

    public Date getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public String getIssueAuthToken() {
        return mIssueAuthToken;
    }

    public void setIssueAuthToken(String issueAuthToken) {
        mIssueAuthToken = issueAuthToken;
    }

    public String getUserAuthToken() {
        return mUserAuthToken;
    }

    public void setUserAuthToken(String userAuthToken) {
        mUserAuthToken = userAuthToken;
    }

    public Picture getPicture() {
        return mPicture;
    }

    public void setPicture(Picture picture) {
        mPicture = picture;
    }
}
