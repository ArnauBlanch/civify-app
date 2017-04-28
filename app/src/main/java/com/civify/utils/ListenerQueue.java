package com.civify.utils;

import android.support.annotation.Nullable;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ListenerQueue {

    // Thread-safe
    private final ConcurrentLinkedQueue<Runnable> mListeners;

    public ListenerQueue() {
        mListeners = new ConcurrentLinkedQueue<>();
    }

    /** Adds the listener to the end of this queue iff it is not null. */
    public void enqueue(@Nullable Runnable listener) {
        if (listener != null) mListeners.add(listener);
    }

    /** Runs all listeners from the first to the last listener enqueued. */
    public void consume() {
        synchronized (mListeners) {
            Iterator<Runnable> listenerIterator = mListeners.iterator();
            while (listenerIterator.hasNext()) {
                listenerIterator.next().run();
                listenerIterator.remove();
            }
        }
    }

    public int size() {
        return mListeners.size();
    }

    public boolean isEmpty() {
        return mListeners.isEmpty();
    }
}
