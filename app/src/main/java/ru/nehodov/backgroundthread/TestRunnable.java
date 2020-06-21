package ru.nehodov.backgroundthread;

import android.util.Log;

public class TestRunnable implements Runnable {

    private static final String TAG = TestRunnable.class.getName();

    private int times;

    public TestRunnable(int times) {
        this.times = times;
    }

    @Override
    public void run() {
        int count = 0;
        while (!Thread.currentThread().isInterrupted()) {
            Log.d(TAG, "StartThread count: " + count);
            count++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "Count is ended");
    }
}
