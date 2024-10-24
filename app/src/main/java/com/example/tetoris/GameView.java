package com.example.tetoris;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class GameView extends AppCompatActivity {
    View gameView=null;//出てこない

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       Button resultButton= findViewById(R.id.button);


        setContentView(R.layout.activity_game_view);
        gameView=new Surface(this);//でてこない前に
        setContentView(gameView);




    }




}