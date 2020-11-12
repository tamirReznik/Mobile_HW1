package com.college.mobile_hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play();
        exit();
        scoreTable();

    }

    private void scoreTable() {
//TODO - Create score table
    }

    private void exit() {
        findViewById(R.id.main_IMG_exit).setOnClickListener((View v) -> finish());
    }

    public void play() {
        findViewById(R.id.main_BTN_play).setOnClickListener((View v) -> openPlayActivity(MainActivity.this));
    }

    public void openPlayActivity(Activity baseActivity) {
        Intent mainIntent = new Intent(baseActivity, GameActivity.class);
        startActivity(mainIntent);
    }
}