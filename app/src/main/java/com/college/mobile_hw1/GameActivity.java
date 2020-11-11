package com.college.mobile_hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Random;


public class GameActivity extends AppCompatActivity {
    static final char[] cardsType = {'d', 'c', 's', 'h'};
    private ArrayList<Integer> cards;
    private TypedArray rawCardsIds;
    private ImageView game_IMG_cardL, game_IMG_cardR;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        rawCardsIds = getResources().obtainTypedArray(R.array.cards);
        cards = new ArrayList<>();
        for (int i = 0; i < rawCardsIds.length(); i++)
            cards.add(rawCardsIds.getResourceId(i, 0));

        random = new Random();


        this.game_IMG_cardL = findViewById(R.id.game_IMG_cardL);
        this.game_IMG_cardR = findViewById(R.id.game_IMG_cardR);

        ImageView game_IMG_play = findViewById(R.id.game_IMG_play);
        game_IMG_play.setOnClickListener((View v) -> {
            playRound();
            if (cards.isEmpty()) {
                //TODO Winning activity + update table score
                finish();
            }
        });


    }

    private void playRound() {
        
        Log.i("TAG", "playRoundRrRrR: "+this.game_IMG_cardR.toString());
        this.game_IMG_cardR.setImageResource(cards.remove(random.nextInt(cards.size())));
        Log.i("TAG", "playRoundLlLlL: "+this.game_IMG_cardR.toString());
        this.game_IMG_cardL.setImageResource(cards.remove(random.nextInt(cards.size())));


    }
}