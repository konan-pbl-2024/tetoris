package com.example.tetoris;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tetoris.GameActivity;
import com.example.tetoris.ItemManager;
import com.example.tetoris.Items;

public class ItemBox extends AppCompatActivity {
    private ItemManager itemManager;
    private Items selectedItem;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        itemManager = new ItemManager();

        Button defaultButton = findViewById(R.id.defaultButton);
        Button scoreBoosterButton = findViewById(R.id.scoreBoosterButton);
        Button scoreBoosterButton2 = findViewById(R.id.scoreBoosterButton2);
        Button speedBoosterButton = findViewById(R.id.speedBoosterButton);
        Button speedBoosterButton2 = findViewById(R.id.speedBoosterButton2);
        Button holddisButton = findViewById(R.id.holddisButton);
        Button obstacleButton = findViewById(R.id.obstacleButton);
        Button obstacleButton2 = findViewById(R.id.obstacleButton2);
        Button bossButton = findViewById(R.id.bossButton);

        defaultButton.setOnClickListener(v -> {
            selectedItem = itemManager.getItemList().get(0); // Score Boosterを選択
            startGameWithItem(R.drawable.gamebackground);
        });

        scoreBoosterButton.setOnClickListener(v -> {
            selectedItem = itemManager.getItemList().get(1); // Score Boosterを選択
            startGameWithItem(R.drawable.gamebackground);
        });

        scoreBoosterButton2.setOnClickListener(v -> {
            selectedItem = itemManager.getItemList().get(2); // Score Boosterを選択
            startGameWithItem(R.drawable.scoreb);
        });

        speedBoosterButton.setOnClickListener(v -> {
            selectedItem = itemManager.getItemList().get(3); // Speed Boosterを選択
            startGameWithItem(R.drawable.speeda);
        });

        speedBoosterButton2.setOnClickListener(v -> {
            selectedItem = itemManager.getItemList().get(4); // Speed Boosterを選択
            startGameWithItem(R.drawable.speedb);
        });

        holddisButton.setOnClickListener(v -> {
            selectedItem = itemManager.getItemList().get(5); // Speed Boosterを選択
            startGameWithItem(R.drawable.holddis);
        });

        obstacleButton.setOnClickListener(v -> {
            selectedItem = itemManager.getItemList().get(6); // Speed Boosterを選択
            startGameWithItem(R.drawable.obstacle1);
        });

        obstacleButton2.setOnClickListener(v -> {
            selectedItem = itemManager.getItemList().get(7); // Speed Boosterを選択
            startGameWithItem(R.drawable.obstcle2);
        });

        bossButton.setOnClickListener(v -> {
            selectedItem = itemManager.getItemList().get(8); // Speed Boosterを選択
            startGameWithItem(R.drawable.boss);
        });


        // MediaPlayerを初期化し、BGMをセット
        mediaPlayer = MediaPlayer.create(this, R.raw.item);
        mediaPlayer.setVolume(0.2f,0.2f);
        mediaPlayer.setLooping(true); // ループ再生
        mediaPlayer.start(); // 再生開始
    }

    private void startGameWithItem(int backgroundID) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("selectedItem", selectedItem);
        intent.putExtra("backgroundID", backgroundID);
        startActivity(intent);
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
