package com.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showVideoPlayer(View view)
    {
        Intent intent = new Intent(MainActivity.this, VideoActivity.class);
        startActivity(intent);
    }

    public void showAudioPlayer(View view)
    {
        Intent intent = new Intent(MainActivity.this, AudioActivity.class);
        startActivity(intent);
    }
}