package com.college.mobile_hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WinnerActivity extends AppCompatActivity {


    ImageView winner_IMG_player;
    TextView winner_LBL_topMsg, winner_LBL_bottomMsg;
    MediaPlayer applauseSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);


        try {
            showGreet();
        } catch (Exception e) {
            e.printStackTrace();
        }

        backToMenuListener();
    }

    private void play_applause(boolean isDraw) {
        if (isDraw)
            this.applauseSound = MediaPlayer.create(getApplicationContext(), R.raw.disappointment_sound);
        else
            this.applauseSound = MediaPlayer.create(getApplicationContext(), R.raw.fake_applause);
        this.applauseSound.start();
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

        this.winner_IMG_player = findViewById(R.id.winner_IMG_player);
        this.winner_IMG_player.setImageResource(drawableId);

        this.winner_LBL_topMsg = findViewById(R.id.winner_LBL_topMsg);
        this.winner_LBL_bottomMsg = findViewById(R.id.winner_LBL_bottomMsg);

        boolean isDraw = getIntent().getBooleanExtra("isDraw", true);
        if (isDraw)
            this.winner_LBL_topMsg.setText(R.string.drawMsg);
        else {
            this.winner_LBL_topMsg.setText(R.string.congratsMsg);
            this.winner_LBL_bottomMsg.setText(R.string.winMsg);
        }
        play_applause(isDraw);


    }
}