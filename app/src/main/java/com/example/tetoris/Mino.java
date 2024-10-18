package com.example.tetoris;

import java.util.Random;

public class Mino {
    public int[][] shape;
    public String type;

    public Mino(String type) {
        this.type = type;
        this.shape = getShape(type);
    }

    private int[][] getShape(String type) {
        switch (type) {
            case "I":
                return new int[][]{{1, 1, 1, 1}};
            case "O":
                return new int[][]{{1, 1}, {1, 1}};
            case "T":
                return new int[][]{{0, 1, 0}, {1, 1, 1}};
            case "S":
                return new int[][]{{0, 1, 1}, {1, 1, 0}};
            case "Z":
                return new int[][]{{1, 1, 0}, {0, 1, 1}};
            case "J":
                return new int[][]{{1, 0, 0}, {1, 1, 1}};
            case "L":
                return new int[][]{{0, 0, 1}, {1, 1, 1}};
            default:
                return new int[][]{};
        }
    }

    public static Mino getRandomMino() {
        String[] types = {"I", "O", "T", "S", "Z", "J", "L"};
        Random random = new Random();
        String randomType = types[random.nextInt(types.length)];
        return new Mino(randomType);
    }
}
