package com.civify.model.badge;

import com.civify.utils.ServiceGenerator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Badge implements Serializable {

    @Expose
    @SerializedName("title")
    private String mTitle;

    @Expose
    @SerializedName("content_type")
    private String mContentType;

    @Expose
    @SerializedName("large_url")
    private String mUrl;

    @Expose
    @SerializedName("obtained_date")
    private Date mObtainedDate;

    @Expose
    @SerializedName("corresponds_to_type")
    private String mCorrespondsToType;

    @Expose
    @SerializedName("corresponds_to_token")
    private String mCorrespondsToToken;

    public Badge() { }

    public Badge(String title, String contentType, String url, Date obtainedDate,
            String correspondsToType, String correspondsToToken) {
        mTitle = title;
        mContentType = contentType;
        mUrl = url;
        mObtainedDate = obtainedDate;
        mCorrespondsToType = correspondsToType;
        mCorrespondsToToken = correspondsToToken;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getContentType() {
        return mContentType;
    }

    public void setContentType(String contentType) {
        mContentType = contentType;
    }

    public String getUrl() {
        return ServiceGenerator.BASE_URL + mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public Date getObtainedDate() {
        return mObtainedDate;
    }

    public void setObtainedDate(Date obtainedDate) {
        mObtainedDate = obtainedDate;
    }

    public String getCorrespondsToType() {
        return mCorrespondsToType;
    }

    public void setCorrespondsToType(String correspondsToType) {
        mCorrespondsToType = correspondsToType;
    }

    public String getCorrespondsToToken() {
        return mCorrespondsToToken;
    }

    public void setCorrespondsToToken(String correspondsToToken) {
        mCorrespondsToToken = correspondsToToken;
    }
}
