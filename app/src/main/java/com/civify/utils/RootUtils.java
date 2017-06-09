package com.civify.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.civify.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public final class RootUtils {

    private static boolean sRootPermitted = true;

    private RootUtils() { }

    public static boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
    }

    public static boolean isRootPermitted() {
        return sRootPermitted || !RootUtils.isDeviceRooted();
    }

    public static void setRootPermitted(boolean permitted, @NonNull Context context,
            @Nullable Runnable onProhibit, @Nullable Runnable onDissmissDialog) {
        sRootPermitted = permitted;
        if (!permitted) verifyRootPermitted(context, onProhibit, onDissmissDialog);
    }

    public static boolean verifyRootPermitted(@NonNull Context context,
            @Nullable Runnable onProhibit,
            @Nullable Runnable onConfirm) {
        if (!isRootPermitted()) {
            if (onProhibit != null) onProhibit.run();
            showRootSettingsDialog(context, onConfirm);
            return false;
        }
        return true;
    }

    private static void showRootSettingsDialog(@NonNull Context context,
            @Nullable final Runnable onConfirm) {
        ConfirmDialog rootDialog = new ConfirmDialog(context,
                context.getString(R.string.rooted_device),
                context.getString(R.string.rooted_device_message));
        rootDialog.setPositiveButton(null, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onConfirm != null) onConfirm.run();
            }
        });
        rootDialog.show();
    }

    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod2() {
        String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su",
                          "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su",
                          "/system/sd/xbin/su", "/system/bin/failsafe/su",
                          "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        boolean checked = false;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] {"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            checked = in.readLine() != null;
        } catch (IOException ignore) {
            checked = false;
        } finally {
            if (process != null) process.destroy();
        }
        return checked;
    }
}
