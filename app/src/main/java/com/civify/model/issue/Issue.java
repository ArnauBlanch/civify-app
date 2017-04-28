package com.civify.model.issue;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Base64;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Date;

public class Issue implements Serializable {

    private static final int BITMAP_COMPRESS_VALUE = 70;

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
    private double mLongitude;

    @Expose
    @SerializedName("latitude")
    private double mLatitude;

    @Expose(serialize = false)
    @SerializedName("confirm_votes")
    private int mConfirmVotes;

    @Expose(serialize = false)
    @SerializedName("resolved_votes")
    private int mResolvedVotes;

    @Expose(serialize = false)
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

    @Expose(serialize = false)
    @SerializedName("user_auth_token")
    private String mUserAuthToken;

    @Expose(serialize = false)
    @SerializedName("confirmed_by_auth_user")
    private boolean mConfirmedByAuthUser;

    @Expose(serialize = false)
    @SerializedName("reported_by_auth_user")
    private boolean mReportedByAuthUser;

    @Expose(serialize = false)
    @SerializedName("resolved_by_auth_user")
    private boolean mResolvedByAuthUser;

    @Expose
    @SerializedName("picture")
    private Picture mPicture;

    public Issue() {

    }

    public Issue(String title, String description, Category category, boolean risk,
            double longitude, double latitude, Bitmap pictureBitmap, String userAuthToken) {
        mTitle = title;
        mDescription = description;
        mCategory = category;
        mRisk = risk;
        mLongitude = longitude;
        mLatitude = latitude;
        mResolvedVotes = 0;
        mConfirmVotes = 0;
        mReports = 0;
        mUserAuthToken = userAuthToken;
        Log.v("Issue creadora", pictureBitmap.toString());
        setPicture(pictureBitmap);
    }

    public Issue(String title, String description, Category category, boolean risk,
            double longitude, double latitude, int confirmVotes, int resolvedVotes, int reports,
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

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(float longitude) {
        this.mLongitude = longitude;
    }

    public double getLatitude() {
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

    public void setPicture(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, BITMAP_COMPRESS_VALUE, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        mPicture = new Picture("issue-picture", "image/jpg",
                Base64.encodeToString(byteArray, Base64.DEFAULT));
    }

    public boolean getConfirmedByAuthUser() {
        return mConfirmedByAuthUser;
    }

    public boolean getReportedByAuthUser() {
        return mReportedByAuthUser;
    }

    public boolean getResolvedByAuthUser() {
        return mResolvedByAuthUser;
    }

}
