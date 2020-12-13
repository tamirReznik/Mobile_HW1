package com.college.mobile_hw1.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.college.mobile_hw1.MediaManager;
import com.college.mobile_hw1.R;
import com.college.mobile_hw1.SharedPreferencesManager;
import com.college.mobile_hw1.fragments.DetailsDialog;
import com.college.mobile_hw1.general.Player;
import com.college.mobile_hw1.interfaces.callbackes.DialogInputListener;
import com.college.mobile_hw1.utils.KEYS;
import com.college.mobile_hw1.utils.Utils;
import com.google.gson.Gson;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements DialogInputListener {


    private static final int LOCATION_REFRESH_TIME = 1000;
    private static final int LOCATION_REFRESH_DISTANCE = 2;
    private static final int REQUEST_CODE = 101;
    private MediaManager mediaManager;
    private ImageView main_IMG_volume;
    private boolean isMute;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("TAG", "MainActivity onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLocation();

        initSound();

        imgSoundListener();

        playListener();

        scoreListener();

    }

    private void initLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!Utils.checkLocationPermission(this,REQUEST_CODE)){
            myLocationListener();
        }
    }

    @SuppressLint("MissingPermission")
    private void myLocationListener() {

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        SharedPreferencesManager.write(KEYS.LAT, Double.toString(location.getLatitude()));
                        SharedPreferencesManager.write(KEYS.LON, Double.toString(location.getLongitude()));
                        Log.i("onLocationChan", "onLocationChanged: "+location.getLatitude()+" lon: "+location.getLongitude());
                    }

                    @Override
                    public void onProviderEnabled(@NonNull String provider) {

                    }

                    @Override
                    public void onProviderDisabled(@NonNull String provider) {

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0)
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    myLocationListener();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TAG", "MainActivity onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            playSound();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("TAG", "MainActivity onResume: ");
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


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEYS.IS_MUTE, isMute);


    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isMute = savedInstanceState.getBoolean(KEYS.IS_MUTE);
        updateVolume();

    }


    @Override
    public void detailsInput(CharSequence name, String avatar) {

        Player player = new Player(name.toString(), avatar);
        openPlayActivity(player);
    }

    public void initSound() {
        mediaManager = MediaManager.getInstance();
        main_IMG_volume = findViewById(R.id.main_IMG_volume);
        isMute = false;
    }

    private void scoreListener() {
        findViewById(R.id.main_LBL_tableScore).setOnClickListener((View v) -> openScoreActivity());
    }

    public void playListener() {
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

    public void openScoreActivity() {
        Intent mainIntent = new Intent(MainActivity.this, ScoreActivity.class);
        startActivity(mainIntent);
    }

    public void openPlayActivity(Player player) {
        Intent mainIntent = new Intent(MainActivity.this, GameActivity.class);
        Gson gson = new Gson();
        mainIntent.putExtra(KEYS.KEY_PLAYER, gson.toJson(player));
        startActivity(mainIntent);
    }

    private void playSound() throws IOException {
        mediaManager.setSound(R.raw.elevator_music, true, false);
        updateVolume();
    }

    public void pauseSound() {
        mediaManager.pause();
    }

    private void updateVolume() {

        if (isMute) {
            main_IMG_volume.setImageResource(R.drawable.mute);
            mediaManager.setVolume(0, 0);
        } else {
            main_IMG_volume.setImageResource(R.drawable.volume_on);
            mediaManager.setVolume(1, 1);
        }
    }

}