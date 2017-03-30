package com.civify.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ConfirmDialog extends AlertDialog {

    public ConfirmDialog(@NonNull Context context) {
        super(context);
        setCanceledOnTouchOutside(false);
        setPositiveButton(context.getString(android.R.string.yes), null);
    }

    public ConfirmDialog(@NonNull Context context,
                         @NonNull String title,
                         @NonNull String message) {
        this(context);
        setTitle(title);
        setMessage(message);
    }

    public ConfirmDialog(@NonNull Context context,
                         @NonNull String title,
                         @NonNull String message,
                         @Nullable OnClickListener yesListener,
                         @Nullable OnClickListener noListener) {
        this(context, title, message);
        if (yesListener != null) {
            setPositiveButton(context.getString(android.R.string.yes), yesListener);
        }
        if (noListener != null) {
            setNegativeButton(context.getString(android.R.string.no), noListener);
        }
    }

    public final void setPositiveButton(
            @NonNull String text, @Nullable OnClickListener yesListener) {
        setButton(BUTTON_POSITIVE, text, yesListener);
    }

    public final void setNegativeButton(
            @NonNull String text, @Nullable OnClickListener noListener) {
        setButton(BUTTON_NEGATIVE, text, noListener);
    }

    public static void show(@NonNull Context context,
                            @NonNull String title,
                            @NonNull String message,
                            @Nullable OnClickListener yesListener,
                            @Nullable OnClickListener noListener) {
        new ConfirmDialog(context, title, message, yesListener, noListener).show();
    }

    public static void show(@NonNull Context context,
                            @NonNull String title,
                            @NonNull String message) {
        new ConfirmDialog(context, title, message).show();
    }
}
