package com.example.tetoris;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // 表示する値を受け取る
        int score = getIntent().getIntExtra("SCORE", 0);
        int level = getIntent().getIntExtra("Level", 0);
        int maxcombo = getIntent().getIntExtra("MAXCOMBO", 0);

        // スコアを表示する処理を追加
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText("Score: " + score);

        // ハイスコアを受け取る
        SharedPreferences sharedPreferences = getSharedPreferences("GAME_DATA", MODE_PRIVATE);
        int highScore = sharedPreferences.getInt("HIGH_SCORE", 0);

        // ハイスコアの更新
        if (score > highScore) {

            TextView textView = findViewById(R.id.new_record_text);
            textView.setTextColor(Color.YELLOW);
            ObjectAnimator blinkAnimator = ObjectAnimator.ofFloat(textView, View.ALPHA, 1f, 0f);
            blinkAnimator.setDuration(500); // 点滅速度
            blinkAnimator.setRepeatMode(ObjectAnimator.REVERSE);
            blinkAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            blinkAnimator.start();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.apply();

        } else {
            TextView textView = findViewById(R.id.new_record_text);
            textView.setTextColor(Color.TRANSPARENT); // 更新しないときは不可視に
        }

        // レベルを表示する処理を追加
        TextView LevelTextView = findViewById(R.id.LevelTextView);
        LevelTextView.setText("Level: " + level);

        // 最大コンボを表示する処理を追加
        TextView maxcomboTextView = findViewById(R.id.maxcomboTextView);
        maxcomboTextView.setText("MaxCombo: " + maxcombo);

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