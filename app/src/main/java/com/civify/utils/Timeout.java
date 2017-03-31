package com.civify.utils;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public final class Timeout {

    private static final String TAG = Timeout.class.getSimpleName();

    private Handler mHandler;
    private Runnable mCallback;
    private long mDuration, mStart;
    private boolean mTimedOut, mCanceled;

    private Timeout(@Nullable final String tag, @NonNull final Runnable callback, long millis) {
        mDuration = millis;
        mHandler = new Handler();
        mCallback = new RunnableWrapper(tag, callback);
        reschedule();
    }

    public void reschedule() {
        cancel();
        mTimedOut = mDuration <= 0;
        mStart = System.currentTimeMillis();
        if (hasTimedOut()) mHandler.post(mCallback);
        else mHandler.postDelayed(mCallback, mDuration);
        mCanceled = false;
    }

    public void reschedule(long millis) {
        mDuration = millis;
        reschedule();
    }

    public void cancel() {
        mHandler.removeCallbacks(mCallback);
        mCanceled = true;
    }

    public boolean isCanceled() {
        return mCanceled;
    }

    public long getLapsedMillisFromSchedule() {
        return System.currentTimeMillis() - mStart;
    }

    public long getRemainingMillisToTimeOut() {
        return mDuration - getLapsedMillisFromSchedule();
    }

    public boolean hasTimedOut() {
        return mTimedOut;
    }

    @NonNull
    public static Timeout schedule(@NonNull Runnable callback, long millis) {
        return new Timeout(null, callback, millis);
    }

    @NonNull
    public static Timeout schedule(@NonNull String tag, @NonNull Runnable callback, long millis) {
        return new Timeout(tag, callback, millis);
    }

    private class RunnableWrapper implements Runnable {

        private final String mPrefix;
        private final Runnable mCallbackWrapped;

        RunnableWrapper(String prefix, Runnable callbackWrapped) {
            mPrefix = prefix;
            mCallbackWrapped = callbackWrapped;
        }

        @Override
        public void run() {
            mTimedOut = true;
            Log.v(TAG, prefix(mPrefix) + "Timed out with " + mDuration + "ms");
            mCallbackWrapped.run();
        }

        @NonNull
        private String prefix(String prefix) {
            return prefix == null || prefix.isEmpty() ? "" : "[" + prefix + " ] ";
        }
    }
}
