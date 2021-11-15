package com.lab3.Audio;

import static com.lab3.Audio.ApplicationClass.ACTION_NEXT;
import static com.lab3.Audio.ApplicationClass.ACTION_PLAY;
import static com.lab3.Audio.ApplicationClass.ACTION_PREV;
import static com.lab3.Audio.ApplicationClass.CHANNEL_ID_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lab3.R;

import java.util.ArrayList;
import java.util.Random;

public class PlayerActivity extends AppCompatActivity implements ActionPlaying, ServiceConnection {
    TextView songName, artistName, durationPlayed, durationTotal;
    ImageView nextButton, prevButton, shuffleButton, repeatButton, buttonRew, buttonFF;
    FloatingActionButton playPauseButton;
    SeekBar seekBar;
    SeekBar volumeSeekBar = null;
    int pos = -1;
    static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    Uri uri;
    MusicService musicService;
    AudioManager audioManager;
    Handler handler = new Handler();
    boolean shuffleBool = false, repeatBool = false;
    Thread playThread, prevThread, nextThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        init();
        getIntentMethod();
        initAudioManager();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (musicService != null && fromUser) {
                    musicService.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (musicService != null) {
                    int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    durationPlayed.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });

        buttonFF.setOnClickListener(view -> {
            int currentPosition = musicService.getCurrentPosition();
            int duration = musicService.getDuration();
            if (musicService.isPlaying() && duration != currentPosition) {
                currentPosition = currentPosition + 5000;
                durationPlayed.setText(formattedTime(currentPosition / 1000));
                musicService.seekTo(currentPosition);
            }
        });

        buttonRew.setOnClickListener(view -> {
            int currentPosition = musicService.getCurrentPosition();
            if (musicService.isPlaying() && currentPosition > 5000) {
                currentPosition = currentPosition - 5000;
                durationPlayed.setText(formattedTime(currentPosition / 1000));
                musicService.seekTo(currentPosition);
            }
        });

        shuffleButton.setOnClickListener(view -> {
            if (shuffleBool) {
                shuffleBool = false;
                shuffleButton.setImageResource(R.drawable.ic_shuffle_off);
            } else {
                shuffleBool = true;
                shuffleButton.setImageResource(R.drawable.ic_shuffle_on);
            }
        });

        repeatButton.setOnClickListener(view -> {
            if (repeatBool) {
                repeatBool = false;
                repeatButton.setImageResource(R.drawable.ic_repeat_off);
            } else {
                repeatBool = true;
                repeatButton.setImageResource(R.drawable.ic_repeat_on);
            }

        });

    }

    private void getIntentMethod() {
        pos = getIntent().getIntExtra("pos", -1);
        listSongs = AudioActivity.musicFiles;
        if (listSongs != null) {
            playPauseButton.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(listSongs.get(pos).getPath());
        }
        showNotification(R.drawable.ic_pause);
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("servicePos", pos);
        startService(intent);
    }

    private void init() {
        songName = findViewById(R.id.songName);
        artistName = findViewById(R.id.songArtist);
        durationPlayed = findViewById(R.id.player_position);
        durationTotal = findViewById(R.id.player_duration);
        nextButton = findViewById(R.id.button_next);
        prevButton = findViewById(R.id.button_previous);
        shuffleButton = findViewById(R.id.button_random);
        repeatButton = findViewById(R.id.button_loop);
        playPauseButton = findViewById(R.id.button_play);
        seekBar = findViewById(R.id.seekBar);
        volumeSeekBar = findViewById(R.id.volume);
        buttonRew = findViewById(R.id.button_rew);
        buttonFF = findViewById(R.id.button_ff);
    }

    private void initAudioManager() {
        try {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

            volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formattedTime(int mCurrentPosition) {
        String totalOut;
        String totalNew;
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }

    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
        playThreadButton();
        nextThreadButton();
        prevThreadButton();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    private void prevThreadButton() {
        prevThread = new Thread() {
            @Override
            public void run() {
                super.run();
                prevButton.setOnClickListener(view -> prevButtonClicked());
            }
        };
        prevThread.start();
    }

    public void prevButtonClicked() {
        if (musicService == null) {
            Toast.makeText(this, "Нужно выбрать песню", Toast.LENGTH_LONG).show();
        }
        if (musicService.isPlaying()) {
            musicService.stop();
            musicService.release();
            if (shuffleBool && !repeatBool) {
                pos = getRandom(listSongs.size() - 1);
            } else if (!shuffleBool && !repeatBool) {
                pos = ((pos - 1) < 0 ? (listSongs.size() - 1) : (pos - 1));
            }
            uri = Uri.parse(listSongs.get(pos).getPath());
            musicService.createMediaPlayer(pos);
            metaData(uri);
            songName.setText(listSongs.get(pos).getTitle());
            artistName.setText(listSongs.get(pos).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.OnCompleted();
            showNotification(R.drawable.ic_pause);
            playPauseButton.setImageResource(R.drawable.ic_pause);
            musicService.start();
        } else {
            musicService.stop();
            musicService.release();
            if (shuffleBool && !repeatBool) {
                pos = getRandom(listSongs.size() - 1);
            } else if (!shuffleBool && !repeatBool) {
                pos = ((pos - 1) < 0 ? (listSongs.size() - 1) : (pos - 1));
            }
            uri = Uri.parse(listSongs.get(pos).getPath());
            musicService.createMediaPlayer(pos);
            metaData(uri);
            songName.setText(listSongs.get(pos).getTitle());
            artistName.setText(listSongs.get(pos).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.OnCompleted();
            showNotification(R.drawable.ic_play);
            playPauseButton.setImageResource(R.drawable.ic_play);
        }
    }

    private void nextThreadButton() {
        nextThread = new Thread() {
            @Override
            public void run() {
                super.run();
                nextButton.setOnClickListener(view -> nextButtonClicked());
            }
        };
        nextThread.start();
    }

    public void nextButtonClicked() {
        if (musicService == null) {
            Toast.makeText(this, "Нужно выбрать песню", Toast.LENGTH_LONG).show();
        }
        if (musicService.isPlaying()) {
            musicService.stop();
            musicService.release();
            if (shuffleBool && !repeatBool) {
                pos = getRandom(listSongs.size() - 1);
            } else if (!shuffleBool && !repeatBool) {
                pos = ((pos + 1) % listSongs.size());
            }
            uri = Uri.parse(listSongs.get(pos).getPath());
            musicService.createMediaPlayer(pos);
            metaData(uri);
            songName.setText(listSongs.get(pos).getTitle());
            artistName.setText(listSongs.get(pos).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.OnCompleted();
            showNotification(R.drawable.ic_pause);
            playPauseButton.setImageResource(R.drawable.ic_pause);
            musicService.start();
        } else {
            musicService.stop();
            musicService.release();
            if (shuffleBool && !repeatBool) {
                pos = getRandom(listSongs.size() - 1);
            } else if (!shuffleBool && !repeatBool) {
                pos = ((pos + 1) % listSongs.size());
            }
            uri = Uri.parse(listSongs.get(pos).getPath());
            musicService.createMediaPlayer(pos);
            metaData(uri);
            songName.setText(listSongs.get(pos).getTitle());
            artistName.setText(listSongs.get(pos).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.OnCompleted();
            showNotification(R.drawable.ic_play);
            playPauseButton.setImageResource(R.drawable.ic_play);
        }
    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i + 1);
    }

    private void playThreadButton() {
        playThread = new Thread() {
            @Override
            public void run() {
                super.run();
                playPauseButton.setOnClickListener(view -> playPauseButtonClicked());
            }
        };
        playThread.start();
    }

    public void playPauseButtonClicked() {
        if (musicService == null) {
            Toast.makeText(this, "Нужно выбрать песню", Toast.LENGTH_LONG).show();
        } else if (musicService.isPlaying()) {
            playPauseButton.setImageResource(R.drawable.ic_play);
            showNotification(R.drawable.ic_play);
            musicService.pause();
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        } else {
            showNotification(R.drawable.ic_pause);
            playPauseButton.setImageResource(R.drawable.ic_pause);
            musicService.start();
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }

    public void metaData(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int duration_total = Integer.parseInt(listSongs.get(pos).getDuration()) / 1000;
        durationTotal.setText(formattedTime(duration_total));
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
        musicService = myBinder.getService();
        musicService.setCallBack(this);
        if (musicService != null) {
            musicService.OnCompleted();
            seekBar.setMax(musicService.getDuration() / 1000);
            metaData(uri);
            songName.setText(listSongs.get(pos).getTitle());
            artistName.setText(listSongs.get(pos).getArtist());
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService = null;
    }

    void showNotification(int playPauseButton) {
        Intent intent = new Intent(this, PlayerActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent contentIntent =
                PendingIntent.getActivity(this, 0, intent, 0);

        Intent prevIntent = new Intent(this, NotificationReceiver.class).
                setAction(ACTION_PREV);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent prevPending =
                PendingIntent.getBroadcast(this, 0, prevIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent = new Intent(this, NotificationReceiver.class).
                setAction(ACTION_PLAY);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pausePending =
                PendingIntent.getBroadcast(this, 0, pauseIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent(this, NotificationReceiver.class).
                setAction(ACTION_NEXT);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent nextPending =
                PendingIntent.getBroadcast(this, 0, nextIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap picture;
        picture = BitmapFactory.decodeResource(getResources(), R.drawable.ic_music_note);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setSmallIcon(playPauseButton)
                .setLargeIcon(picture)
                .setContentTitle(listSongs.get(pos).getTitle())
                .setContentText(listSongs.get(pos).getArtist())
                .addAction(R.drawable.ic_previous, "Prev", prevPending)
                .addAction(playPauseButton, "Pause", pausePending)
                .addAction(R.drawable.ic_next, "Next", nextPending)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

}