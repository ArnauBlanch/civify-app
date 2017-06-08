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
            setPositiveButton(null, yesListener);
        }
        if (noListener != null) {
            setNegativeButton(null, noListener);
        }
    }

    public final void setPositiveButton(
            @Nullable String text, @Nullable OnClickListener yesListener) {
        String checkedTest = text;
        if (text == null) checkedTest = getContext().getString(android.R.string.ok);
        setButton(BUTTON_POSITIVE, checkedTest, yesListener);
    }

    public final void setNegativeButton(
            @Nullable String text, @Nullable OnClickListener noListener) {
        String checkedTest = text;
        if (text == null) checkedTest = getContext().getString(android.R.string.cancel);
        setButton(BUTTON_NEGATIVE, checkedTest, noListener);
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
