package com.civify.model.issue;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;

import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.issue.IssueDetailsFragment;
import com.civify.adapter.LocationAdapter;
import com.civify.model.Picture;
import com.civify.model.map.CivifyMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Date;

public class Issue implements Serializable {

    private static final int BITMAP_COMPRESS_VALUE = 70;
    private static final int KILOMETER = 1000;

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

    @Expose(serialize = false)
    @SerializedName("resolved")
    private boolean mResolved;

    public Issue() { }

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

    public Issue(String title, String description, Category category, boolean risk,
            double longitude, double latitude, Picture pic, String userAuthToken) {
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
        mPicture = pic;
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
        if (mCategory == null) mCategory = Category.OTHER;
        return mCategory;
    }

    public void setCategory(Category category) {
        this.mCategory = category;
    }

    public boolean isRisk() {
        return mRisk;
    }

    public boolean isResolved() {
        return mResolved;
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

    public void setConfirmedByAuthUser(boolean confirmedByAuthUser) {
        mConfirmedByAuthUser = confirmedByAuthUser;
    }

    public boolean getReportedByAuthUser() {
        return mReportedByAuthUser;
    }

    public void setReportedByAuthUser(boolean reportedByAuthUser) {
        mReportedByAuthUser = reportedByAuthUser;
    }

    public boolean getResolvedByAuthUser() {
        return mResolvedByAuthUser;
    }

    public void setResolvedByAuthUser(boolean resolvedByAuthUser) {
        mResolvedByAuthUser = resolvedByAuthUser;
    }

    public LatLng getPosition() {
        return LocationAdapter.getLatLng(mLatitude, mLongitude);
    }

    /** @return distance in meters between the current geolocated position and this marker. */
    @Nullable
    public Float getDistanceFromCurrentLocation() {
        Location currentLocation = CivifyMap.getInstance().getCurrentLocation();
        if (currentLocation != null) {
            return currentLocation.distanceTo(LocationAdapter.getLocation(getPosition()));
        }
        return null;
    }

    @Nullable
    public String getDistanceFromCurrentLocationAsString() {
        Float distance = getDistanceFromCurrentLocation();
        if (distance != null) {
            String distanceAsString = "";
            if (distance > KILOMETER) {
                int km = (int) Math.floor(distance / KILOMETER);
                distanceAsString += km + "km ";
                distance -= km * KILOMETER;
            }
            distanceAsString += Math.round(distance) + "m";
            return distanceAsString.trim();
        }
        return null;
    }

    public void showIssueDetails() {
        Fragment issueDetailsFragment = IssueDetailsFragment.newInstance(this);
        if (issueDetailsFragment != null) {
            CivifyMap.getInstance().getContext()
                    .setFragment(issueDetailsFragment, DrawerActivity.DETAILS_ISSUE_ID);
        }
    }

    @Override
    public String toString() {
        return '{'
                + "mTitle='"
                + mTitle
                + '\''
                + ", mDescription='"
                + mDescription
                + '\''
                + ", mCategory="
                + mCategory
                + ", mRisk="
                + mRisk
                + ", mLongitude="
                + mLongitude
                + ", mLatitude="
                + mLatitude
                + ", mConfirmVotes="
                + mConfirmVotes
                + ", mResolvedVotes="
                + mResolvedVotes
                + ", mReports="
                + mReports
                + ", mCreatedAt="
                + mCreatedAt
                + ", mUpdatedAt="
                + mUpdatedAt
                + ", mIssueAuthToken='"
                + mIssueAuthToken
                + '\''
                + ", mUserAuthToken='"
                + mUserAuthToken
                + '\''
                + '}';
    }
}
