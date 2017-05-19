package com.civify.model.award;

import android.support.v4.app.Fragment;

import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.reward.ShowQrFragment;
import com.civify.model.issue.Picture;
import com.civify.model.map.CivifyMap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ExchangedAward extends Award {

    private static final long serialVersionUID = 5570093585413874196L;

    @Expose(serialize = false)
    @SerializedName("used")
    private boolean mUsed;

    @Expose(serialize = false)
    @SerializedName("exchange_auth_token")
    private String mCode;

    public ExchangedAward(String title, String description, int price, Date createdAt,
            Date updatedAt, String awardAuthToken, String commerceOffering, Picture picture,
            boolean used, String code) {
        super(title, description, price, createdAt, updatedAt, awardAuthToken, commerceOffering,
                picture);
        mUsed = used;
        mCode = code;
    }

    public boolean isUsed() {
        return mUsed;
    }

    public void setUsed(boolean used) {
        mUsed = used;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public void showDetails() {
        Fragment awardDetailsFragment = ShowQrFragment.newInstance(this);
        if (awardDetailsFragment != null) {
            CivifyMap.getInstance().getContext()
                    .setFragment(awardDetailsFragment, DrawerActivity.DETAILS_QR_ID);
        }
    }
}
