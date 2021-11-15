package com.lab3.Audio;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    IBinder mBinder = new MyBinder();
    MediaPlayer mediaPlayer;
    ArrayList<MusicFiles> musicFiles = new ArrayList<>();
    Uri uri;
    int pos = -1;
    ActionPlaying actionPlaying;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Bind", "Method");
        return mBinder;
    }

    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int myPos = intent.getIntExtra("servicePos", -1);
        String actionName = intent.getStringExtra("ActionName");
        if (myPos != -1) {
            playMedia(myPos);
        }
        if (actionName != null) {
            switch (actionName) {
                case "playPause":
                    if (actionPlaying != null) {
                        Log.e("Inside", "Action");
                        actionPlaying.playPauseButtonClicked();
                    }
                    break;
                case "next":
                    if (actionPlaying != null) {
                        Log.e("Inside", "Action");
                        actionPlaying.nextButtonClicked();
                    }
                    break;
                case "prev":
                    if (actionPlaying != null) {
                        Log.e("Inside", "Action");
                        actionPlaying.prevButtonClicked();
                    }
                    break;
            }
        }
        return START_STICKY;
    }

    private void playMedia(int StartPos) {
        musicFiles = AudioActivity.musicFiles;
        pos = StartPos;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (mediaPlayer != null) {
                createMediaPlayer(pos);
                mediaPlayer.start();
            }
        } else {
            createMediaPlayer(pos);
            mediaPlayer.start();
        }
    }

    void start() {
        mediaPlayer.start();
    }

    boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    void stop() {
        mediaPlayer.stop();
    }

    void release() {
        mediaPlayer.release();
    }

    int getDuration() {
        return mediaPlayer.getDuration();
    }

    void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    void createMediaPlayer(int pos) {
        uri = Uri.parse(musicFiles.get(pos).getPath());
        mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
    }

    void pause() {
        mediaPlayer.pause();
    }

    void OnCompleted() {
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (actionPlaying != null) {
            actionPlaying.nextButtonClicked();
            if (mediaPlayer != null) {
                createMediaPlayer(pos);
                mediaPlayer.start();
                OnCompleted();
            }
        }
    }

    void setCallBack(ActionPlaying actionPlaying) {
        this.actionPlaying = actionPlaying;
    }
}
