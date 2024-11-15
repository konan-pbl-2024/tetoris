package com.example.tetoris;

import java.io.Serializable;

public class Items implements Serializable {
    private String name;
    private int scoreMultiplier;
    private int dropSpeedModifier;
    private boolean holddis;
    private boolean obstacle;


    public Items(String name, int scoreMultiplier, int dropSpeedModifier,boolean holddis,boolean obstacle) {
        this.name = name;
        this.scoreMultiplier = scoreMultiplier;
        this.dropSpeedModifier = dropSpeedModifier;
        this.holddis = holddis;
        this.obstacle = obstacle;
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

    // getter メソッドを追加
    public boolean isHoldDisabled() {
        return holddis;
    }

    public boolean isGenerateObstacle() {
        return obstacle;
    }
}
