package ru.nehodov.backgroundthread;

import android.util.Log;

public class TestThread extends Thread {

    private static final String TAG = TestThread.class.getName();

    private volatile boolean stopThread = false;

    private int times;

    public TestThread() {
    }

    public TestThread(int times) {
        this.times = times;
    }

    @Override
    public void run() {
        int count = 0;
        while (!stopThread) {
            Log.d(TAG, "StartThread count: " + count);
            count++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void interrupt() {
        this.stopThread = true;
    }
}
