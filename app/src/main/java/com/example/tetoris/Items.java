package com.example.tetoris;

public class Items {
    private String name; // アイテムの名前
    private double scoreMultiplier; // スコア補正倍率
    private double dropSpeedModifier; // 落下速度補正

    // コンストラクタ
    public Items(String name, double scoreMultiplier, double dropSpeedModifier) {
        this.name = name;
        this.scoreMultiplier = scoreMultiplier;
        this.dropSpeedModifier = dropSpeedModifier;
    }


    // ゲッター
    public String getName() {
        return name;
    }

    public double getScoreMultiplier() {
        return scoreMultiplier;
    }

    public double getDropSpeedModifier() {
        return dropSpeedModifier;
    }

    // 他のアイテムメソッドを追加してもよい
    // 例: アイテム効果の適用や説明を返すメソッド
}
/*tetrisviewに追加する用
public class Game {
    private ItemManager itemManager;

    public Game() {
        itemManager = new ItemManager();
        // 選択されたアイテムを使用する場合
        Items selectedItem = itemManager.getItemList().get(0); // 例：最初のアイテムを選択
        applyItemEffect(selectedItem);
    }

    private void applyItemEffect(Items item) {
        double newScoreMultiplier = item.getScoreMultiplier();
        double newDropSpeed = item.getDropSpeedModifier();
        // スコアや落下速度に適用
    }
}
 */

