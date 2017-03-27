package com.civify.civify.activity.model;

public class UserFake {
    
    private String mUsername;
    private String mFullName;
    private int mLevel;
    private int mCoins;
    
    public UserFake() {
        mUsername = "ArnauBlanch";
        mFullName = "Arnau Blanch Cortès";
        mLevel = 2;
        mCoins = 122;
    }
    
    public String getUsername() {
        return mUsername;
    }
    
    public void setUsername(String username) {
        mUsername = username;
    }
    
    public String getFullName() {
        return mFullName;
    }
    
    public void setFullName(String fullName) {
        mFullName = fullName;
    }
    
    public int getLevel() {
        return mLevel;
    }
    
    public void setLevel(int level) {
        mLevel = level;
    }
    
    public int getCoins() {
        return mCoins;
    }
    
    public void setCoins(int coins) {
        mCoins = coins;
    }
}
