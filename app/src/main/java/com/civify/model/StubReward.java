package com.civify.model;

/**
 * Created by MSI on 08/05/2017.
 */

public class StubReward {

    private static final int FAKE_COINS = 35;
    private static final String FAKE_COMPANY = "Decathlon";
    private static final String FAKE_TITLE = "10% descompte en alpinisme";
    private static final String FAKE_DESCRIPTION = "Aquest descompte et permetre anar a la "
            + "muntanya mes alta del teu poble i caminar amb unes botes carisimes que et "
            + "servirien igual que unes botes comprades al senyor xines de les rambles, pero "
            + "posem que son de marca i ens forrem.";
    private static final String FAKE_URL = "http://corporate.decathlon" + ".com/wp-content/uploads/2014/02/Simond_alpinisme.jpg";

    private int mCoins;
    private String mTitle;
    private String mCompany;
    private String mDescription;
    private String mUrlImage;

    public StubReward() {
        mCoins = FAKE_COINS;
        mTitle = FAKE_TITLE;
        mCompany = FAKE_COMPANY;
        mDescription = FAKE_DESCRIPTION;
        mUrlImage = FAKE_URL;
    }

    public String getUrlImage() {
        return mUrlImage;
    }

    public int getCoins() {
        return mCoins;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getCompany() {
        return mCompany;
    }

    public String getDescription() {
        return mDescription;
    }
}
