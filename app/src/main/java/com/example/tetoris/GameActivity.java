package com.example.tetoris;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private TetrisView tetrisView;
    private TextView scoreTextView;
    private TextView levelTextView;
    private TextView comboTextView;
    private Button buttonLeft;   // 左ボタン
    private Button buttonRight;  // 右ボタン
    private Button buttonrightRotate; //回転ボタン
    private Button buttonleftRotate; //回転ボタン
    private Button hardDropButton; //ハードドロップボタン
    private Button fastDropButton;
    private Button HoldButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        tetrisView = findViewById(R.id.tetrisView);
        scoreTextView = findViewById(R.id.scoreTextView);
        levelTextView = findViewById(R.id.levelTextView);
        comboTextView = findViewById(R.id.comboTextView);
        // スコア表示の更新
        updateScore();
        updateLEVEL();
        updateCombo();

        // 左ボタン
        buttonLeft = findViewById(R.id.buttonLeft);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetrisView.moveLeft();
            }
        });

        // 回転ボタン
        buttonrightRotate = findViewById(R.id.buttonrightRotate);
        buttonrightRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetrisView.rightrotateBlock();
            }
        });

        // 回転ボタン
        buttonleftRotate = findViewById(R.id.buttonleftRotate);
        buttonleftRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetrisView.leftrotateBlock();
            }
        });

        // 右ボタン
        buttonRight = findViewById(R.id.buttonRight);
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetrisView.moveRight();
            }
        });

        // Fast Dropボタン
        fastDropButton = findViewById(R.id.fastDropButton);
        // ボタンの長押しリスナーを設定
        fastDropButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // ボタンが押されたとき
                        tetrisView.setFastDrop(true); // 落下速度を速くする
                        return true; // イベントを消費する
                    case MotionEvent.ACTION_UP: // ボタンが離されたとき
                        tetrisView.setFastDrop(false); // 元の速度に戻す
                        return true; // イベントを消費する
                    case MotionEvent.ACTION_CANCEL: // ボタンがキャンセルされたとき
                        tetrisView.setFastDrop(false); // 元の速度に戻す
                        return true; // イベントを消費する
                }
                return false; // 他のイベントは無視する
            }
        });

        //hard Dropボタン
        hardDropButton = findViewById(R.id.hardDropButton);
        hardDropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetrisView.hardDrop();
            }
        });

        //Holdボタン
        HoldButton = findViewById(R.id.HoldButton);
        HoldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetrisView.swapHold();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_A:
                buttonLeft.performClick();  // 左ボタンのクリック動作を呼び出し
                return true;
            case KeyEvent.KEYCODE_D:
                buttonRight.performClick();  // 右ボタンのクリック動作を呼び出し
                return true;
            case KeyEvent.KEYCODE_O:
                buttonrightRotate.performClick();  // 回転ボタンのクリック動作を呼び出し
                return true;
            case KeyEvent.KEYCODE_U:
                buttonleftRotate.performClick();
                return true;
            case KeyEvent.KEYCODE_W:
                hardDropButton.performClick();  // ハードドロップボタンのクリック動作を呼び出し
                return true;
            case KeyEvent.KEYCODE_I:
                HoldButton.performClick();  // ホールドボタンのクリック動作を呼び出し
                return true;
            case KeyEvent.KEYCODE_S:
                tetrisView.softDrop();
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    // スコアを更新するメソッド
    public void updateScore() {
        scoreTextView.setText("Score: " + tetrisView.getScore());
    }

    public void updateLEVEL() {
        levelTextView.setText("LEVEL: " + tetrisView.getlevel());
    }

    public void updateCombo() {
        comboTextView.setText("Combo: " + tetrisView.getCombo());
    }

    //結果表示
    public void showResultScreen(int score,int level,int maxcombo) {
        // 結果画面へのインテントを作成し、スコアを渡す
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("Level",level);
        intent.putExtra("MAXCOMBO",maxcombo);
        startActivity(intent);
    }

}
