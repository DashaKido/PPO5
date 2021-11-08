package com.lab3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AudioActivity extends AppCompatActivity {

    private SeekBar volumeSeekBar = null;
    private TextView currentSongName;
    private TextView playerPosition;

    ImageView buttonRew, buttonFF;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;
    //private int pauseAtLength;
    //private String currentUrlFromStream;
    ListView listView;
    String[] items;

    ArrayList<MusicFiles> musicFiles;
    public static final int REQUEST_CODE = 1;
    RecyclerView recyclerView;
    MusicAdapter musicAdapter;
    SeekBar seekBar;
    static TextView songName, artistName, durationPlayed, durationTotal;
    static ImageView coverArt, nextButton, backButton, shuffleButton, repeatButton;
    static FloatingActionButton playPauseButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);


        //currentSongName = findViewById(R.id.songName);
        //initListView();
        //listView = findViewById(R.id.songListView);
        //runTimePermission();
        /*volumeSeekBar = findViewById(R.id.volume);
        playerPosition = findViewById(R.id.player_position);
        TextView playerDuration = findViewById(R.id.player_duration);
        seekBar = findViewById(R.id.seekBar);
        buttonRew = findViewById(R.id.button_rew);
        buttonPlay = findViewById(R.id.button_play);
        buttonPause = findViewById(R.id.button_pause);
        buttonFF = findViewById(R.id.button_ff);
        listView = findViewById(R.id.songListView);
        initAudioManager();
        runTimePermission();


        /*mediaPlayer = MediaPlayer.create(this, R.raw.minor);
        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);

            }
        };

        int duration = mediaPlayer.getDuration();
        String sDuration = convertFormat(duration);
        playerDuration.setText(sDuration);
        buttonPlay.setOnClickListener(view -> {
            buttonPlay.setVisibility(View.GONE);
            buttonPause.setVisibility(View.VISIBLE);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration());
            handler.postDelayed(runnable, 0);

        });

        buttonPause.setOnClickListener(view -> {
            buttonPause.setVisibility(View.GONE);
            buttonPlay.setVisibility(View.VISIBLE);
            mediaPlayer.pause();
            handler.removeCallbacks(runnable);
        });

        buttonFF.setOnClickListener(view -> {
            int currentPosition = mediaPlayer.getCurrentPosition();
            int duration1 = mediaPlayer.getDuration();
            if (mediaPlayer.isPlaying() && duration1 != currentPosition) {
                currentPosition = currentPosition + 5000;
                playerPosition.setText(convertFormat(currentPosition));
                mediaPlayer.seekTo(currentPosition);
            }
        });

        buttonRew.setOnClickListener(view -> {
            int currentPosition = mediaPlayer.getCurrentPosition();
            if (mediaPlayer.isPlaying() && currentPosition > 5000) {
                currentPosition = currentPosition - 5000;
                playerPosition.setText(convertFormat(currentPosition));
                mediaPlayer.seekTo(currentPosition);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
                playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaPlayer.setOnCompletionListener(mp -> {
            buttonPause.setVisibility(View.GONE);
            buttonPlay.setVisibility(View.VISIBLE);
            mediaPlayer.seekTo(0);
        });*/
        permission();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        if (!(musicFiles.size() < 1)) {
            musicAdapter = new MusicAdapter(this, musicFiles);
            recyclerView.setAdapter(musicAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
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
                //Log.e("Path: " + path, "Title: " + title);
                tempAudioList.add(musicFiles);

            }
            cursor.close();
        }
        return tempAudioList;
    }

    public static void initSong() {
        songName =
    }
    /*public void runTimePermission() {
        Dexter.withContext(this).
                withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public ArrayList<File> findSong(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();
        assert files != null;
        for (File singlefile : files) {
            if (singlefile.isDirectory() && !singlefile.isHidden()) {
                arrayList.addAll(findSong(singlefile));
            } else {
                if (singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav")) {
                    arrayList.add(singlefile);
                }
            }
        }
        return arrayList;
    }

    void displaySongs() {
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++) {
            items[i] = mySongs.get(i).getName().replace(".mp3", "").replace(".wav", "");

        }
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(myAdapter);
    }*/

    /*@SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
*/
   /* private void initListView() {
        final List<String> songList = new ArrayList<>();
        songList.add("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_700KB.mp3");
        songList.add("https://dl.espressif.com/dl/audio/ff-16b-2c-44100hz.mp3");
        songList.add("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_MG.mp3");
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView songTV = (TextView) super.getView(position, convertView, parent);
                songTV.setTag(songList.get(position));
                songTV.setTextColor(Color.parseColor("#FFFF00"));
                songTV.setTypeface(songTV.getTypeface(), Typeface.BOLD);
                songTV.setText(songList.get(position).substring(songList.get(position).lastIndexOf("/") + 1));
                songTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
                return songTV;
            }
        };
        ListView listView = (ListView) findViewById(R.id.songListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentUrlFromStream = view.getTag().toString();
                currentSongName.setText(currentUrlFromStream.substring(currentUrlFromStream.lastIndexOf("/") + 1));
                try {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                    }
                    pauseAtLength = 0;
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(currentUrlFromStream);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                            playButton.setText("Pause");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        listView.setAdapter(arrayAdapter);
    }*/

    /*private void initAudioManager() {
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
    }*/
}