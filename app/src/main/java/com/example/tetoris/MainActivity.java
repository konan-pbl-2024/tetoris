package com.example.tetoris;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button opButton = findViewById(R.id.op_button);
        opButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opActivityへの画面遷移
                Intent intent = new Intent(MainActivity.this, OpActivity.class);
                startActivity(intent);
            }
        });

        TextView textView = findViewById(R.id.taptext);
        ObjectAnimator blinkAnimator = ObjectAnimator.ofFloat(textView, View.ALPHA, 1f, 0f);
        blinkAnimator.setDuration(500); // 点滅速度
        blinkAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        blinkAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        blinkAnimator.start();

        // 画面全体をタップできるエリアを取得
        FrameLayout fullScreenTouchArea = findViewById(R.id.full_screen_touch_area);

        // 画面全体がタップされたときに遷移
        fullScreenTouchArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 画面遷移（新しいアクティビティを開く）
                Intent intent = new Intent(MainActivity.this, ItemBox.class);
                startActivity(intent);
            }
        });

        // MediaPlayerを初期化し、BGMをセット
        mediaPlayer = MediaPlayer.create(this, R.raw.main);
        mediaPlayer.setVolume(0.5f,0.5f);
        mediaPlayer.setLooping(true); // ループ再生
        mediaPlayer.start(); // 再生開始
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause(); // 一時停止
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start(); // 再開
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); // リソース解放
            mediaPlayer = null;
        }
    }

}
