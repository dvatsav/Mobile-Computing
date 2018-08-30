package com.example.deepak.a2_2016030;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MusicDownloadService_A2_2016030 extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String ACTION = "come.example.deepak.a2_2016030.MusicDownloadService";

    public MusicDownloadService_A2_2016030() {
        super("Music Download Service");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNet = cm.getActiveNetworkInfo();
        boolean internet = true;
        if (activeNet == null || !activeNet.isConnected())
            internet = false;
        String filename = "s1.mp3";
        String urlPath = "http://faculty.iiitd.ac.in/~mukulika/s1.mp3";
        Intent in = new Intent(ACTION);
        if (!internet) {
            in.putExtra("resultCode", 4);
            sendBroadcast(in);
            return;
        }
        FileOutputStream outputStream;
        InputStream stream = null;
        HttpURLConnection connection;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            URL url = new URL(urlPath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();
            stream = connection.getInputStream();
            int fileLength = connection.getContentLength();
            byte data[] = new byte[1024];
            int bufferLength = 0;
            while ((bufferLength = stream.read(data)) > 0) {
                outputStream.write(data, 0, bufferLength);
            }
            outputStream.flush();
            outputStream.close();
            stream.close();
            result = Activity.RESULT_OK;
            in.putExtra("resultCode", result);
            Log.i("Download:", "Successful");
        } catch (IOException i) {
            in.putExtra("resultCode", Activity.RESULT_CANCELED);
            Log.i("Download:", "Failed");
            i.printStackTrace();
        } finally {
            if (stream == null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        sendBroadcast(in);
    }
}
