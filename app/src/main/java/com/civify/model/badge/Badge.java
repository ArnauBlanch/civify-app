package com.civify.model.badge;

import com.civify.utils.ServiceGenerator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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
}
