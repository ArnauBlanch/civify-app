package com.civify.civify.model;


public class User {

    private static final int LEVEL = 3;
    private static final int EXPERIENCE = 50;
    private static final int COINS = 432;


    private String mName;
    private String mUsername;
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
    
    public String getName() {
        return mName;
    }
    
    public void setName(String name) {
        mName = name;
    }
    
    public String getUsername() {
        return mUsername;
    }
    
    public void setUsername(String username) {
        mUsername = username;
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
