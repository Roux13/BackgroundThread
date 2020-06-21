package ru.nehodov.backgroundthread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private TestThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startThread(View view) {
        thread = new TestThread();
        thread.start();
//        thread = new Thread(new TestRunnable(10));
//        thread.start();
    }

    public void stopThread(View view) {
        Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
        thread.interrupt();
    }
}