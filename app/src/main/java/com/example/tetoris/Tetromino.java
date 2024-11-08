// Tetromino.java
package com.example.tetoris;

import android.graphics.Color;
import android.util.Log;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Tetromino {
    public int[][] shape; // 形状
    public int color; // 色
    public int x=8; // テトリミノのX位置
    public int y=3; // テトリミノのY位置
    public int rotation;
    public static final int[][][] shapes = {
            {{0, 0, 0, 0},{1, 1, 1, 1},{0, 0, 0, 0},{0, 0, 0, 0}}, // I
            {{1, 1}, {1, 1}}, // O
            {{0, 1, 0}, {1, 1, 1},{0, 0, 0}}, // T
            {{1, 1, 0}, {0, 1, 1},{0, 0, 0}}, // Z
            {{0, 1, 1}, {1, 1, 0},{0, 0, 0}}, // S
            {{1, 0, 0}, {1, 1, 1},{0, 0, 0}}, // J
            {{0, 0, 1}, {1, 1, 1},{0, 0, 0}}  // L
    };

    private static final int[] colors = {
            Color.rgb(0, 255, 255), // I - 水色
            Color.rgb(255, 255, 0), // O - 黄色
            Color.rgb(128, 0, 128), // T - 紫色
            Color.rgb(255, 0, 0),   // Z - 緑色
            Color.rgb(0, 255, 0) ,   // S - 赤色
            Color.rgb(0, 0, 255),   // J - 青色
            Color.rgb(255, 165, 0), // L - オレンジ色
    };

    // Iミノのオフセットリスト（3次元配列）
    private static final int[][][] I_MINO_OFFSETS = {
            { {0, 0}, {-2, 0}, {1, 0}, {-2, 1}, {1, -2} },  // 0 -> 1右回転
            { {0, 0}, {-1, 0}, {2, 0}, {-1, -2}, {2, 1} },  // 1 -> 2
            { {0, 0}, {2, 0}, {-1, 0}, {2, -1}, {-1, 2} },  // 2 -> 3
            { {0, 0}, {1, 0}, {-2, 0}, {1, 2}, {-2, -1} },   // 3 -> 0
            { {0, 0}, {-1, 0}, {2, 0}, {-1, -2}, {2, -1} },  // 0 -> 3左回転
            { {0, 0}, {-2, 0}, {1, 0}, {-2, 1}, {1, -2} },  // 3 -> 2
            { {0, 0}, {1, 0}, {-2, 0}, {1, 2}, {-2, -1} },  // 2 -> 1
            { {0, 0}, {2, 0}, {-1, 0}, {2, -1}, {-1, 2} }  // 1 -> 0


    };

    // 他のミノのオフセットリスト（3次元配列）
    private static final int[][][] STANDARD_MINO_OFFSETS = {
            { {0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2} },  // 0 -> 1
            { {0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2} },      // 1 -> 2
            { {0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2} },     // 2 -> 3
            { {0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2} },    // 3 -> 0
            { {0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2} },    // 0 -> 3
            { {0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2} }, // 3 -> 2
            { {0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2} }, // 2 -> 1
            { {0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2} },  // 1 -> 0

    };


    public int[][] getWallKickOffsets(int fromRotation, int toRotation,int num) {
        int offsetIndex;
        if(fromRotation==0&&toRotation==1){
            offsetIndex=0;
        } else if (fromRotation==1&&toRotation==2) {
            offsetIndex=1;
        } else if (fromRotation==2&&toRotation==3) {
            offsetIndex=2;
        } else if(fromRotation==3&&toRotation==0){
            offsetIndex=3;
        } else if(fromRotation==0&&toRotation==3){
            offsetIndex=4;
        } else if(fromRotation==3&&toRotation==2){
            offsetIndex=5;
        } else if(fromRotation==2&&toRotation==1){
            offsetIndex=6;
        } else {
            offsetIndex=7;
        }

        if (num == 1) { // Iミノの場合
            return I_MINO_OFFSETS[offsetIndex];
        } else {
            return STANDARD_MINO_OFFSETS[offsetIndex];
        }
    }


    private static List<Integer> bag = new LinkedList<>();

    public Tetromino() {
        if (bag.isEmpty()) {
            refillBag();
        }

        int index = bag.remove(0);
        shape = shapes[index];
        color = colors[index];
        this.rotation = 0;

    }

    private static void refillBag() {
        bag.clear();
        for (int i = 0; i < shapes.length; i++) {
            bag.add(i);
        }
        Collections.shuffle(bag);
    }

}
