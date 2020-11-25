package com.college.mobile_hw1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WinnerActivity extends AppCompatActivity {


    private MediaPlayer applauseSound;
    private int soundPosition;

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TAG", "WinnerActivity onPause: ");
        this.applauseSound.pause();
        this.soundPosition = this.applauseSound.getCurrentPosition();

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
        if (this.soundPosition == -1)
            this.applauseSound.start();
        else if (this.soundPosition < this.applauseSound.getDuration()) {
            this.applauseSound.start();
            this.applauseSound.seekTo(this.soundPosition);
        }
        Utils.fullScreen(getWindow());
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
        this.applauseSound.stop();
        this.applauseSound.release();
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

    private void initApplauseSound(boolean isDraw) {
        this.soundPosition = -1;
        if (isDraw)
            this.applauseSound = MediaPlayer.create(getApplicationContext(), R.raw.disappointment_sound);
        else
            this.applauseSound = MediaPlayer.create(getApplicationContext(), R.raw.fake_applause);

    }

    private void backToMenuListener() {
//        Finish activity when back to main activity
        findViewById(R.id.winner_BTN_menu).setOnClickListener((View v) -> finish());
    }

    private void showGreet() throws Exception {
        //Extract data from previous activity - img of winner + game status (win/draw)
        int drawableId = getIntent().getIntExtra("drawable_id", -1);

        if (drawableId == -1)
            throw new Exception("Invalid drawable id");

        ImageView winner_IMG_player = findViewById(R.id.winner_IMG_player);
        winner_IMG_player.setImageResource(drawableId);

        TextView winner_LBL_topMsg = findViewById(R.id.winner_LBL_topMsg);
        TextView winner_LBL_bottomMsg = findViewById(R.id.winner_LBL_bottomMsg);

        boolean isDraw = getIntent().getBooleanExtra("isDraw", true);
        if (isDraw)
            winner_LBL_topMsg.setText(R.string.drawMsg);
        else {
            winner_LBL_topMsg.setText(R.string.congratsMsg);
            winner_LBL_bottomMsg.setText(R.string.winMsg);
        }
        initApplauseSound(isDraw);


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("media_position", applauseSound.getCurrentPosition());

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.soundPosition = savedInstanceState.getInt("media_position");

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus)
//            Utils.fullScreen(getWindow());
    }
}