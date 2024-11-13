package com.example.tetoris;

import java.io.Serializable;

public class Items implements Serializable {
    private String name;
    private int scoreMultiplier;
    private int dropSpeedModifier;

    public Items(String name, int scoreMultiplier, int dropSpeedModifier) {
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
}
