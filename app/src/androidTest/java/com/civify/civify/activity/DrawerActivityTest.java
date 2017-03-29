package com.civify.civify.activity;

import static android.os.Build.VERSION_CODES.M;
import static org.junit.Assert.*;

import android.app.Application;
import android.app.Instrumentation;

import org.junit.Before;
import org.junit.Test;

public class DrawerActivityTest {
    
    private Instrumentation mInstrumentation;
    private DrawerActivity mDrawerActivity;
    
    @Before
    private void setUp() {
        mInstrumentation = new Instrumentation();
        mDrawerActivity = new DrawerActivity();
    }
    
    @Test
    public void testDrawerActivityCreation() {
        mInstrumentation.callActivityOnCreate(mDrawerActivity, null);
        assertNotNull(mDrawerActivity.getNavigationView());
        assertNotNull(mDrawerActivity.getDrawerLayout());
        assertEquals(0, mDrawerActivity.getCurrentFragment());
    }
    
    //@Test
    //public void onNavigationItemSelected() throws Exception {
    //
    //}
}