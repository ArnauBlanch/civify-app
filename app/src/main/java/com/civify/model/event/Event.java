package com.civify.model.event;

import android.content.Context;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.event.EventDetailsFragment;
import com.civify.model.Picture;
import com.civify.model.achievement.Achievement;
import com.civify.model.map.CivifyMap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;

public class Event extends Achievement {

    private static final int MILI_TO_DAYS = 86400000;
    private static final int MILI_TO_HOURS = 3600000;
    private static final int DAY_TO_HOURS = 24;
    private static final char SPACE = ' ';

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

    public String getDuration(Context context) {
        Long time = mEndDate.getTime() - (isHappening() ? Calendar.getInstance().getTime().getTime()
                : mStartDate.getTime());
        Long days = time / MILI_TO_DAYS;
        Long hours = (time / MILI_TO_HOURS) - (days * DAY_TO_HOURS);
        String duration;
        String daydays;
        if (days > 0) {
            if (days > 1) {
                daydays = context.getResources().getString(R.string
                        .days);
            } else {
                daydays = context.getResources().getString(R.string
                        .day);
            }
            if (hours > 0) {
                duration = days.toString() + SPACE + daydays + SPACE + hours.toString()
                        + SPACE + context.getResources()
                        .getString(R.string.first_letter_hour);
            } else {
                duration = days.toString() + SPACE + daydays;
            }
        } else {
            duration = hours.toString()
                    + SPACE
                    + context.getResources().getString(R.string.first_letter_hour);
        }
        return duration;
    }

    public boolean isHappening() {
        boolean happen = false;
        Long currentTime = Calendar.getInstance().getTime().getTime();
        if (currentTime > mStartDate.getTime() && currentTime < mEndDate.getTime()) happen = true;
        return happen;
    }

    public boolean isPast() {
        boolean past = false;
        Long currentTime = Calendar.getInstance().getTime().getTime();
        if (currentTime > mEndDate.getTime()) past = true;
        return past;
    }

    public void showEventDetails() {
        CivifyMap.getInstance().getContext()
                .setFragment(EventDetailsFragment.newInstance(this), DrawerActivity
                        .DETAILS_EVENTS_ID);
    }
}
