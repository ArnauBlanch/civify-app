package com.civify.civify.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Picture {
    @Expose
    @SerializedName("picture_file_name")
    private String mPictureFileName;

    @Expose
    @SerializedName("picture_content_type")
    private String mPictureContentType;

    @Expose
    @SerializedName("picture_file_size")
    private int mPictureFileSize;

    @Expose
    @SerializedName("picture_updated_at")
    private Date mPictureUpdatedAt;

    @Expose
    @SerializedName("picture_small_url")
    private String mPictureSmallUrl;

    @Expose
    @SerializedName("picture_med_url")
    private String mPictureMedUrl;

    @Expose
    @SerializedName("picture_large_url")
    private String mPictureLargeUrl;

    public Picture() {

    }

    public Picture(String pictureFileName, String pictureContentType, int pictureFileSize,
            Date pictureUpdatedAt, String pictureSmallUrl, String pictureMedUrl,
            String pictureLargeUrl) {
        mPictureFileName = pictureFileName;
        mPictureContentType = pictureContentType;
        mPictureFileSize = pictureFileSize;
        mPictureUpdatedAt = pictureUpdatedAt;
        mPictureSmallUrl = pictureSmallUrl;
        mPictureMedUrl = pictureMedUrl;
        mPictureLargeUrl = pictureLargeUrl;
    }

    public String getPictureFileName() {
        return mPictureFileName;
    }

    public void setPictureFileName(String pictureFileName) {
        mPictureFileName = pictureFileName;
    }

    public String getPictureContentType() {
        return mPictureContentType;
    }

    public void setPictureContentType(String pictureContentType) {
        mPictureContentType = pictureContentType;
    }

    public int getPictureFileSize() {
        return mPictureFileSize;
    }

    public void setPictureFileSize(int pictureFileSize) {
        mPictureFileSize = pictureFileSize;
    }

    public Date getPictureUpdatedAt() {
        return mPictureUpdatedAt;
    }

    public void setPictureUpdatedAt(Date pictureUpdatedAt) {
        mPictureUpdatedAt = pictureUpdatedAt;
    }

    public String getPictureSmallUrl() {
        return mPictureSmallUrl;
    }

    public void setPictureSmallUrl(String pictureSmallUrl) {
        mPictureSmallUrl = pictureSmallUrl;
    }

    public String getPictureMedUrl() {
        return mPictureMedUrl;
    }

    public void setPictureMedUrl(String pictureMedUrl) {
        mPictureMedUrl = pictureMedUrl;
    }

    public String getPictureLargeUrl() {
        return mPictureLargeUrl;
    }

    public void setPictureLargeUrl(String pictureLargeUrl) {
        mPictureLargeUrl = pictureLargeUrl;
    }
}
