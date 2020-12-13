package com.college.mobile_hw1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.college.mobile_hw1.utils.KEYS;
import com.college.mobile_hw1.MediaManager;
import com.college.mobile_hw1.R;
import com.college.mobile_hw1.utils.Utils;

import java.io.IOException;

public class WinnerActivity extends AppCompatActivity {

    private MediaManager mediaManager;
    private int soundId;


    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TAG", "WinnerActivity onPause: ");
        onPauseSound();

    }

    private void onPauseSound() {
        mediaManager.pause();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TAG", "WinnerActivity onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAG", "WinnerActivity onResume: ");

        try {
            onResumeSound();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void onResumeSound() throws IOException {

        mediaManager.setSound(soundId, false, false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("TAG", "WinnerActivity onStop: ");
    }


    @Override
    protected void onDestroy() {
        Log.i("TAG", "WinnerActivity onDestroy: ");
        super.onDestroy();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);
        Log.i("TAG", "WinnerActivity onCreate: ");

        try {
            showGreet();
        } catch (Exception e) {
            e.printStackTrace();
        }

        backToMenuListener();
    }


    private void initApplauseSound() {
        mediaManager = MediaManager.getInstance();

    }

    private void backToMenuListener() {
//        Finish activity when back to main activity
        findViewById(R.id.winner_BTN_menu).setOnClickListener((View v) -> finish());
    }

    private void showGreet() throws Exception {
        //Extract data from previous activity - img of winner + game status (win/draw)
        String avatarFileName = getIntent().getStringExtra(KEYS.KEY_DRAWABLE_FILE_NAME);

        if (avatarFileName == null)
            throw new Exception("Invalid avatar file name");

        ImageView winner_IMG_player = findViewById(R.id.winner_IMG_player);
        winner_IMG_player.setImageResource(getResources().getIdentifier(avatarFileName
                , "drawable"
                , getApplicationContext().getOpPackageName()));

        TextView winner_LBL_topMsg = findViewById(R.id.winner_LBL_topMsg);
        TextView winner_LBL_bottomMsg = findViewById(R.id.winner_LBL_bottomMsg);

        int score = getIntent().getIntExtra(KEYS.KEY_SCORE, 27);
        if (score == 27)  //invalid score
            throw new RuntimeException("invalid score");

        if (score == 0) {
            winner_LBL_topMsg.setText(R.string.drawMsg);
            soundId = R.raw.disappointment_sound;
        }

        if (score < 0) {
            winner_LBL_topMsg.setText(R.string.lost_msg);
            soundId = R.raw.fail_horn;
        }
        if (score > 0) {
            winner_LBL_topMsg.setText(R.string.congratsMsg);
            winner_LBL_bottomMsg.setText(R.string.winMsg);
            soundId = R.raw.fake_applause;
        }
        initApplauseSound();


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEYS.MEDIA_POSITION, mediaManager.getCurrentPos());
        outState.putBoolean(KEYS.IS_MUTE, mediaManager.isPlaying());

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        try {
            mediaManager.setSound(soundId, false, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            Utils.fullScreen(getWindow());
    }
}