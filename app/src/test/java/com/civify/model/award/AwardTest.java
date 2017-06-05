package com.civify.model.award;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import android.util.Log;

import com.civify.model.Picture;
import com.civify.utils.ServiceGenerator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AwardTest {

    private static final String TAG = AwardTest.class.getName();
    private static final String TITLE = "new_title";
    private static final String DESCRIPTION = "new_desc";
    private static final int PRICE = 20;
    private static final String AUTH_TOKEN = "award_auth_token";
    private static final String OFFERED_BY = "commerce_auth_token";
    private Date mCreatedAt;
    private Date mUpdatedAt;
    private Award mAward;


    @Before
    public void setUp() {
        mAward = new Award();
        DateFormat dateFormat = new SimpleDateFormat(ServiceGenerator.RAILS_DATE_FORMAT, Locale
                .getDefault());
        try {
            mUpdatedAt = dateFormat.parse("2017-03-28T23:53:20.000Z");
            mCreatedAt = dateFormat.parse("2017-03-28T23:53:20.000Z");
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @After
    public void tearDown() {
        mAward = null;
    }

    @Test
    public void testTitle() {
        mAward.setTitle(TITLE);
        assertEquals(TITLE, mAward.getTitle());
    }

    @Test
    public void testDescription() {
        mAward.setDescription(DESCRIPTION);
        assertEquals(DESCRIPTION, mAward.getDescription());
    }

    @Test
    public void testPrice() {
        mAward.setPrice(PRICE);
        assertEquals(PRICE, mAward.getPrice());
    }

    @Test
    public void testCreatedAt() {
        mAward.setCreatedAt(mCreatedAt);
        assertEquals(mCreatedAt, mAward.getCreatedAt());
    }

    @Test
    public void testUpdatedAt() {
        mAward.setUpdatedAt(mUpdatedAt);
        assertEquals(mUpdatedAt, mAward.getUpdatedAt());
    }

    @Test
    public void testAwardAuthToken() {
        mAward.setAwardAuthToken(AUTH_TOKEN);
        assertEquals(AUTH_TOKEN, mAward.getAwardAuthToken());
    }

    @Test
    public void testOfferedBy() {
        mAward.setCommerceOffering(OFFERED_BY);
        assertEquals(OFFERED_BY, mAward.getCommerceOffering());
    }

    @Test
    public void testPicture() {
        Picture picture = mock(Picture.class);
        mAward.setPicture(picture);
        Assert.assertEquals(picture, mAward.getPicture());
    }
}
