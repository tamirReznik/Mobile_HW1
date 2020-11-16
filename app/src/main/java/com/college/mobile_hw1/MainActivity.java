package com.college.mobile_hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

//
//    @Override
//    protected void onPause() {
//        Log.i("info", "MainonPause");
//        super.onPause();
//    }
//
//
//
//    @Override
//    protected void onDestroy() {
//        Log.i("info", "MainonDestroy");
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onStart() {
//        Log.i("info", "MainonStart");
//        super.onStart();
//    }
//
//    @Override
//    protected void onResume() {
//        Log.i("info", "MainonResume");
//        super.onResume();
//    }
//
//    @Override
//    protected void onStop() {
//        Log.i("info", "MainonStop");
//        super.onStop();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.i("info", "MainonCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play();
        scoreTable();

    }

    private void scoreTable() {
//TODO - Create score table
    }

    public void play() {
        findViewById(R.id.main_BTN_play).setOnClickListener((View v) -> openPlayActivity(MainActivity.this));
    }

    public void openPlayActivity(Activity baseActivity) {
        Intent mainIntent = new Intent(baseActivity, GameActivity.class);
        startActivity(mainIntent);
    }
}