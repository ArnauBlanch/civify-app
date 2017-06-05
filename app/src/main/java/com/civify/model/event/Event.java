package com.civify.model.event;

import com.civify.model.Picture;
import com.civify.model.achievement.Achievement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Event extends Achievement {

    @Expose
    @SerializedName("start_date")
    private Date mStartDate;

    @Expose
    @SerializedName("end_date")
    private Date mEndDate;

    @Expose
    @SerializedName("updated_at")
    private Date mUpdatedAt;

    @Expose
    @SerializedName("event_token")
    private String mToken;

    @Expose
    @SerializedName("picture")
    private Picture mPicture;

    public Event() {
    }

    public Event(String title, String description, int number, String kind, int coins, int xp,
            Date createdAt, String token, boolean enabled, int progress, boolean completed,
            boolean claimed, Picture badge, Date startDate, Date endDate, Date updatedAt,
            String token1, Picture picture) {
        super(title, description, number, kind, coins, xp, createdAt, token, enabled, progress,
                completed, claimed, badge);
        mStartDate = startDate;
        mEndDate = endDate;
        mUpdatedAt = updatedAt;
        mToken = token1;
        mPicture = picture;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
    }

    public Date getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        mUpdatedAt = updatedAt;
    }

    @Override
    public String getToken() {
        return mToken;
    }

    @Override
    public void setToken(String token) {
        mToken = token;
    }

    public Picture getPicture() {
        return mPicture;
    }

    public void setPicture(Picture picture) {
        mPicture = picture;
    }
}
