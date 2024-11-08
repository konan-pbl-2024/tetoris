package com.example.tetoris;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_op);

        Button stButton = findViewById(R.id.st_button);

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
