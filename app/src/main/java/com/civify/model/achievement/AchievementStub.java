package com.civify.model.achievement;

/**
 * Created by MSI on 01/06/2017.
 */

public class AchievementStub {

    private static final String URL_IMAGE = "http://1.bp.blogspot.com/_jifqT6DMvgw/TQLKH4pth0I/"
            + "AAAAAAAAAVU/ZTMWA300pCM/s1600/medalla.jpg";

    private int mId;
    private String mTitle;
    private String mDescription;
    private int mNumber;
    private String mKind;
    private int mCoins;
    private int mXp;
    private String mBadgePicture;
    private String mToken;

    private int mProgress;

    public AchievementStub(int id, String title, String description, int number, String kind,
            int coins, int xp, String badgePicture, String token) {
        this.mId = id;
        this.mTitle = title;
        this.mDescription = description;
        this.mNumber = number;
        this.mKind = kind;
        this.mCoins = coins;
        this.mXp = xp;
        this.mBadgePicture = badgePicture;
        this.mToken = token;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int number) {
        this.mNumber = number;
    }

    public String getKind() {
        return mKind;
    }

    public void setKind(String kind) {
        this.mKind = kind;
    }

    public int getCoins() {
        return mCoins;
    }

    public void setCoins(int coins) {
        this.mCoins = coins;
    }

    public int getXp() {
        return mXp;
    }

    public void setXp(int xp) {
        this.mXp = xp;
    }

    public String getBadgePicture() {
        return URL_IMAGE;
    }

    public void setBadgePicture(String badgePicture) {
        this.mBadgePicture = badgePicture;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        this.mToken = token;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        mProgress = progress;
    }
}
