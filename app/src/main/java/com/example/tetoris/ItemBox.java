package com.example.tetoris;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tetoris.GameActivity;
import com.example.tetoris.ItemManager;
import com.example.tetoris.Items;

public class ItemBox extends AppCompatActivity {
    private ItemManager itemManager;
    private Items selectedItem;

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

        defaultButton.setOnClickListener(v -> {
            selectedItem = itemManager.getItemList().get(0); // Score Boosterを選択
            startGameWithItem();
        });

        scoreBoosterButton.setOnClickListener(v -> {
            selectedItem = itemManager.getItemList().get(1); // Score Boosterを選択
            startGameWithItem();
        });

        scoreBoosterButton2.setOnClickListener(v -> {
            selectedItem = itemManager.getItemList().get(2); // Score Boosterを選択
            startGameWithItem();
        });

        speedBoosterButton.setOnClickListener(v -> {
            selectedItem = itemManager.getItemList().get(3); // Speed Boosterを選択
            startGameWithItem();
        });

        speedBoosterButton2.setOnClickListener(v -> {
            selectedItem = itemManager.getItemList().get(4); // Speed Boosterを選択
            startGameWithItem();
        });
    }

    private void startGameWithItem() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("selectedItem", selectedItem);
        startActivity(intent);
    }
}
