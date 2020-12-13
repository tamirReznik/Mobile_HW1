package com.college.mobile_hw1;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class MediaManager implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private static MediaManager instance;
    private final MediaPlayer mediaPlayer;
    private final Context context;
    private int currentSoundId;
    volatile boolean playPressed;
    private int currentPos;
    private boolean onRepeat;


    public static MediaManager getInstance() {
        return instance;
    }

    private MediaManager(Context context) {
        mediaPlayer = MediaPlayer.create(context.getApplicationContext(), R.raw.elevator_music);
        this.context = context.getApplicationContext();
        this.setAudioStreamType(AudioManager.STREAM_MUSIC);
        currentSoundId = R.raw.elevator_music;
    }

    public static void init(Context context) {
        if (instance == null)
            instance = new MediaManager(context);
    }

    public void setSound(int fileId, boolean isLooping, boolean onRepeat) throws IOException {

        if (fileId != currentSoundId) {

            this.onRepeat = onRepeat;
            playPressed = false;
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context.getResources().openRawResourceFd(fileId));
            mediaPlayer.setVolume(1, 1);
            setLooping(isLooping);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
            currentSoundId = fileId;
        } else if (isLooping || onRepeat)
            mediaPlayer.start();
        else if (currentPos < mediaPlayer.getDuration()) {
            mediaPlayer.seekTo(currentPos);
            mediaPlayer.start();
        }

    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    private void setLooping(boolean loop) {

        mediaPlayer.setLooping(loop);
        if (!loop)
            mediaPlayer.setOnCompletionListener(this);


    }

    public void setAudioStreamType(int streamType) {
        mediaPlayer.setAudioStreamType(streamType);
    }

    public void setVolume(float leftVol, float righVol) {
        mediaPlayer.setVolume(leftVol, righVol);
    }

    public void seekTo(int mil_sec) {
        mediaPlayer.seekTo(mil_sec);
    }

    public int getCurrentPos() {
        return mediaPlayer.getCurrentPosition();
    }

    public void play() {


        mediaPlayer.start();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void pause() {
        currentPos = mediaPlayer.getCurrentPosition();
        mediaPlayer.pause();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (!onRepeat)
            mp.start();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (!onRepeat)
            mp.reset();

    }


}
