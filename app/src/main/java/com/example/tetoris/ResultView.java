package com.example.tetoris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ResultView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_view);
        Button gameButton= findViewById(R.id.gameButton);
        Button startButton= findViewById(R.id.stButton);
        gameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ResultView.this,GameView.class);
                startActivity(intent);


            }
    });
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ResultView.this,MainActivity.class);
                startActivity(intent);


            }
        });
    }
}