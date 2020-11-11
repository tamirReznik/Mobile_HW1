package com.college.mobile_hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
        ImageView main_IMG_exit = findViewById(R.id.main_IMG_exit);
        main_IMG_exit.setOnClickListener((View v) -> {
            finish();
        });
    }

    public void play() {

        Button main_BTN_play = findViewById(R.id.main_BTN_play);
        main_BTN_play.setOnClickListener((View v) -> {
            openPlayActivity(MainActivity.this);
        });
    }

    public void openPlayActivity(Activity baseActivity) {
        Intent mainIntent = new Intent(baseActivity, GameActivity.class);
        startActivity(mainIntent);
    }
}