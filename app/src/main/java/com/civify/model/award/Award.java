package com.civify.model.award;

import com.civify.model.issue.Picture;
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

    public Award() {}

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
}
