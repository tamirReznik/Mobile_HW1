package com.college.mobile_hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WinnerActivity extends AppCompatActivity {
    ImageView winner_IMG_player;
    TextView winner_LBL_topMsg, winner_LBL_bottomMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);


        try {
            showGreet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        backToMenu();
    }

    private void backToMenu() {
        findViewById(R.id.winner_BTN_menu).setOnClickListener((View v) -> finish());
    }

    private void showGreet() throws Exception {
        int drawableId = getIntent().getIntExtra("drawable_id", -1);

        if (drawableId == -2)
            throw new Exception("Invalid drawable id");

        this.winner_IMG_player = findViewById(R.id.winner_IMG_player);
        this.winner_IMG_player.setImageResource(drawableId);

        this.winner_LBL_topMsg = findViewById(R.id.winner_LBL_topMsg);
        this.winner_LBL_bottomMsg = findViewById(R.id.winner_LBL_bottomMsg);

        if (getIntent().getBooleanExtra("isDraw", false))
            this.winner_LBL_topMsg.setText(R.string.drawMsg);
        else {
            this.winner_LBL_topMsg.setText(R.string.congratsMsg);
            this.winner_LBL_bottomMsg.setText(R.string.winMsg);
        }


    }
}