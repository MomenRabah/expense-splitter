package com.team.splitapp.util;

import android.os.Handler;
import android.os.Looper;

/**
 * Background work for the whole app (Lecture 4: "better not to implement operations with
 * high latency on the UI thread").
 *
 * <p>Rule for the team: every database query, balance/settle calculation and file write runs
 * in {@link #runInBackground}. To show the result, post it back with {@link #runOnMainThread},
 * because views can only be touched from the UI (main) thread.
 *
 * <pre>
 * AppExecutors.runInBackground(() -&gt; {
 *     List&lt;Expense&gt; expenses = dao.getExpenses(groupId);      // slow: worker thread
 *     AppExecutors.runOnMainThread(() -&gt; showExpenses(expenses)); // UI: main thread
 * });
 * </pre>
 */
public final class AppExecutors {

    /** A Handler attached to the main thread's Looper delivers Runnables to the UI thread. */
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    private AppExecutors() {
    }

    /** Runs {@code task} on a new worker thread. */
    public static void runInBackground(Runnable task) {
        Thread worker = new Thread(task, "split-worker");
        worker.start();
    }

    /** Runs {@code task} on the UI thread. */
    public static void runOnMainThread(Runnable task) {
        MAIN_HANDLER.post(task);
    }
}
