package com.example.tetoris;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    private List<Items> itemList;

    public ItemManager() {
        itemList = new ArrayList<>();

        // インスタンスを生成してリストに追加
        Items scoreBooster = new Items("Score Booster", 1.5, 1.0);
        Items speedBooster = new Items("Speed Booster", 1.0, 0.8);

        itemList.add(scoreBooster); // 作成したインスタンスをリストに追加
        itemList.add(speedBooster);
    }

    public List<Items> getItemList() {
        return itemList;
    }
}
