package com.example.tetoris;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // スコアを受け取る
        int score = getIntent().getIntExtra("SCORE", 0);

        // スコアを表示する処理を追加
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText("Score: " + score);

        // レベルを受け取る
        int Level = getIntent().getIntExtra("LEVEL", 0);

        // レベルを表示する処理を追加
        TextView LevelTextView = findViewById(R.id.LevelTextView);
        LevelTextView.setText("Level: " + Level);

        // 最大コンボを受け取る
        //int maxcombo = getIntent().getIntExtra("MAXCOMBO", 0);

        // 最大コンボを表示する処理を追加
        //TextView maxcomboTextView = findViewById(R.id.maxcomboTextView);
        //LevelTextView.setText("MaxCombo: " + maxcombo);

        // 再スタートボタンの設定
        Button restartButton = findViewById(R.id.restart_button);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivityに戻る
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // 現在のアクティビティを終了
            }
        });
    }

}