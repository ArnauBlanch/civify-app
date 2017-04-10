package com.civify.activity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

import android.os.Build;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

public class BasicUiTest {

    private static final String TAG = "Permissions";

    public static void allowPermissionsIfNeeded() {
        if (Build.VERSION.SDK_INT >= 23) {
            UiDevice device = UiDevice.getInstance(getInstrumentation());
            UiObject allowPermissions = device.findObject(new UiSelector().text("Allow"));
            if (allowPermissions.exists()) {
                try {
                    allowPermissions.click();
                } catch (UiObjectNotFoundException e) {
                    Log.e(TAG, "There is no permissions dialog to interact with." + e.getMessage());
                }
            }
        }
    }
}
