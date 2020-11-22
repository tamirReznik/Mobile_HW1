package com.college.mobile_hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {


    private static final int DECK_SIZE = 52;
    private static final int PROGRESS_SEGMENT_3 = 4;
    static final String ERROR = "Error";
    private HashMap<Integer, Integer> cards;
    private ArrayList<Integer> keyList;
    private ImageView game_IMG_cardL, game_IMG_cardR, game_IMG_play;
    private TextView game_LBL_rightScore, game_LBL_leftScore;
    private Random random;
    private ProgressBar game_PB_progressBar;
    private int[] progressSegments;
    private int progressCounter;
    private Timer gameTimer;
    boolean isPaused;

    public GameActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initGame();

        playBtnListener();
    }

    private void initProgressBar() {
        this.game_PB_progressBar = findViewById(R.id.game_PB_progressBar);
        this.progressCounter = 0;
        this.game_PB_progressBar.setProgress(this.progressCounter);
        this.progressSegments = new int[DECK_SIZE / 2];
        for (int i = 0; i < progressSegments.length; i++)
            if (i < PROGRESS_SEGMENT_3)
                this.progressSegments[i] = 3;
            else this.progressSegments[i] = 4;

    }

    private void initGame() {

        isPaused = true;

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

        initProgressBar();

    }

    public void playBtnListener() {


        this.game_IMG_play.setOnClickListener((View v) -> {
            this.isPaused = !isPaused;

            if (!this.isPaused)
                playPressed();
            else {
                this.game_IMG_play.setImageResource(R.drawable.play_button);
                gameTimer.cancel();
            }



        });
    }

    public void playPressed() {

        this.game_IMG_play.setImageResource(R.drawable.pause_button);

        this.gameTimer = new Timer();
        this.gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    runOnUiThread(() -> {
                        playRound();
                        if (keyList.isEmpty()) {
                            gameOver();
                            this.cancel();
                        }
                    });

                } catch (NullPointerException e) {
                    Log.e(ERROR, "onCreate: ", e);
                }

            }
        }, 500, 2000);


    }

    private void playRound() {

        int leftKey, rightKey;
        Integer leftCard, rightCard;

        //get and remove random key from Arraylist for each side
        rightKey = keyList.remove(random.nextInt(keyList.size()));
        this.game_IMG_cardR.setImageResource(rightKey);
        leftKey = keyList.remove(random.nextInt(keyList.size()));
        this.game_IMG_cardL.setImageResource(leftKey);

        //keyList.size() == 52 , 50 , 48 , 46 .... , 2 , 0
        //extract index ->  -- , 25 , 24 , 23 .... , 1 , 0
        this.progressCounter += progressSegments[keyList.size() / 2];
        this.game_PB_progressBar.setProgress(this.progressCounter);


        //safety check for null from the Hashmap
        if ((leftCard = cards.get(leftKey)) != null && (rightCard = cards.get(rightKey)) != null) {
            if (leftCard < rightCard)
                game_LBL_rightScore.setText(String.valueOf(Integer.parseInt(game_LBL_rightScore.getText().toString()) + 1));

            if (leftCard > rightCard)
                game_LBL_leftScore.setText(String.valueOf(Integer.parseInt(game_LBL_leftScore.getText().toString()) + 1));
        } else
            throw new NullPointerException("playRound: leftCard or right cards is null");
    }

    public void gameOver() {

        //Disable the play button when game is over
        this.game_IMG_play.setOnClickListener(null);
        //Pop a toast massage on top of the screen - game over
        Toast gameOver = Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_SHORT);
        gameOver.setGravity(Gravity.TOP, 0, 0);
        gameOver.show();

        int leftPlayerScore = Integer.parseInt(game_LBL_leftScore.getText().toString());
        int rightPlayerScore = Integer.parseInt(game_LBL_rightScore.getText().toString());

        if (leftPlayerScore > rightPlayerScore)
            openWinningActivity(GameActivity.this, R.drawable.player_boy, false);
        if (leftPlayerScore < rightPlayerScore)
            openWinningActivity(GameActivity.this, R.drawable.player_girl, false);
        if (leftPlayerScore == rightPlayerScore)
            openWinningActivity(GameActivity.this, R.drawable.draw, true);
    }

    private void openWinningActivity(Activity baseActivity, int winner, boolean isDraw) {

        //Store valuable data for next activity
        Intent mainIntent = new Intent(baseActivity, WinnerActivity.class);
        mainIntent.putExtra("drawable_id", winner);
        mainIntent.putExtra("isDraw", isDraw);
        //give the user 2.5 seconds to see the final card state + game over toast massage before launch next activity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(mainIntent);
            finish();
        }, 2500);

    }
}