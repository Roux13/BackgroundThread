package ru.nehodov.backgroundthread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private static final String URL = "https://i.ibb.co/4dcspk7/ic-launcher.png";

    private Thread thread;

    private TextView textView;
    private ImageView imageView;

    private Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
        imageView = findViewById(R.id.imageView);

        if (isInternetAvailable()) {
            Log.d(TAG, "Internet is available");
        } else {
            Log.d(TAG, "Internet is not available");
        }

        Log.d(TAG, "Before starting thread");
        thread = new Thread(() -> {
            Log.d(TAG, "Loading thread is started");
            Bitmap bitmapFromNetwork = loadImageFromNetwork(URL);
            if (bitmapFromNetwork != null) {
                Log.d(TAG, "bitmap = " + bitmapFromNetwork.toString());
            } else {
                Log.d(TAG, "bitmap is null");
            }
            mainHandler.post(() -> {
                Log.d(TAG, "Into MainHandler");
                imageView.setImageBitmap(bitmapFromNetwork);
            });
        });
        thread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private Bitmap loadImageFromNetwork(String textUrl) {
        Log.d(TAG, "Into method loadImageNetwork");
        Bitmap bitmap = null;
        try {
            URL url = new URL(textUrl);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (UnknownHostException e) {
            Log.d(TAG, "UnknownHostException");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            Log.d(TAG, "MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "After bitmap downloading");
        return bitmap;
    }

    public void startThread(View view) {
        thread = new Thread(() -> {
           int count = 0;
           while (count < 10) {
               if (count == 5) {
//                   Handler threadHandler = new Handler(Looper.getMainLooper());
                   this.runOnUiThread(() -> textView.setText("50%"));
               }
               Log.d(TAG, "StartThread count: " + count);
               count++;
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        });
        thread.start();
    }

    public void stopThread(View view) {
        Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
        thread.interrupt();
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
}