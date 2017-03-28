package com.civify.civify.model;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PictureTest {
    private static final String RAILS_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final String FILE_NAME = "test-file-name";
    private static final String CONTENT_TYPE = "test-content-type";
    private static final int FILE_SIZE = 1234;
    private Date mUpdatedAt;
    private static final String SMALL_URL = "test-small-url";
    private static final String MED_URL = "test-med-url";
    private static final String LARGE_URL = "test-large-url";

    private Picture mPicture;

    @Before
    public void setUp() {
        mPicture = new Picture();
        String stringDate = "2017-03-28T23:53:20+0100";
        DateFormat dateFormat = new SimpleDateFormat(RAILS_DATE_FORMAT, Locale.FRANCE);
        try {
            mUpdatedAt = dateFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        mPicture = null;
    }

    @Test
    public void testPictureFileName() {
        mPicture.setPictureFileName(FILE_NAME);
        assertEquals(FILE_NAME, mPicture.getPictureFileName());
    }

    @Test
    public void testPictureContentType() {
        mPicture.setPictureContentType(CONTENT_TYPE);
        assertEquals(CONTENT_TYPE, mPicture.getPictureContentType());
    }

    @Test
    public void testPictureFileSize() {
        mPicture.setPictureFileSize(FILE_SIZE);
        assertEquals(FILE_SIZE, mPicture.getPictureFileSize());
    }

    @Test
    public void testPictureUpdatedAt() {
        mPicture.setPictureUpdatedAt(mUpdatedAt);
        assertEquals(mUpdatedAt, mPicture.getPictureUpdatedAt());
    }

    @Test
    public void testPictureSmallUrl() {
        mPicture.setPictureSmallUrl(SMALL_URL);
        assertEquals(SMALL_URL, mPicture.getPictureSmallUrl());
    }

    @Test
    public void testPictureMedUrl() {
        mPicture.setPictureMedUrl(MED_URL);
        assertEquals(MED_URL, mPicture.getPictureMedUrl());
    }

    @Test
    public void testPictureLargeUrl() {
        mPicture.setPictureLargeUrl(LARGE_URL);
        assertEquals(LARGE_URL, mPicture.getPictureLargeUrl());
    }

    @Test
    public void testConstructor() {
        mPicture = new Picture(FILE_NAME, CONTENT_TYPE, FILE_SIZE, mUpdatedAt, SMALL_URL,
                MED_URL, LARGE_URL);
        assertEquals(FILE_NAME, mPicture.getPictureFileName());
        assertEquals(CONTENT_TYPE, mPicture.getPictureContentType());
        assertEquals(FILE_SIZE, mPicture.getPictureFileSize());
        assertEquals(mUpdatedAt, mPicture.getPictureUpdatedAt());
        assertEquals(SMALL_URL, mPicture.getPictureSmallUrl());
        assertEquals(MED_URL, mPicture.getPictureMedUrl());
        assertEquals(LARGE_URL, mPicture.getPictureLargeUrl());
    }
}
