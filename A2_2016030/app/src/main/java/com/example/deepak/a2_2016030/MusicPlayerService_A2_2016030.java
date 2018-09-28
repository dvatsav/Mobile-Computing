package com.example.deepak.a2_2016030;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

public class MusicPlayerService_A2_2016030 extends Service implements MediaPlayer.OnPreparedListener,
                                                    MediaPlayer.OnErrorListener,
                                                    MediaPlayer.OnCompletionListener {
    private MediaPlayer musicPlayer;
    private IBinder musicBind = new MusicBinder();
    private String songTitle;
    private int songNumber;
    private int listSize;

    public void onCreate() {
        super.onCreate();
        musicPlayer = new MediaPlayer();
        musicPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        musicPlayer.setOnPreparedListener(this);
        musicPlayer.setOnCompletionListener(this);
        musicPlayer.setOnErrorListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        musicPlayer.stop();
        musicPlayer.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (musicPlayer.getCurrentPosition() > 0) {
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

        Intent notifIntent = new Intent(this, MainActivity_A2_2016030.class);
        notifIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(getApplicationContext(), 0,
                notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play)
                .setOngoing(true)
                .setTicker(songTitle)
                .setContentTitle("Playing: " + songTitle);
        Notification notif = builder.build();

        startForeground(1, notif);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    public void playSong(Uri songUri, int songNumber, int listSize) {
        musicPlayer.reset();
        this.songNumber = songNumber;
        this.listSize = listSize;

        songTitle = MusicListFragment_A2_2016030.songNames.get(songNumber);

        try {
            musicPlayer.setDataSource(getApplicationContext(), songUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        musicPlayer.prepareAsync();

    }

    public class MusicBinder extends Binder {
        MusicPlayerService_A2_2016030 getService() {
            return MusicPlayerService_A2_2016030.this;
        }
    }

    public int getPosn() {
        return musicPlayer.getCurrentPosition();
    }

    public int getDur() {
        return musicPlayer.getDuration();
    }

    public boolean isPng(){
        return musicPlayer.isPlaying();
    }

    public void pausePlayer(){
        musicPlayer.pause();
    }

    public void seek(int posn){
        musicPlayer.seekTo(posn);
    }

    public void go(){
        musicPlayer.start();
    }

    public void playPrev(){
        songNumber--;
        if(songNumber < 0) {
            songNumber = listSize - 1;
        }
        Uri songUri;
        if (songNumber < MusicListFragment_A2_2016030.rawSongCount) {
            songUri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + MusicListFragment_A2_2016030.songNames.get(songNumber).replace(" ", "_"));
        } else {
            songUri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + MusicListFragment_A2_2016030.songNames.get(MusicListFragment_A2_2016030.rawSongCount - 1).replace(" ", "_"));
        }
        playSong(songUri, songNumber, listSize);
    }

    public void playNext(){
        songNumber++;
        if (songNumber >= listSize) {
            songNumber = 0;
        }
        Uri songUri;
        if (songNumber < MusicListFragment_A2_2016030.rawSongCount) {
            songUri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + MusicListFragment_A2_2016030.songNames.get(songNumber).replace(" ", "_"));
        } else {
            songUri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + MusicListFragment_A2_2016030.songNames.get(0).replace(" ", "_"));
        }
        playSong(songUri, songNumber, listSize);
    }
}
