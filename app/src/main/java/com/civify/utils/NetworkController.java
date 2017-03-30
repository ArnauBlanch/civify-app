package com.civify.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.civify.activity.BasicActivity;

public final class NetworkController {

    private static final String TAG = NetworkController.class.getSimpleName();

    private static boolean sDisplayingDialog;

    private static ListenerQueue sDonePressedListeners = new ListenerQueue();
    private static ListenerQueue sAfterSettingsListeners = new ListenerQueue();
    private static ListenerQueue sDismissListeners = new ListenerQueue();

    private NetworkController() { }

    private static NetworkInfo getNetworkInfo() {
        ConnectivityManager manager = (ConnectivityManager)
                BasicActivity.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo();
    }

    public static boolean isNetworkAvailable() {
        NetworkInfo networkInfo = getNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean checkNetwork(@NonNull Activity context) {
        return checkNetwork(context, null, null, null, null);
    }

    public static boolean checkNetwork(@NonNull Activity context,
                                       @Nullable Runnable noNetworkPreDialogListener,
                                       @Nullable Runnable onDismissListener) {
        return checkNetwork(context, noNetworkPreDialogListener, null, null, onDismissListener);
    }

    public static boolean checkNetwork(@NonNull Activity context,
                                       @Nullable Runnable noNetworkPreDialogListener,
                                       @Nullable Runnable onDonePressedListener,
                                       @Nullable Runnable onAfterSettingsListener,
                                       @Nullable Runnable onDismissListener) {
        return checkNetwork(isNetworkAvailable(), context,
                noNetworkPreDialogListener,
                onDonePressedListener,
                onAfterSettingsListener,
                onDismissListener);
    }

    private static boolean checkNetwork(boolean network, @NonNull final Activity context,
                                 @Nullable Runnable noNetworkPreDialogListener,
                                 @Nullable Runnable onDonePressedListener,
                                 @Nullable Runnable onAfterSettingsListener,
                                 @Nullable Runnable onDismissListener) {
        if (!network) {
            Log.i(TAG, "Network unavailable. Context " + context.getClass().getSimpleName());

            sDonePressedListeners.enqueue(onDonePressedListener);
            sAfterSettingsListeners.enqueue(onAfterSettingsListener);
            sDismissListeners.enqueue(onDismissListener);

            if (!sDisplayingDialog) {
                sDisplayingDialog = true;

                boolean airplaneMode = isAirplaneModeEnabled();
                String additionalInfo = airplaneMode
                        ? "Disable Airplane Mode and enableLocationUpdates the network."
                        : "Enable the network.";
                setNetworkDialog(context, airplaneMode, additionalInfo);
            } else if (noNetworkPreDialogListener != null) {
                noNetworkPreDialogListener.run();
            }
        }
        return network;
    }

    private static void setNetworkDialog(@NonNull final Activity context,
            final boolean airplaneMode, String additionalInfo) {
        ConfirmDialog networkDialog = new ConfirmDialog(context, "Network unavailable",
                "Network is unavailable and therefore Civify could not work! "
                        + additionalInfo);
        networkDialog.setPositiveButton("DONE", new OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int w) {
                sDonePressedListeners.consume();
            }
        });
        networkDialog.setNegativeButton("SETTINGS", new OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int w) {
                context.startActivityForResult(new Intent(airplaneMode
                        ? Settings.ACTION_AIRPLANE_MODE_SETTINGS :
                        Settings.ACTION_SETTINGS), 0);
                sAfterSettingsListeners.consume();
            }
        });
        networkDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                sDisplayingDialog = false;
                sDismissListeners.consume();
            }
        });
        networkDialog.show();
    }

    @SuppressWarnings("deprecation")
    private static boolean isAirplaneModeEnabled() {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1
                ? Settings.System.getInt(BasicActivity.getInstance().getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0)
                : Settings.Global.getInt(BasicActivity.getInstance().getContentResolver(),
                        Settings.Global.AIRPLANE_MODE_ON, 0)) != 0;
    }
}
