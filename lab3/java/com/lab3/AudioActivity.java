package com.lab3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

public class AudioActivity extends AppCompatActivity implements ActionPlaying, ServiceConnection {

    ImageView buttonRew, buttonFF;
    AudioManager audioManager;
    SeekBar volumeSeekBar = null;
    static ArrayList<MusicFiles> musicFiles;
    static Uri uri;
    //static MediaPlayer mediaPlayer;
    public static final int REQUEST_CODE = 1;
    RecyclerView recyclerView;
    MusicAdapter musicAdapter;
    @SuppressLint("StaticFieldLeak")
    static SeekBar seekBar;
    @SuppressLint("StaticFieldLeak")
    static TextView songName, artistName, durationPlayed, durationTotal;
    @SuppressLint("StaticFieldLeak")
    static ImageView nextButton, prevButton, shuffleButton, repeatButton;
    static FloatingActionButton playPauseButton;
    static Handler handler = new Handler();
    static Thread playThread, prevThread, nextThread;
    static int pos = -1;
    static boolean shuffleBool = false, repeatBool = false;
    static MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        init();
        initAudioManager();
        permission();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        if (!(musicFiles.size() < 1)) {
            musicAdapter = new MusicAdapter(this, musicFiles);
            recyclerView.setAdapter(musicAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        }

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

        AudioActivity.this.runOnUiThread(new Runnable() {
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
        recyclerView = findViewById(R.id.recyclerView);
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
                pos = getRandom(musicFiles.size() - 1);
            } else if (!shuffleBool && !repeatBool) {
                pos = ((pos - 1) < 0 ? (musicFiles.size() - 1) : (pos - 1));
            }
            uri = Uri.parse(musicFiles.get(pos).getPath());
            musicService.createMediaPlayer(pos);
            metaData(uri);
            songName.setText(musicFiles.get(pos).getTitle());
            artistName.setText(musicFiles.get(pos).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            AudioActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playPauseButton.setImageResource(R.drawable.ic_pause);
            musicService.start();
        } else {
            musicService.stop();
            musicService.release();
            if (shuffleBool && !repeatBool) {
                pos = getRandom(musicFiles.size() - 1);
            } else if (!shuffleBool && !repeatBool) {
                pos = ((pos - 1) < 0 ? (musicFiles.size() - 1) : (pos - 1));
            }
            uri = Uri.parse(musicFiles.get(pos).getPath());
            musicService.createMediaPlayer(pos);
            metaData(uri);
            songName.setText(musicFiles.get(pos).getTitle());
            artistName.setText(musicFiles.get(pos).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            AudioActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
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
                pos = getRandom(musicFiles.size() - 1);
            } else if (!shuffleBool && !repeatBool) {
                pos = ((pos + 1) % musicFiles.size());
            }
            uri = Uri.parse(musicFiles.get(pos).getPath());
            musicService.createMediaPlayer(pos);
            metaData(uri);
            songName.setText(musicFiles.get(pos).getTitle());
            artistName.setText(musicFiles.get(pos).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            AudioActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playPauseButton.setImageResource(R.drawable.ic_pause);
            musicService.start();
        } else {
            musicService.stop();
            musicService.release();
            if (shuffleBool && !repeatBool) {
                pos = getRandom(musicFiles.size() - 1);
            } else if (!shuffleBool && !repeatBool) {
                pos = ((pos + 1) % musicFiles.size());
            }
            uri = Uri.parse(musicFiles.get(pos).getPath());
            musicService.createMediaPlayer(pos);
            metaData(uri);
            songName.setText(musicFiles.get(pos).getTitle());
            artistName.setText(musicFiles.get(pos).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            AudioActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
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
            musicService.pause();
            seekBar.setMax(musicService.getDuration() / 1000);
            AudioActivity.this.runOnUiThread(new Runnable() {
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
            playPauseButton.setImageResource(R.drawable.ic_pause);
            musicService.start();
            seekBar.setMax(musicService.getDuration() / 1000);
            AudioActivity.this.runOnUiThread(new Runnable() {
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

    private static String formattedTime(int mCurrentPosition) {
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

    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , REQUEST_CODE);
        } else {
            musicFiles = getAllAudio(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                musicFiles = getAllAudio(this);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        , REQUEST_CODE);
            }
        }
    }

    public static ArrayList<MusicFiles> getAllAudio(Context context) {
        ArrayList<MusicFiles> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(0);
                String duration = cursor.getString(1);
                String path = cursor.getString(2);
                String artist = cursor.getString(3);
                MusicFiles musicFiles = new MusicFiles(path, title, artist, duration);
                tempAudioList.add(musicFiles);

            }
            cursor.close();
        }
        return tempAudioList;
    }

    public static void initSong(Context mContext) {
        if (musicFiles != null) {
            playPauseButton.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(musicFiles.get(pos).getPath());
        }
        Intent intent = new Intent(mContext, MusicService.class);
        intent.putExtra("servicePos", pos);
        Service service = new Service() {
            @Nullable
            @Override
            public IBinder onBind(Intent intent) {
                return null;
            }
        };
        service.startService(intent);
    }

    public static void metaData(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int duration_total = Integer.parseInt(musicFiles.get(pos).getDuration()) / 1000;
        durationTotal.setText(formattedTime(duration_total));
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

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
        musicService = myBinder.getService();
        if (musicService != null) {
            //Toast.makeText(this, "Connected " + musicService, Toast.LENGTH_SHORT).show();
            seekBar.setMax(musicService.getDuration() / 1000);
            metaData(uri);
            songName.setText(musicFiles.get(pos).getTitle());
            artistName.setText(musicFiles.get(pos).getArtist());
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService = null;

    }
}