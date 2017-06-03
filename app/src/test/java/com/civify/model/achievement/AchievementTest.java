package com.civify.model.achievement;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import android.util.Log;

import com.civify.utils.ServiceGenerator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AchievementTest {

    private static final String TAG = AchievementTest.class.getName();
    private static final String TITLE = "sample_title";
    private static final String DESCRIPTION = "description";
    private static final int NUMBER = 10;
    private static final String KIND = "kind";
    private static final int COINS = 10;
    private static final int XP = 100;
    private static final String DATE = "2017-04-22T22:11:41.000Z";
    private static final String TOKEN = "token";
    private static final int PROGRESS = 3;

    private Date mDate;
    private Achievement mAchievement;

    @Before
    public void setUp(){
        mAchievement = new Achievement();
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
        mAchievement = null;
    }

    @Test
    public void testTitle() {
        mAchievement.setTitle(TITLE);
        assertEquals(TITLE, mAchievement.getTitle());
    }

    @Test
    public void testDescription() {
        mAchievement.setDescription(DESCRIPTION);
        assertEquals(DESCRIPTION, mAchievement.getDescription());
    }

    @Test
    public void testNumber() {
        mAchievement.setNumber(NUMBER);
        assertEquals(NUMBER, mAchievement.getNumber());
    }

    @Test
    public void testKind() {
        mAchievement.setKind(KIND);
        assertEquals(KIND, mAchievement.getKind());
    }

    @Test
    public void testCoins() {
        mAchievement.setCoins(COINS);
        assertEquals(COINS, mAchievement.getCoins());
    }

    @Test
    public void testXp() {
        mAchievement.setXp(XP);
        assertEquals(XP, mAchievement.getXp());
    }

    @Test
    public void testXreatedAt() {
        mAchievement.setCreatedAt(mDate);
        assertEquals(mDate, mAchievement.getCreatedAt());
    }

    @Test
    public void testAchievementToken() {
        mAchievement.setAchievementToken(TOKEN);
        assertEquals(TOKEN, mAchievement.getAchievementToken());
    }

    @Test
    public void testEnabled() {
        mAchievement.setEnabled(true);
        assertTrue(mAchievement.isEnabled());
    }

    @Test
    public void testProgress() {
        mAchievement.setProgress(PROGRESS);
        assertEquals(PROGRESS, mAchievement.getProgress());
    }

    @Test
    public void testCompleted() {
        mAchievement.setCompleted(true);
        assertTrue(mAchievement.isCompleted());
    }

    @Test
    public void testClaimed() {
        mAchievement.setClaimed(true);
        assertTrue(mAchievement.isClaimed());
    }
}
