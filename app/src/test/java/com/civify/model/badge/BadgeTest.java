package com.civify.model.badge;

import static junit.framework.Assert.assertEquals;

import com.civify.utils.ServiceGenerator;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BadgeTest {

    private static final String TITLE = "new_title";
    private static final String CONTENT_TYPE = "content_type";
    private static final String URL = "new_url";
    private static final String TYPE = "new_type";
    private static final String TOKEN = "new_token";
    private Badge mBadge;
    private Date mDate;

    @Before
    public void setUp() {
        mBadge = new Badge();
        mDate = new Date();
    }

    @After
    public void tearDown() {
        mBadge = null;
        mDate = null;
    }

    @Test
    public void testTitle() {
        mBadge.setTitle(TITLE);
        assertEquals(TITLE, mBadge.getTitle());
    }

    @Test
    public void testContent_type() {
        mBadge.setContentType(CONTENT_TYPE);
        assertEquals(CONTENT_TYPE, mBadge.getContentType());
    }

    @Test
    public void testUrl() {
        mBadge.setUrl(URL);
        assertEquals(ServiceGenerator.BASE_URL + URL, mBadge.getUrl());
    }

    @Test
    public void testObtainedAt() {
        mBadge.setObtainedDate(mDate);
        assertEquals(mDate, mBadge.getObtainedDate());
    }

    @Test
    public void testCorrespondsToType() {
        mBadge.setCorrespondsToType(TYPE);
        assertEquals(TYPE, mBadge.getCorrespondsToType());
    }

    @Test
    public void testCorrespondsToToken() {
        mBadge.setCorrespondsToToken(TOKEN);
        assertEquals(TOKEN, mBadge.getCorrespondsToToken());
    }
}
