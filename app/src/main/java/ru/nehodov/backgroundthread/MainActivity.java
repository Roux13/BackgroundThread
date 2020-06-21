package ru.nehodov.backgroundthread;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
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

    private ImageDownloadAsyncTask asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
        imageView = findViewById(R.id.imageView);

        asyncTask = new ImageDownloadAsyncTask(this);
        asyncTask.execute(URL);
    }

    @Override
    protected void onDestroy() {
        asyncTask.cancel(true);
        asyncTask.clearReference();
        super.onDestroy();
    }

    public void startThread(View view) {
        thread = new Thread(() -> {
           int count = 0;
           while (count < 10) {
               if (count == 5) {
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

    private static class ImageDownloadAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private WeakReference<MainActivity> weakReference;

        public ImageDownloadAsyncTask(MainActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        public void clearReference() {
            weakReference.clear();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Log.d(TAG, "Into method doInBackground");
            Bitmap bitmap = null;
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                Log.d(TAG, "Url passed");
                URLConnection connection = url.openConnection();
                Log.d(TAG, "URLConnection passed");
                inputStream = (InputStream) connection.getInputStream();
                Log.d(TAG, "InputStream passed");
                bitmap = BitmapFactory.decodeStream(inputStream);
                Log.d(TAG, "BitmapFactory passed");
            } catch (UnknownHostException e) {
                Log.d(TAG, "UnknownHostException");
                e.printStackTrace();
            } catch (MalformedURLException e) {
                Log.d(TAG, "MalformedURLException");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "After bitmap downloading");
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            MainActivity activity = weakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.imageView.setImageBitmap(bitmap);
        }
    }

}