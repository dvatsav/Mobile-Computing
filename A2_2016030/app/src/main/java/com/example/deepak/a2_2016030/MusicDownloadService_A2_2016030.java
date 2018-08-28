package com.example.deepak.a2_2016030;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

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
        String filename = "until_you_were_gone.mp3";
        String urlPath = "https://dl45.y2mate.com/youtube/mp3/7/y2mate.com%20-%20the_chainsmokers_tritonal_until_you_were_gone_official_video_ft_emily_warren_iPAac-0IUKQ.mp3";
        Intent in = new Intent(ACTION);
        if (!internet) {
            in.putExtra("resultCode", Activity.RESULT_CANCELED);
            sendBroadcast(in);
            return;
        }
        FileOutputStream outputStream;
        InputStream stream = null;


        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            URL url = new URL(urlPath);
            stream = url.openConnection().getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            int next = -1;
            while ((next = reader.read()) != -1) {
                outputStream.write(next);
            }
            outputStream.close();
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
