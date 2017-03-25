package com.civify.civify.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Issue {

    @Expose()
    @SerializedName("title")
    private String mTitle;

    @Expose()
    @SerializedName("description")
    private String mDescription;

    @Expose()
    @SerializedName("category")
    private String mCategory;

    @Expose()
    @SerializedName("risk")
    private boolean mRisk;

    @Expose()
    @SerializedName("longitude")
    private float mLongitude;

    @Expose()
    @SerializedName("latitude")
    private float mLatitude;

    @Expose()
    @SerializedName("username")
    private String mUsername;

    @Expose(serialize = false)          // deserialize = true
    @SerializedName("confirm_votes")
    private int mConfirmVotes;

    @Expose(serialize = false)          // deserialize = true
    @SerializedName("resolved_votes")
    private int mResolvedVotes;

    @Expose(serialize = false)          // deserialize = true
    @SerializedName("resolved")
    private boolean mResolved;

    @Expose(serialize = false)          // deserialize = true
    @SerializedName("reports")
    private int mReports;

    @Expose(serialize = false)          // deserialize = true
    @SerializedName("created_at")
    private Date mCreatedAt;

    public Issue(String title, String description, String category, boolean risk, float longitude,
                 float latitude, String username) {
        this.mTitle = title;
        this.mDescription = description;
        this.mCategory = category;
        this.mRisk = risk;
        this.mLongitude = longitude;
        this.mLatitude = latitude;
        this.mUsername = username;
        mConfirmVotes = mResolvedVotes = mReports = 0;
        mResolved = false;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public boolean ismRisk() {
        return mRisk;
    }

    public void setmRisk(boolean mRisk) {
        this.mRisk = mRisk;
    }

    public float getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(float mLongitude) {
        this.mLongitude = mLongitude;
    }

    public float getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(float mLatitude) {
        this.mLatitude = mLatitude;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public int getmConfirmVotes() {
        return mConfirmVotes;
    }

    public void setmConfirmVotes(int mConfirmVotes) {
        this.mConfirmVotes = mConfirmVotes;
    }

    public int getmResolvedVotes() {
        return mResolvedVotes;
    }

    public void setmResolvedVotes(int mResolvedVotes) {
        this.mResolvedVotes = mResolvedVotes;
    }

    public boolean ismResolved() {
        return mResolved;
    }

    public void setmResolved(boolean mResolved) {
        this.mResolved = mResolved;
    }

    public int getmReports() {
        return mReports;
    }

    public void setmReports(int mReports) {
        this.mReports = mReports;
    }

    public Date getmCreatedAt() {
        return mCreatedAt;
    }

    public void setmCreatedAt(Date mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }
}
