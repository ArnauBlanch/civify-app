package com.civify.model.award;

import android.graphics.Bitmap;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class AwardTest {

    private Award mAward;
    private Date mCreatedAt;
    private Date mUpdatedAt;
    private Bitmap mBitmap;

    @Before
    public void setUp() {
        mAward = new Award();
    }

    @Test
    public void getTitle() {
        //...
    }
}
