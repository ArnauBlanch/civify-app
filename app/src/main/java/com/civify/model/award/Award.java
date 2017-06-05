package com.civify.model.award;

import android.support.v4.app.Fragment;

import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.award.AwardDetailsFragment;
import com.civify.model.Picture;
import com.civify.model.map.CivifyMap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Award implements Serializable {

    @Expose
    @SerializedName("title")
    private String mTitle;

    @Expose
    @SerializedName("description")
    private String mDescription;

    @Expose
    @SerializedName("price")
    private int mPrice;

    @Expose
    @SerializedName("created_at")
    private Date mCreatedAt;

    @Expose
    @SerializedName("updated_at")
    private Date mUpdatedAt;

    @Expose
    @SerializedName("award_auth_token")
    private String mAwardAuthToken;

    @Expose
    @SerializedName("offered_by")
    private String mCommerceOffering;

    @Expose
    @SerializedName("picture")
    private Picture mPicture;

    public Award() { }

    public Award(String title, String description, int price, Date createdAt, Date updatedAt,
            String awardAuthToken, String commerceOffering, Picture picture) {
        this.mTitle = title;
        this.mDescription = description;
        this.mPrice = price;
        this.mCreatedAt = createdAt;
        this.mUpdatedAt = updatedAt;
        this.mAwardAuthToken = awardAuthToken;
        this.mCommerceOffering = commerceOffering;
        this.mPicture = picture;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getPrice() {
        return mPrice;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public Date getUpdatedAt() {
        return mUpdatedAt;
    }

    public String getAwardAuthToken() {
        return mAwardAuthToken;
    }

    public String getCommerceOffering() {
        return mCommerceOffering;
    }

    public Picture getPicture() {
        return mPicture;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public void setAwardAuthToken(String awardAuthToken) {
        mAwardAuthToken = awardAuthToken;
    }

    public void setCommerceOffering(String commerceOffering) {
        mCommerceOffering = commerceOffering;
    }

    public void setPicture(Picture picture) {
        mPicture = picture;
    }

    public void showRewardDetails() {
        Fragment awardDetailsFragment = AwardDetailsFragment.newInstance(this);
        if (awardDetailsFragment != null) {
            CivifyMap.getInstance().getContext()
                    .setFragment(awardDetailsFragment, DrawerActivity.DETAILS_AWARD_ID);
        }
    }
}
