package com.college.mobile_hw1.activities;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.college.mobile_hw1.utils.KEYS;
import com.college.mobile_hw1.MediaManager;
import com.college.mobile_hw1.R;
import com.college.mobile_hw1.SharedPreferencesManager;
import com.college.mobile_hw1.general.Player;
import com.college.mobile_hw1.utils.Utils;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.IOException;
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
    private Timer gameTimer = null;
    private boolean isGamePause;
    private MediaManager mediaManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Log.i("TAG", "gameActivity onCreate: " + getIntent().getStringExtra("player"));

        try {
            initGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TAG", "gameActivity onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAG", "gameActivity onResume: ");
        playBtnListener();
//        Utils.fullScreen(getWindow());

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TAG", "gameActivity onPause: ");

        if (gameTimer != null) {
//            isGamePause = true;
            pausePressed();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("TAG", "gameActivity onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TAG", "gameActivity onDestroy: ");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            Utils.fullScreen(getWindow());
    }


    public GameActivity() {
    }

    private void initProgressBar() {
        game_PB_progressBar = findViewById(R.id.game_PB_progressBar);
        progressCounter = 0;
        game_PB_progressBar.setProgress(progressCounter);
        progressSegments = new int[DECK_SIZE / 2];
        for (int i = 0; i < progressSegments.length; i++)
            if (i < PROGRESS_SEGMENT_3)
                progressSegments[i] = 3;
            else progressSegments[i] = 4;

    }

    private void initGame() throws IOException {

        isGamePause = true;

        initCardArray();

        findViews();

        initSound();

        initProgressBar();

    }

    private void initSound() throws IOException {
        mediaManager = MediaManager.getInstance();
        mediaManager.setSound(R.raw.flip_card_sound, false, true);
    }

    private void initCardArray() { //Create TypeArray with all cards vectors
        TypedArray rawCardsIds = getResources().obtainTypedArray(R.array.cards);

        //Store all vectors in hashMap with id as key and number of card as value
        cards = new HashMap<>();
        for (int i = 0; i < rawCardsIds.length(); i++)
            cards.put(rawCardsIds.getResourceId(i, 0), (i % 13) + 1);
        rawCardsIds.recycle();

        //use Arraylist with all keys to use random key each round
        keyList = new ArrayList<>(cards.keySet());
        random = new Random();
    }

    private void findViews() {
        game_LBL_rightScore = findViewById(R.id.game_LBL_rightScore);
        game_LBL_leftScore = findViewById(R.id.game_LBL_leftScore);

        game_IMG_cardL = findViewById(R.id.game_IMG_cardL);
        game_IMG_cardR = findViewById(R.id.game_IMG_cardR);

        game_IMG_play = findViewById(R.id.game_IMG_play);
    }

    public void playBtnListener() {
        game_IMG_play.setOnClickListener((View v) -> onPlayPauseBTNPressed());
    }

    public void onPlayPauseBTNPressed() {
        isGamePause = !isGamePause;

        if (!isGamePause)
            playPressed();
        else
            pausePressed();
    }

    private void pausePressed() {
        game_IMG_play.setImageResource(R.drawable.play_button);
        gameTimer.cancel();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEYS.KEY_IS_GAME_PAUSE, isGamePause);
        outState.putIntegerArrayList(KEYS.KEY_KEY_LIST, keyList);
        outState.putInt(KEYS.KEY_PROGRESS_COUNTER, progressCounter);
        outState.putString(KEYS.KEY_LEFT_SCORE, game_LBL_leftScore.getText().toString());
        outState.putString(KEYS.KEY_RIGHT_SCORE, game_LBL_rightScore.getText().toString());


    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        keyList = savedInstanceState.getIntegerArrayList(KEYS.KEY_KEY_LIST);
        isGamePause = savedInstanceState.getBoolean(KEYS.KEY_IS_GAME_PAUSE);
        progressCounter = savedInstanceState.getInt(KEYS.KEY_PROGRESS_COUNTER);
        game_LBL_leftScore.setText(savedInstanceState.getString(KEYS.KEY_LEFT_SCORE));
        game_LBL_rightScore.setText(savedInstanceState.getString(KEYS.KEY_RIGHT_SCORE));
        if (!isGamePause)
            playPressed();


    }

    public void playPressed() {

        game_IMG_play.setImageResource(R.drawable.pause_button);

        timerPlay();

    }

    private void timerPlay() {
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    runOnUiThread(() -> {

                        if (keyList.isEmpty()) {
                            gameOver();
                            this.cancel();
                        } else {
                            mediaManager.play();
                            playRound();
                        }
                    });

                } catch (NullPointerException e) {
                    Log.e(ERROR, "onCreate: ", e);
                }

            }
        }, 500, 1000);
    }

    private void playRound() {

        int leftKey, rightKey;
        Integer leftCard, rightCard;
        //get and remove random key from Arraylist for each side
        rightKey = keyList.remove(random.nextInt(keyList.size()));
        game_IMG_cardR.setImageResource(rightKey);
        leftKey = keyList.remove(random.nextInt(keyList.size()));
        game_IMG_cardL.setImageResource(leftKey);


        //keyList.size() == 52 , 50 , 48 , 46 .... , 2 , 0
        //extract index ->  -- , 25 , 24 , 23 .... , 1 , 0
        progressCounter += progressSegments[keyList.size() / 2];
        game_PB_progressBar.setProgress(progressCounter);


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
        game_IMG_play.setOnClickListener(null);
        //Pop a toast massage on top of the screen - game over
        Toast gameOver = Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_SHORT);
        gameOver.setGravity(Gravity.TOP, 0, 0);
        gameOver.show();

        int leftPlayerScore = Integer.parseInt(game_LBL_leftScore.getText().toString());
        int rightPlayerScore = Integer.parseInt(game_LBL_rightScore.getText().toString());

        int score;
        Player player = new Gson().fromJson(getIntent().getStringExtra(KEYS.KEY_PLAYER), Player.class);

        score = player.getAvatar().equals(getResources().getResourceEntryName(R.drawable.player_boy)) ?
                leftPlayerScore - rightPlayerScore :
                rightPlayerScore - leftPlayerScore;

        player.setScore(Math.max(score, 0));
        if (player.getScore() > 0)
            writeToDb(player);
        openWinningActivity(GameActivity.this, player.getAvatar(), score);

    }

    private void openWinningActivity(Activity baseActivity, String player, int score) {

        //Store valuable data for next activity
        Intent mainIntent = new Intent(baseActivity, WinnerActivity.class);
        mainIntent.putExtra(KEYS.KEY_DRAWABLE_FILE_NAME, player);
        mainIntent.putExtra(KEYS.KEY_SCORE, score);
        //give the user 2.5 seconds to see the final card state + game over toast massage before launch next activity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(mainIntent);
            finish();
        }, 2500);
    }

    public void writeToDb(Player player) {

//update player location when game over...
        player.setLat(Double.parseDouble(SharedPreferencesManager.read(KEYS.LAT, KEYS.DEFAULT_LAT_LON)));
        player.setLon(Double.parseDouble(SharedPreferencesManager.read(KEYS.LON, KEYS.DEFAULT_LAT_LON)));

////write player details to db
            FirebaseDatabase.getInstance().getReference().child(KEYS.KEY_DB_PLAYERS_LIST).push().setValue(player);


    }


}