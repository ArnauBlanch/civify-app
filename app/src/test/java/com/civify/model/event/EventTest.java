package com.civify.model.event;

import static junit.framework.Assert.assertEquals;

import android.util.Log;

import com.civify.model.Picture;
import com.civify.utils.ServiceGenerator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EventTest {

    private static final String TAG = EventTest.class.getName();
    private static final String DATE = "2017-04-22T22:11:41.000Z";
    private static final String TOKEN = "token";

    private Date mDate;
    private Event mEvent;

    @Before
    public void setUp(){
        mEvent = new Event();
        DateFormat dateFormat = new SimpleDateFormat(ServiceGenerator.RAILS_DATE_FORMAT, Locale
                .getDefault());
        try {
            mDate = dateFormat.parse(DATE);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @After
    public void tearDown() {
        mEvent = null;
    }

    @Test
    public void testStartDate(){
        mEvent.setStartDate(mDate);
        assertEquals(mDate, mEvent.getStartDate());
    }

    @Test
    public void testEndDate(){
        mEvent.setEndDate(mDate);
        assertEquals(mDate, mEvent.getEndDate());
    }

    @Test
    public void testUpdatedAt(){
        mEvent.setUpdatedAt(mDate);
        assertEquals(mDate, mEvent.getUpdatedAt());
    }

    @Test
    public void testToken(){
        mEvent.setToken(TOKEN);
        assertEquals(TOKEN, mEvent.getToken());
    }

    @Test
    public void testPicture() {
        Picture picture = new Picture();
        mEvent.setPicture(picture);
        assertEquals(picture, mEvent.getPicture());
    }
}
