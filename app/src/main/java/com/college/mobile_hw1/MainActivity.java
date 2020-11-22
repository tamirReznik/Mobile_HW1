package com.college.mobile_hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer playSound = null;
    private int seekTo = -1;

    @Override
    protected void onPause() {
        super.onPause();

        if (playSound != null)
            this.seekTo = playSound.getCurrentPosition();
        this.playSound.stop();
        Log.i("TAG", "onPause:sdsd " + this.seekTo);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play();

    }

    @Override
    protected void onResume() {
        super.onResume();

            playSound(R.raw.evil_laugh);
    }

    private void playSound(int sound) {
        if (playSound == null)
            playSound = MediaPlayer.create(getApplicationContext(), sound);
        else
            playSound.setAudioSessionId(sound);

        if (playSound.isPlaying())
            playSound.stop();

        playSound.start();

    }
    //TODO - Add a score table

    public void play() {
        findViewById(R.id.main_BTN_play).setOnClickListener((View v) -> {
            playSound(R.raw.flip_card_sound);
            openPlayActivity(MainActivity.this);
        });
    }

    public void openPlayActivity(Activity baseActivity) {
        Intent mainIntent = new Intent(baseActivity, GameActivity.class);
        startActivity(mainIntent);
    }
}