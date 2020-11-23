package com.college.mobile_hw1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static MediaPlayer mainScreenSound = null;
    private ImageView main_IMG_volume;
    private boolean isMute;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TAG", "MainActivity onDestroy: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("TAG", "MainActivity onStop: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TAG", "MainActivity onStart: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseSound();
        Log.i("TAG", "MainActivity onPause: ");

    }

    public void pauseSound() {
        mainScreenSound.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAG", "MainActivity onResume: " + mainScreenSound.isPlaying());
        playSound();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("TAG", "MainActivity onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSound();
        imgSoundListener();
        play();

    }

    public void initSound() {
        mainScreenSound = MediaPlayer.create(getApplicationContext(), R.raw.elevator_music);
        mainScreenSound.setLooping(true);
        mainScreenSound.setAudioStreamType(AudioManager.STREAM_MUSIC);

        this.main_IMG_volume = findViewById(R.id.main_IMG_volume);
        this.isMute = false;
    }

    private void playSound() {
        mainScreenSound.start();
    }
    //TODO - Add a score table

    public void play() {
        findViewById(R.id.main_BTN_play).setOnClickListener((View v) -> openPlayActivity(MainActivity.this));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("volume_on", isMute);
        outState.putInt("media_position", mainScreenSound.getCurrentPosition());

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mainScreenSound.seekTo(savedInstanceState.getInt("media_position"));
        this.isMute = savedInstanceState.getBoolean("volume_on");
        updateVolume();

        if (!mainScreenSound.isPlaying())
            mainScreenSound.start();
    }

    private void updateVolume() {

        if (this.isMute) {
            this.main_IMG_volume.setImageResource(R.drawable.mute);
            mainScreenSound.setVolume(0, 0);
        } else {
            this.main_IMG_volume.setImageResource(R.drawable.volume_on);
            mainScreenSound.setVolume(1, 1);
        }
    }

    private void imgSoundListener() {
        this.main_IMG_volume.setOnClickListener(v -> {
            this.isMute = !this.isMute;
            updateVolume();

        });
    }

    public void openPlayActivity(Activity baseActivity) {
        Intent mainIntent = new Intent(baseActivity, GameActivity.class);
        startActivity(mainIntent);
    }
}