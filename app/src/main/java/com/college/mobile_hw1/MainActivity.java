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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements DetailsDialog.DialogInputListener {

    private static MediaPlayer mainScreenSound = null;
    private ImageView main_IMG_volume;
    private boolean isMute;
    private DetailsDialog detailsDialog;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("TAG", "MainActivity onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSound();

        imgSoundListener();

        play();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TAG", "MainActivity onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAG", "MainActivity onResume: " + mainScreenSound.isPlaying());
        playSound();
        Utils.fullScreen(getWindow());
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseSound();
        Log.i("TAG", "MainActivity onPause: ");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("TAG", "MainActivity onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TAG", "MainActivity onDestroy: ");
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i("TAG", "onWindowFocusChanged: mainActivity");
        if (hasFocus)
            Utils.fullScreen(getWindow());
    }

    //TODO - Add a score table

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
        isMute = savedInstanceState.getBoolean("volume_on");
        updateVolume();

        if (!mainScreenSound.isPlaying())
            mainScreenSound.start();


    }

    @Override
    public void detailsInput(CharSequence name, int avatar) {
        String playerName = name.toString();
        int playerAvatar = avatar;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        openPlayActivity(MainActivity.this);
    }

    public void initSound() {
        mainScreenSound = MediaPlayer.create(getApplicationContext(), R.raw.elevator_music);
        mainScreenSound.setLooping(true);
        mainScreenSound.setAudioStreamType(AudioManager.STREAM_MUSIC);

        main_IMG_volume = findViewById(R.id.main_IMG_volume);
        isMute = false;
    }

    public void play() {
        findViewById(R.id.main_BTN_play).setOnClickListener((View v) -> {
            DetailsDialog detailsDialog = new DetailsDialog();
            detailsDialog.show(getSupportFragmentManager(), "user details");

        });
    }

    private void imgSoundListener() {
        main_IMG_volume.setOnClickListener(v -> {
            isMute = !isMute;
            updateVolume();

        });
    }

    public void openPlayActivity(Activity baseActivity) {
        Intent mainIntent = new Intent(baseActivity, GameActivity.class);
        startActivity(mainIntent);
    }

    private void playSound() {
        mainScreenSound.start();
    }

    public void pauseSound() {
        mainScreenSound.pause();
    }

    private void updateVolume() {

        if (isMute) {
            main_IMG_volume.setImageResource(R.drawable.mute);
            mainScreenSound.setVolume(0, 0);
        } else {
            main_IMG_volume.setImageResource(R.drawable.volume_on);
            mainScreenSound.setVolume(1, 1);
        }
    }
}