package com.civify.model;

import com.civify.utils.ServiceGenerator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Picture implements Serializable {
    @Expose
    @SerializedName("file_name")
    private String mFileName;

    @Expose
    @SerializedName("content_type")
    private String mContentType;

    @Expose(deserialize = false)
    @SerializedName("content")
    private String mContent;

    @Expose(serialize = false)
    @SerializedName("file_size")
    private int mFileSize;

    @Expose(serialize = false)
    @SerializedName("updated_at")
    private Date mUpdatedAt;

    @Expose(serialize = false)
    @SerializedName("small_url")
    private String mSmallUrl;

    @Expose(serialize = false)
    @SerializedName("med_url")
    private String mMedUrl;

    @Expose(serialize = false)
    @SerializedName("large_url")
    private String mLargeUrl;

    private String mBaseUrl;

    public Picture() {

    }

    public Picture(String fileName, String contentType, String content) {
        mFileName = fileName;
        mContentType = contentType;
        mContent = content;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    public String getContentType() {
        return mContentType;
    }

    public void setContentType(String contentType) {
        mContentType = contentType;
    }

    public int getFileSize() {
        return mFileSize;
    }

    public void setFileSize(int fileSize) {
        mFileSize = fileSize;
    }

    public Date getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public String getSmallUrl() {
        if (mBaseUrl != null) {
            return mBaseUrl + mSmallUrl;
        }
        return ServiceGenerator.BASE_URL + mSmallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        mSmallUrl = smallUrl;
    }

    public String getMedUrl() {
        if (mBaseUrl != null) {
            return mBaseUrl + mMedUrl;
        }
        return ServiceGenerator.BASE_URL + mMedUrl;
    }

    public void setMedUrl(String medUrl) {
        mMedUrl = medUrl;
    }

    public String getLargeUrl() {
        if (mBaseUrl != null) {
            return mBaseUrl + mLargeUrl;
        }
        return ServiceGenerator.BASE_URL + mLargeUrl;
    }

    public void setLargeUrl(String largeUrl) {
        mLargeUrl = largeUrl;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public void setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
    }
}
