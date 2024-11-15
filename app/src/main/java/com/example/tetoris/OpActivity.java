package com.example.tetoris;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class OpActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_op);
        ScrollView scrollView=findViewById(R.id.opScrollView);



        Button stButton = findViewById(R.id.st_button);

        VideoView videoView = findViewById(R.id.videoView);
        VideoView videoView2 = findViewById(R.id.videoView2);
        VideoView videoView3 = findViewById(R.id.videoView3);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0,0);
            }
        });


        //動画持ってくる
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.fastdop);
        Uri videoUri2 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.harddop);
        Uri videoUri3 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.demo);

        

        // VideoViewに動画をセット
        videoView.setVideoURI(videoUri);

        videoView2.setVideoURI(videoUri2);
        videoView3.setVideoURI(videoUri3);

        // 動画の再生
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 動画が終了したら再度再生を開始
                videoView.start();
            }
        });

        videoView.start();
        videoView2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp2) {
                // 動画が終了したら再度再生を開始
                videoView2.start();
            }
        });

        videoView2.start();
        videoView3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp2) {
                // 動画が終了したら再度再生を開始
                videoView3.start();
            }
        });

        videoView3.start();






        stButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opActivityへの画面遷移
                Intent intent = new Intent(OpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }



}
