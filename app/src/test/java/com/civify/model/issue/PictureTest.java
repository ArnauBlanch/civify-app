package com.civify.model.issue;

import static org.junit.Assert.assertEquals;

import com.civify.model.issue.Picture;
import com.civify.utils.ServiceGenerator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PictureTest {
    private static final String FILE_NAME = "test-file-name";
    private static final String CONTENT_TYPE = "test-content-type";
    private static final String CONTENT = "test-content";
    private static final int FILE_SIZE = 1234;
    private Date mUpdatedAt;
    private static final String SMALL_URL = "test-small-url";
    private static final String MED_URL = "test-med-url";
    private static final String LARGE_URL = "test-large-url";

    private Picture mPicture;

    @Before
    public void setUp() {
        mPicture = new Picture();
        String stringDate = "2017-03-28T23:53:20.000Z";
        DateFormat dateFormat = new SimpleDateFormat(ServiceGenerator.RAILS_DATE_FORMAT, Locale
                .getDefault());
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
        mPicture.setFileName(FILE_NAME);
        assertEquals(FILE_NAME, mPicture.getFileName());
    }

    @Test
    public void testPictureContentType() {
        mPicture.setContentType(CONTENT_TYPE);
        assertEquals(CONTENT_TYPE, mPicture.getContentType());
    }

    @Test
    public void testPictureContent() {
        mPicture.setContent(CONTENT);
        assertEquals(CONTENT, mPicture.getContent());
    }

    @Test
    public void testPictureFileSize() {
        mPicture.setFileSize(FILE_SIZE);
        assertEquals(FILE_SIZE, mPicture.getFileSize());
    }

    @Test
    public void testPictureUpdatedAt() {
        mPicture.setUpdatedAt(mUpdatedAt);
        assertEquals(mUpdatedAt, mPicture.getUpdatedAt());
    }

    @Test
    public void testPictureSmallUrl() {
        mPicture.setSmallUrl(SMALL_URL);
        assertEquals(SMALL_URL, mPicture.getSmallUrl());
    }

    @Test
    public void testPictureMedUrl() {
        mPicture.setMedUrl(MED_URL);
        assertEquals(MED_URL, mPicture.getMedUrl());
    }

    @Test
    public void testPictureLargeUrl() {
        mPicture.setLargeUrl(LARGE_URL);
        assertEquals(LARGE_URL, mPicture.getLargeUrl());
    }

    @Test
    public void testConstructor() {
        mPicture = new Picture(FILE_NAME, CONTENT_TYPE, CONTENT);
        assertEquals(FILE_NAME, mPicture.getFileName());
        assertEquals(CONTENT_TYPE, mPicture.getContentType());
        assertEquals(CONTENT, mPicture.getContent());
    }
}
