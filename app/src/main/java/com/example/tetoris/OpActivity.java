package com.example.tetoris;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class OpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_op);

        Button stButton = findViewById(R.id.st_button);

        VideoView videoView = findViewById(R.id.videoView);


        // ローカル動画のURIを設定
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sample3);

        // VideoViewに動画をセット
        videoView.setVideoURI(videoUri);

        // 動画の再生
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 動画が終了したら再度再生を開始
                videoView.start();
            }
        });

        videoView.start();




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
