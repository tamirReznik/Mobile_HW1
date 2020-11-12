package com.college.mobile_hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class GameActivity extends AppCompatActivity {

    private HashMap<Integer, Integer> cards;
    private ArrayList<Integer> keyList;
    private ImageView game_IMG_cardL, game_IMG_cardR, game_IMG_play;
    private TextView game_LBL_rightScore, game_LBL_leftScore;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initGame();

        this.game_IMG_play.setOnClickListener((View v) -> {
            playRound();
            if (keyList.isEmpty()) {
                ImageView winner;
                int leftPlayerScore = Integer.parseInt(game_LBL_rightScore.getText().toString());
                int rightPlayerScore = Integer.parseInt(game_LBL_leftScore.getText().toString());
                if (leftPlayerScore > rightPlayerScore)
                    //TODO Winning activity + update table score
                    openWinningActivity();
                finish();
            }
        });


    }

    private void initGame() {

        //Create TypeArray with all cards vectors
        TypedArray rawCardsIds = getResources().obtainTypedArray(R.array.cards);

        //Store all vectors in hashMap with id as key and number of card as value
        this.cards = new HashMap<>();
        for (int i = 0; i < rawCardsIds.length(); i++)
            cards.put(rawCardsIds.getResourceId(i, 0), (i % 13) + 1);
        rawCardsIds.recycle();

        //use Arraylist with all keys to use random key each round
        this.keyList = new ArrayList<>(cards.keySet());
        this.random = new Random();


        this.game_LBL_rightScore = findViewById(R.id.game_LBL_rightScore);
        this.game_LBL_leftScore = findViewById(R.id.game_LBL_leftScore);

        this.game_IMG_cardL = findViewById(R.id.game_IMG_cardL);
        this.game_IMG_cardR = findViewById(R.id.game_IMG_cardR);

        this.game_IMG_play = findViewById(R.id.game_IMG_play);
    }

    private void openWinningActivity() {

    }

    private void playRound() {


        int leftKey, rightKey = keyList.remove(random.nextInt(keyList.size()));
        this.game_IMG_cardR.setImageResource(rightKey);
        leftKey = keyList.remove(random.nextInt(keyList.size()));
        this.game_IMG_cardL.setImageResource(leftKey);

        if (cards.get(rightKey) > cards.get(leftKey))
            game_LBL_rightScore.setText(String.valueOf(Integer.parseInt(game_LBL_rightScore.getText().toString()) + 1));

        if (cards.get(leftKey) > cards.get(rightKey))
            game_LBL_leftScore.setText(String.valueOf(Integer.parseInt(game_LBL_leftScore.getText().toString()) + 1));


    }
}