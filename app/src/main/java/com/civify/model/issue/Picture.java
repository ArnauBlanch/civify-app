package com.civify.model.issue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Picture {
    @Expose
    @SerializedName("file_name")
    private String mFileName;

    @Expose
    @SerializedName("content_type")
    private String mContentType;

    @Expose
    @SerializedName("content")
    private String mContent;

    @Expose(serialize = false)
    @SerializedName("file_size")
    private int mFileSize;

    @Expose(serialize = false)
    @SerializedName("picture_updated_at")
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

    public Picture() {

    }

    public Picture(String fileName, String contentType, String content, int fileSize, Date
            updatedAt, String smallUrl, String medUrl, String largeUrl) {
        mFileName = fileName;
        mContentType = contentType;
        mFileSize = fileSize;
        mUpdatedAt = updatedAt;
        mSmallUrl = smallUrl;
        mMedUrl = medUrl;
        mLargeUrl = largeUrl;
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
        return mSmallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        mSmallUrl = smallUrl;
    }

    public String getMedUrl() {
        return mMedUrl;
    }

    public void setMedUrl(String medUrl) {
        mMedUrl = medUrl;
    }

    public String getLargeUrl() {
        return mLargeUrl;
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
}
