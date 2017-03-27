package model;

import java.util.Date;

/**
 * Created by MSI on 27/03/2017.
 */

public class Issue {

    private String mTitle;
    private float mLatitude;
    private float mLongitude;
    private String mCaregory;
    private String mDescription;
    private boolean mRisk;
    private int mReports;
    private int mConfirmVotes;
    private int mUserId;
    private Date mCreatedAs;
    private Date mUpdatedAs;
    private int mResolvedVotes;
    private String mPicture;

    public Issue(String title, float latitude, float longitude, String caregory, String description,
            boolean risk, int reports, int confirmVotes, int userId, Date createdAs, Date updatedAs,
            int resolvedVotes, String picture) {
        this.mTitle = title;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mCaregory = caregory;
        this.mDescription = description;
        this.mRisk = risk;
        this.mReports = reports;
        this.mConfirmVotes = confirmVotes;
        this.mUserId = userId;
        this.mCreatedAs = createdAs;
        this.mUpdatedAs = updatedAs;
        this.mResolvedVotes = resolvedVotes;
        this.mPicture = picture;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public float getLatitude() {
        return mLatitude;
    }

    public void setLatitude(float latitude) {
        mLatitude = latitude;
    }

    public float getLongitude() {
        return mLongitude;
    }

    public void setLongitude(float longitude) {
        mLongitude = longitude;
    }

    public String getCaregory() {
        return mCaregory;
    }

    public void setCaregory(String caregory) {
        mCaregory = caregory;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean isRisk() {
        return mRisk;
    }

    public void setRisk(boolean risk) {
        mRisk = risk;
    }

    public int getReports() {
        return mReports;
    }

    public void setReports(int reports) {
        mReports = reports;
    }

    public int getConfirmVotes() {
        return mConfirmVotes;
    }

    public void setConfirmVotes(int confirmVotes) {
        mConfirmVotes = confirmVotes;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public Date getCreatedAs() {
        return mCreatedAs;
    }

    public void setCreatedAs(Date createdAs) {
        mCreatedAs = createdAs;
    }

    public Date getUpdatedAs() {
        return mUpdatedAs;
    }

    public void setUpdatedAs(Date updatedAs) {
        mUpdatedAs = updatedAs;
    }

    public int getResolvedVotes() {
        return mResolvedVotes;
    }

    public void setResolvedVotes(int resolvedVotes) {
        mResolvedVotes = resolvedVotes;
    }

    public String getPicture() {
        return mPicture;
    }

    public void setPicture(String picture) {
        mPicture = picture;
    }
}
