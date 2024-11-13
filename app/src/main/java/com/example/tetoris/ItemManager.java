package com.example.tetoris;

import com.example.tetoris.Items;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    private List<Items> itemList;


    public ItemManager() {
        itemList = new ArrayList<>();


        // 他のアイテムの追加
        itemList.add(new Items("default",10,10));//1,1
        itemList.add(new Items("Score Booster", 12, 8));//1.2,1.2
        itemList.add(new Items("Score Booster2", 20, 5));//2.0,1.5
        itemList.add(new Items("Speed Booster", 9, 12));//0.8,0.8
        itemList.add(new Items("Speed Booster2", 8, 15));//0.6,0.5
    }


    public List<Items> getItemList() {
        return itemList;
    }
}
