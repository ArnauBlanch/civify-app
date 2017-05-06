package com.civify.model.reward;

import com.civify.model.issue.Picture;

public class RewardStub {

    private String mTitle;
    private String mBusiness;
    private int mCoins;
    private Picture mPicture;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBusiness() {
        return mBusiness;
    }

    public void setBusiness(String business) {
        mBusiness = business;
    }

    public int getCoins() {
        return mCoins;
    }

    public void setCoins(int coins) {
        mCoins = coins;
    }

    public Picture getPicture() {
        return mPicture;
    }

    public void setPicture(Picture picture) {
        mPicture = picture;
    }

    public void showRewardDetails() {
        // TODO: change to reward details fragment
        /*
        Fragment rewardDetailsFragment = RewardDetailsFragment.newInstance(this);
        if (rewardDetailsFragment != null) {
            CivifyMap.getInstance().getContext()
                    .setFragment(rewardDetailsFragment, DrawerActivity.REWARD_DETAILS_ID);
        }
       */
    }
}
