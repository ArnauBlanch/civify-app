package com.civify.civify.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IssueTest {
    private static final String RAILS_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final String TITLE = "test-issue-title";
    private static final String DESCRIPTION = "test-issue-description";
    private static final String CATEGORY = "test-issue-category";
    private static final boolean RISK = true;
    private static final float LONGITUDE = 45.0f;
    private static final float LATITUDE = 44.0f;
    private static final int CONFIRM_VOTES = 5;
    private static final int RESOLVED_VOTES = 4;
    private static final boolean RESOLVED = false;
    private static final int REPORTS = 6;
    private static final String ISSUE_AUTH_TOKEN = "test-issue-auth-token";
    private static final String USER_AUTH_TOKEN = "test-user-auth-token";
    private Date mCreatedAt;
    private Date mUpdatedAt;
    private Issue mIssue;

    @Before
    public void setUp() {
        mIssue = new Issue();
        String stringDate1 = "2017-03-28T23:53:20+0100";
        String stringDate2 = "2016-04-24T23:53:20+0100";
        DateFormat dateFormat = new SimpleDateFormat(RAILS_DATE_FORMAT, Locale.FRANCE);
        try {
            mUpdatedAt = dateFormat.parse(stringDate1);
            mCreatedAt = dateFormat.parse(stringDate2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        mIssue = null;
    }

    @Test
    public void testTitle() {
        mIssue.setTitle(TITLE);
        assertEquals(TITLE, mIssue.getTitle());
    }

    @Test
    public void testDescription() {
        mIssue.setDescription(DESCRIPTION);
        assertEquals(DESCRIPTION, mIssue.getDescription());
    }

    @Test
    public void testCategory() {
        mIssue.setCategory(CATEGORY);
        assertEquals(CATEGORY, mIssue.getCategory());
    }

    @Test
    public void testRisk() {
        mIssue.setRisk(RISK);
        assertEquals(RISK, mIssue.isRisk());
    }

    @Test
    public void testLongitude() {
        mIssue.setLongitude(LONGITUDE);
        assertTrue(LONGITUDE == mIssue.getLongitude());
    }

    @Test
    public void testLatitude() {
        mIssue.setLatitude(LATITUDE);
        assertTrue(LATITUDE == mIssue.getLatitude());
    }

    @Test
    public void testConfirmVotes() {
        mIssue.setConfirmVotes(CONFIRM_VOTES);
        assertEquals(CONFIRM_VOTES, mIssue.getConfirmVotes());
    }

    @Test
    public void testResolvedVotes() {
        mIssue.setResolvedVotes(RESOLVED_VOTES);
        assertEquals(RESOLVED_VOTES, mIssue.getResolvedVotes());
    }

    @Test
    public void testResolved() {
        mIssue.setResolved(RESOLVED);
        assertEquals(RESOLVED, mIssue.isResolved());
    }

    @Test
    public void testReports() {
        mIssue.setReports(REPORTS);
        assertEquals(REPORTS, mIssue.getReports());
    }

    @Test
    public void testIssueAuthToken() {
        mIssue.setIssueAuthToken(ISSUE_AUTH_TOKEN);
        assertEquals(ISSUE_AUTH_TOKEN, mIssue.getIssueAuthToken());
    }

    @Test
    public void testUserAuthToken() {
        mIssue.setUserAuthToken(USER_AUTH_TOKEN);
        assertEquals(USER_AUTH_TOKEN, mIssue.getUserAuthToken());
    }

    @Test
    public void testCreatedAt() {
        mIssue.setCreatedAt(mCreatedAt);
        assertEquals(mCreatedAt, mIssue.getCreatedAt());
    }

    @Test
    public void testUpdatedAt() {
        mIssue.setUpdatedAt(mUpdatedAt);
        assertEquals(mUpdatedAt, mIssue.getUpdatedAt());
    }

    @Test
    public void testPicture() {
        Picture picture = mock(Picture.class);
        mIssue.setPicture(picture);
        assertEquals(picture, mIssue.getPicture());
    }

    @Test
    public void testFirstConstructor() {
        Picture picture = mock(Picture.class);
        mIssue = new Issue(TITLE, DESCRIPTION, CATEGORY, RISK, LONGITUDE, LATITUDE, picture);
        assertEquals(TITLE, mIssue.getTitle());
        assertEquals(DESCRIPTION, mIssue.getDescription());
        assertEquals(CATEGORY, mIssue.getCategory());
        assertEquals(RISK, mIssue.isRisk());
        assertTrue(LONGITUDE == mIssue.getLongitude());
        assertTrue(LATITUDE == mIssue.getLatitude());
        assertEquals(0, mIssue.getConfirmVotes());
        assertEquals(0, mIssue.getResolvedVotes());
        assertEquals(false, mIssue.isResolved());
        assertEquals(0, mIssue.getReports());
        assertEquals(picture, mIssue.getPicture());
    }

    @Test
    public void testSecondConstructor() {
        Picture picture = mock(Picture.class);
        mIssue = new Issue(TITLE, DESCRIPTION, CATEGORY, RISK, LONGITUDE, LATITUDE,
                CONFIRM_VOTES, RESOLVED_VOTES, RESOLVED, REPORTS, mCreatedAt, mUpdatedAt,
                ISSUE_AUTH_TOKEN, USER_AUTH_TOKEN, picture);
        assertEquals(TITLE, mIssue.getTitle());
        assertEquals(DESCRIPTION, mIssue.getDescription());
        assertEquals(CATEGORY, mIssue.getCategory());
        assertEquals(RISK, mIssue.isRisk());
        assertTrue(LONGITUDE == mIssue.getLongitude());
        assertTrue(LATITUDE == mIssue.getLatitude());
        assertEquals(CONFIRM_VOTES, mIssue.getConfirmVotes());
        assertEquals(RESOLVED_VOTES, mIssue.getResolvedVotes());
        assertEquals(RESOLVED, mIssue.isResolved());
        assertEquals(REPORTS, mIssue.getReports());
        assertEquals(picture, mIssue.getPicture());
        assertEquals(ISSUE_AUTH_TOKEN, mIssue.getIssueAuthToken());
        assertEquals(USER_AUTH_TOKEN, mIssue.getUserAuthToken());
        assertEquals(mCreatedAt, mIssue.getCreatedAt());
        assertEquals(mUpdatedAt, mIssue.getUpdatedAt());
    }

}
