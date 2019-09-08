package com.example.vijay.musicplayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class player extends AppCompatActivity {
    Bundle songExtraData;
    ArrayList<File> songFileList;
    SeekBar seekBar;
    TextView songTitle;
    ImageView playButton;
    ImageView nextButton;
    ImageView prevButton;

    static MediaPlayer mediaPlayer;
    int position;

    TextView currTime;
    TextView totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        seekBar = findViewById(R.id.seekBar);
        songTitle = findViewById(R.id.songName);
        playButton = findViewById(R.id.playButton);
        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.prevButton);
        currTime = findViewById(R.id.currTime);
        totalTime = findViewById(R.id.totalTime);

        if(mediaPlayer != null){
            mediaPlayer.stop();
        }

        Intent songData = getIntent();
        songExtraData = songData.getExtras();
        songFileList = (ArrayList)songExtraData.getParcelableArrayList("songFileList");
        position = songExtraData.getInt("position",0);
        initMediaPlayer(position);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position<songFileList.size()-1){
                    position++;
                }else {
                    position=0;
                }
                initMediaPlayer(position);
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position<=0){
                    position = songFileList.size() -1;
                }else{
                    position--;
                }
                initMediaPlayer(position);
            }
        });

    }

    private void initMediaPlayer(int position){
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.reset();
        }

        String name = songFileList.get(position).getName();
        songTitle.setText(name);

        //get song title on the storage
        Uri songResourceUri = Uri.parse(songFileList.get(position).toString());

        //create media player
        mediaPlayer = MediaPlayer.create(getApplicationContext(),songResourceUri);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(mediaPlayer.getDuration());

                String totTime = createTimerLabel(mediaPlayer.getDuration());
                totalTime.setText(totTime);


                mediaPlayer.start();
                playButton.setImageResource(R.drawable.pause);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playButton.setImageResource(R.drawable.play);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar mseekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);

                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer!=null){
                    try{
                        if(mediaPlayer.isPlaying()){
                            Message message = new Message();
                            message.what = mediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            currTime.setText(createTimerLabel(msg.what));
            seekBar.setProgress(msg.what);
        }
    };

    private void play(){
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            playButton.setImageResource(R.drawable.play);
        }else{
            mediaPlayer.start();
            playButton.setImageResource(R.drawable.pause);
        }
    }

    public String createTimerLabel(int duration){
        String timerLabel = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;

        timerLabel += min + ":";
        if(sec<10){
            timerLabel+="0"+sec;
        }else{
        timerLabel += sec;}
        return timerLabel;
    }
}
