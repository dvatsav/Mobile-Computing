package com.example.deepak.a2_2016030;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.MediaController;
import android.widget.Toast;

public class MainActivity_A2_2016030 extends AppCompatActivity implements MediaController.MediaPlayerControl{

    private Intent playIntent;
    protected static MusicPlayerService_A2_2016030 musicService;
    private boolean bound;
    protected MediaController controller;


    private ServiceConnection musicPlayerConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlayerService_A2_2016030.MusicBinder binder = (MusicPlayerService_A2_2016030.MusicBinder)service;
            musicService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
            String resultValue;
            if (resultCode == RESULT_OK) {
                resultValue = "Song Successfully Downloaded";
            } else {
                resultValue = "Unable to Download Song, Maybe no internet";
            }
            Toast.makeText(MainActivity_A2_2016030.this, resultValue, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MusicListFragment_A2_2016030 musicList = new MusicListFragment_A2_2016030();
        getSupportFragmentManager().beginTransaction().add(R.id.container, musicList).commit();
        View root = findViewById(R.id.container);
        ViewTreeObserver vto = root.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setController();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicPlayerService_A2_2016030.class);
            bindService(playIntent, musicPlayerConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(MusicDownloadService_A2_2016030.ACTION);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        controller.hide();
    }

    @Override
    protected void onDestroy() {
        if (bound)
            unbindService(musicPlayerConnection);
        stopService(playIntent);
        musicService = null;
        controller.hide();
        controller = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (bound)
            unbindService(musicPlayerConnection);
        stopService(playIntent);
        controller.hide();
        musicService = null;
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.download) {
            Intent intent = new Intent(this, MusicDownloadService_A2_2016030.class);
            startService(intent);
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void setController() {
        controller = new MusicController_A2_2016030(this);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.musicList));
        controller.setEnabled(true);
        controller.show();
    }

    private void playNext() {
        musicService.playNext();
        controller.show(0);
    }

    private void playPrev() {
        musicService.playPrev();
        controller.show(0);
    }

    @Override
    public void start() {
        musicService.go();
    }

    @Override
    public void pause() {
        musicService.pausePlayer();
    }

    @Override
    public int getDuration() {
        if (musicService != null && bound && musicService.isPng()) {
            return musicService.getDur();
        } else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (musicService != null && bound && musicService.isPng()) {
            return musicService.getPosn();
        } else {
            return 0;
        }
    }

    @Override
    public void seekTo(int pos) {
        musicService.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if (musicService != null && bound) {
            return musicService.isPng();
        } else return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

}
