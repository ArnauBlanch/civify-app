package com.civify.model;

import com.google.gson.annotations.SerializedName;

public class User {

    private static final int COINS = 432;
    private static final int EXPERIENCE = 50;
    private static final int LEVEL = 3;
    @SerializedName("username")
    private String mUsername;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("password_digest")
    private String mPassworddigest;
    @SerializedName("created_at")
    private String mCreatedat;
    @SerializedName("updated_at")
    private String mUpdatedat;
    private String mName;
    private int mLevel;
    private int mExperience;
    private int mCoins;


    public User() {
        mName = "David Segovia";
        mUsername = "dsegoviat";
        mLevel = LEVEL;
        mExperience = EXPERIENCE;
        mCoins = COINS;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getPassword_digest() {
        return mPassworddigest;
    }

    public void setPassword_digest(String passworddigest) {
        this.mPassworddigest = passworddigest;
    }

    public String getCreated_at() {
        return mCreatedat;
    }

    public void setCreated_at(String createdat) {
        this.mCreatedat = createdat;
    }

    public String getUpdatedat() {
        return mUpdatedat;
    }

    public void setUpdated_at(String updatedat) {
        this.mUpdatedat = updatedat;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        mLevel = level;
    }

    public int getExperience() {
        return mExperience;
    }

    public void setExperience(int experience) {
        mExperience = experience;
    }

    public int getCoins() {
        return mCoins;
    }

    public void setCoins(int coins) {
        mCoins = coins;
    }
}
