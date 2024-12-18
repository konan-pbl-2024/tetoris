//tetrisView
package com.example.tetoris;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class TetrisView extends SurfaceView implements SurfaceHolder.Callback {
    private Tetromino currentTetromino; // 現在のテトリミノ
    private int[][] board; // ゲームボード
    private Paint paint;
    private static final int NUM_COLUMNS = 15; // テトリスの列数
    private static final int NUM_ROWS = 25; // テトリスの行数
    private int cellSize;
    private static final long FALL_INTERVAL = 750; // 1秒ごとに落ちる
    private Handler handler = new Handler();
    private int score = 0;// スコアを保持する変数
    private boolean isFastDropping = false; // ミノを早く落とすフラグ
    private long fallInterval; // 実際の落下間隔
    private boolean isHardDropping = false; // ハードドロップ中かどうかのフラグ
    private static final int NEXT_TETROMINOES_COUNT = 5; // 表示する次のミノの数
    private Queue<Tetromino> nextTetrominoes = new LinkedList<>(); // 次のミノのキュー
    private int scale;//ネクストの大きさ調整
    private int stand;//ネクストの位置補正
    private int rowsCleared;
    //ホールドミノ用宣言
    private Tetromino holdMino = null;
    private boolean hasSwapped = false;
    private int swapcount = 0;
    //接地時の猶予時間用宣言
    private boolean isInGracePeriod = false; // 猶予中かどうかを示すフラグ
    private long graceStartTime; // 猶予の開始時間
    private static final long GRACE_PERIOD = 10; // 0.1秒
    //レベル機能
    private int level = 0;
    private int levelsum;
    private int nextlevel;
    //コンボ機能
    private int rencount;
    private int maxcombo=0;
    //SRS
    private int shapenumber;
    //アイテム
    int newScoreMultiplier;
    int newDropSpeed;
    boolean holddis;
    boolean obstacle;
    private Items selectedItem;  // selectedItem フィールドを追加
    private int fixcount=0;
    //T-spin
    private int movecount=1;
    private int TSpinBonus;
    //SE
    private SoundPool soundPool;
    private int levalsound;
    private int movesound;
    private int canselsound;
    private int breaksound;
    //お邪魔ミノ
    private int randomfix=10;
    private int randomplus;
    private int randommin;


    public TetrisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        fallInterval = FALL_INTERVAL;
        getHolder().addCallback(this);
        paint = new Paint();
        board = new int[NUM_ROWS][NUM_COLUMNS]; // ボードの初期化

        //アイテム系統
        itemManager = new ItemManager();
        // アイテムが渡された場合、そのアイテムを適用
        if (selectedItem != null) {
            applyItemEffect(selectedItem);
        }

        // SoundPoolの初期化（最大再生数を設定）
        soundPool = new SoundPool.Builder().setMaxStreams(10).build();
        // 効果音ファイルをロード（例: R.raw.se_sound に効果音ファイルがあると仮定）
        levalsound = soundPool.load(context, R.raw.levelup, 1);
        movesound = soundPool.load(context, R.raw.move, 1);
        canselsound = soundPool.load(context, R.raw.cansel_fix, 2);
        breaksound = soundPool.load(context, R.raw.breakline, 1);
    }

    // 次のレベルまでの点数を設定
    private int levelcheck() {
        levelsum = 0;
        for(int i=1 ; i<level+2 ; i++){
            levelsum += i;
            nextlevel = i;
        }
        if(score >= levelsum * 1800){
            soundPool.play(levalsound,1.0f,1.0f,1,0,0.8f);
            return nextlevel;
        }else{
            return level;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        cellSize = getWidth() / NUM_COLUMNS; // 各セルのサイズを決定
        generateNextTetrominoes();
        //checkNextTetrominoes();//次のミノが格納されているかの確認
        currentTetromino = nextTetrominoes.poll();
        draw();

        // 定期的にミノを落下させる処理を開始
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveDown(); // ミノを下に移動
                handler.postDelayed(this, fallInterval); // 再度呼び出す
            }
        }, fallInterval);
    }

    // 次の5つのテトリミノを生成
    public void generateNextTetrominoes() {
        while (nextTetrominoes.size() < NEXT_TETROMINOES_COUNT) {
            nextTetrominoes.add(new Tetromino());
        }
    }

    private void drawTetromino(Canvas canvas, Tetromino tetromino) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(tetromino.color);
        for (int i = 0; i < tetromino.shape.length; i++) {
            for (int j = 0; j < tetromino.shape[i].length; j++) {
                if (tetromino.shape[i][j] != 0) {
                    canvas.drawRect(
                            (tetromino.x + j) * cellSize,
                            (tetromino.y + i) * cellSize,
                            (tetromino.x + j + 1) * cellSize,
                            (tetromino.y + i + 1) * cellSize,
                            paint);
                    // 境界線を描画するために色とスタイルを変更
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(Color.BLACK); // 境界線の色を黒に設定
                    paint.setStrokeWidth(5); // 線の太さを設定

                    // ブロックの枠線を描画
                    canvas.drawRect(
                            (tetromino.x + j) * cellSize,
                            (tetromino.y + i) * cellSize,
                            (tetromino.x + j + 1) * cellSize,
                            (tetromino.y + i + 1) * cellSize,
                            paint);

                    // 元の塗りつぶしスタイルに戻す
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(tetromino.color);
                }
            }
        }


    }

    public void drawGrid(Canvas canvas) {
        canvas.drawColor(Color.BLACK); // 背景を黒に設定
        paint.setColor(Color.GRAY); // グリッド線の色を設定
        paint.setStrokeWidth(4); // 線の幅を設定

        // グリッドを描画
        for (int i = 5; i <= NUM_COLUMNS; i++) {
            canvas.drawLine(i * cellSize, cellSize * 4, i * cellSize, cellSize * NUM_ROWS, paint);
        }
        for (int j = 5; j <= NUM_ROWS; j++) {
            canvas.drawLine(cellSize * 5, j * cellSize, cellSize * NUM_COLUMNS, j * cellSize, paint);
        }
        paint.setColor(Color.RED); // グリッド線の色を設定
        canvas.drawLine(cellSize * 5, 5 * cellSize, cellSize * NUM_COLUMNS, 5 * cellSize, paint);

        // ボードの固定されたセルを描画
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLUMNS; j++) {
                if (board[i][j] != 0) {
                    // 塗りつぶし部分
                    paint.setColor(board[i][j]); // 固定された色を設定
                    canvas.drawRect(
                            j * cellSize,
                            i * cellSize,
                            (j + 1) * cellSize,
                            (i + 1) * cellSize,
                            paint);

                    // 境界線部分
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(Color.BLACK); // 境界線の色を黒に設定
                    paint.setStrokeWidth(5);
                    canvas.drawRect(
                            j * cellSize,
                            i * cellSize,
                            (j + 1) * cellSize,
                            (i + 1) * cellSize,
                            paint);

                    // 塗りつぶしスタイルに戻す
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(board[i][j]);
                }
            }
        }

    }

    public void addrows(Canvas canvas) {
         // ボードに追加されたセルを描画
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLUMNS; j++) {
                if (board[i][j] == 2) {
                    // 塗りつぶし部分
                    paint.setColor(Color.WHITE); // 固定された色を設定
                    canvas.drawRect(
                            j * cellSize,
                            i * cellSize,
                            (j + 1) * cellSize,
                            (i + 1) * cellSize,
                            paint);

                    // 境界線部分
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(Color.BLACK); // 境界線の色を黒に設定
                    paint.setStrokeWidth(5);
                    canvas.drawRect(
                            j * cellSize,
                            i * cellSize,
                            (j + 1) * cellSize,
                            (i + 1) * cellSize,
                            paint);

                    // 塗りつぶしスタイルに戻す
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.WHITE);
                }
            }
        }

    }

    private void drawNextTetrominoes(Canvas canvas) {
        int nextTetrominoX = 820; // テトリミノを描画するX座標の初期位置
        int nextTetrominoY = 90; // ボードの下部に表示するためのY座標の初期位置

        // Iteratorを使って最初の4つのテトリミノを表示する
        Iterator<Tetromino> iterator = nextTetrominoes.iterator();

        for (int num = 0; num < 4 && iterator.hasNext(); num++) {
            Tetromino tetromino = iterator.next(); // 次のテトリミノを取得
            paint.setColor(tetromino.color);//色の再設定
            for (int i = 0; i < tetromino.shape.length; i++) {
                for (int j = 0; j < tetromino.shape[i].length; j++) {
                    if (tetromino.shape[i][j] != 0) {

                        if(num==0){//大きさ調整
                            scale=6;
                        }else{
                            scale=5;
                        }

                        if(tetromino.color!=Color.rgb(255, 255, 0)&&tetromino.color!=Color.rgb(0, 255, 255)){
                            stand=25 * (1+num/2);
                        }else {
                            stand=10 * (1+num/2);
                        }

                        // 各テトリミノを左から順に描画
                        canvas.drawRect(
                                nextTetrominoX + stand + j * cellSize * scale / 8,
                                nextTetrominoY + stand + i * cellSize * scale / 8,
                                nextTetrominoX + stand + (j + 1) * cellSize * scale / 8,
                                nextTetrominoY + stand + (i + 1) * cellSize * scale / 8,
                                paint);

                        // 境界線を描画
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setColor(Color.BLACK);
                        paint.setStrokeWidth(5);
                        canvas.drawRect(
                                nextTetrominoX +stand + j * cellSize * scale / 8,
                                nextTetrominoY +stand + i * cellSize * scale / 8,
                                nextTetrominoX +stand + (j + 1) * cellSize * scale / 8,
                                nextTetrominoY +stand + (i + 1) * cellSize * scale / 8,
                                paint);

                        // 元の塗りつぶしスタイルに戻す
                        paint.setStyle(Paint.Style.FILL);
                        paint.setColor(tetromino.color);
                    }
                }
            }

            // 各テトリミノ間にスペースを確保
            nextTetrominoX -= 200; // テトリミノ間に10ピクセルのスペースを追加
            nextTetrominoY -= 30;
        }

    }

    private void drawHoldTetromino(Canvas canvas) {
        if (holdMino == null) return;

        int holdStartX = 1; // ホールドエリアの開始X位置（セル単位）
        int holdStartY = 5; // ホールドエリアの開始Y位置（セル単位）

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(holdMino.color);

        for (int i = 0; i < holdMino.shape.length; i++) {
            for (int j = 0; j < holdMino.shape[i].length; j++) {
                if (holdMino.shape[i][j] != 0) {
                    // holdMinoを特定の位置に描画
                    canvas.drawRect(
                            (holdStartX + j) * cellSize,
                            (holdStartY + i) * cellSize,
                            (holdStartX + j + 1) * cellSize,
                            (holdStartY + i + 1) * cellSize,
                            paint);

                    // 境界線を描画するために色とスタイルを変更
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(Color.BLACK); // 境界線の色を黒に設定
                    paint.setStrokeWidth(5); // 線の太さを設定

                    // ブロックの枠線を描画
                    canvas.drawRect(
                            (holdStartX + j) * cellSize,
                            (holdStartY + i) * cellSize,
                            (holdStartX + j + 1) * cellSize,
                            (holdStartY + i + 1) * cellSize,
                            paint);

                    // 元の塗りつぶしスタイルに戻す
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(holdMino.color);
                }
            }
        }
    }

    private void drawGhostTetromino(Canvas canvas) {
        // ゴーストの位置を計算
        int ghostcellY = currentTetromino.y;
        while (canMove(currentTetromino.x, ghostcellY + 1)) {
            ghostcellY++; // 下に移動
        }

        // 最終的なゴーストのY座標をピクセル単位に変換
        int ghostY = ghostcellY*cellSize; // ゴーストのY座標をピクセル単位に変換

        // ゴーストの色を設定
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.LTGRAY); // 境界線の色を黒に設定
        paint.setAlpha(100); // 半透明に設定（例: 100で薄い色）

        // ゴーストの内部を描画
        for (int i = 0; i < currentTetromino.shape.length; i++) {
            for (int j = 0; j < currentTetromino.shape[i].length; j++) {
                if (currentTetromino.shape[i][j] != 0) {
                    canvas.drawRect(
                            currentTetromino.x * cellSize + j * cellSize,
                            ghostY + i * cellSize,
                            currentTetromino.x * cellSize + (j + 1) * cellSize,
                            ghostY + (i + 1) * cellSize,
                            paint);
                }
            }
        }

        // ゴーストの色を設定
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.LTGRAY); // 境界線の色を黒に設定
        paint.setStrokeWidth(5); // 線の太さを設定

        // ゴーストを描画
        for (int i = 0; i < currentTetromino.shape.length; i++) {
            for (int j = 0; j < currentTetromino.shape[i].length; j++) {
                if (currentTetromino.shape[i][j] != 0) {
                    canvas.drawRect(
                            (currentTetromino.x + j) * cellSize,
                            ghostY + i * cellSize,
                            (currentTetromino.x + (j + 1)) * cellSize,
                            ghostY + (i + 1) * cellSize,
                            paint);
                }
            }
        }
    }



    // 一括描画メソッド
    private void draw() {
        Canvas canvas = getHolder().lockCanvas();
        if (canvas != null) {
            try {
                canvas.drawColor(Color.BLACK); // 背景色をリセット（黒に設定）

                // グリッドの描画
                drawGrid(canvas);

                //お邪魔ミノの描画
                addrows(canvas);

                //ゴーストの表示
                drawGhostTetromino(canvas);

                // 現在のテトリミノの描画
                drawTetromino(canvas, currentTetromino);

                // 次のテトリミノの描画
                drawNextTetrominoes(canvas);

                //ホールドされているミノの描画
                drawHoldTetromino(canvas);

            } finally {
                getHolder().unlockCanvasAndPost(canvas); // Canvasを解放して表示更新
            }
        }
    }

    // 壁を突き抜けないかの判定をする
    private boolean canMove(int newX, int newY) {
        for (int i = 0; i < currentTetromino.shape.length; i++) {
            for (int j = 0; j < currentTetromino.shape[i].length; j++) {
                if (currentTetromino.shape[i][j] != 0) { // セルが空でない場合
                    int x = newX + j; // 新しいX座標
                    int y = newY + i; // 新しいY座標

                    // 壁のチェック
                    if (x < 5 || x >= NUM_COLUMNS || y >= NUM_ROWS) {
                        return false; // 壁を突き抜ける場合
                    }

                    // 既存のブロックとの衝突をチェック
                    if (y >= 0 && y < NUM_ROWS && board[y][x] != 0) {
                        return false; // 既存のブロックと衝突する場合
                    }
                }
            }
        }
        return true; // 移動可能
    }

    public void moveLeft() {
        if (canMove(currentTetromino.x - 1, currentTetromino.y)) {
            soundPool.play(movesound,1.0f,1.0f,1,0,1.0f);
            currentTetromino.x--; // 左に移動
            movecount++;
        }
        draw();
    }

    public void moveRight() {
        soundPool.play(movesound,1.0f,1.0f,1,0,1.0f);
        if (canMove(currentTetromino.x + 1, currentTetromino.y)) {
            currentTetromino.x++; // 右に移動
            movecount++;
        }
        draw();
    }

    // ミノの落下を速くする処理
    public void setFastDrop(boolean fastDrop) {
        isFastDropping = fastDrop;
        // ファストドロップ中の場合は落下間隔を短くする
        if (isFastDropping) {
            fallInterval = (FALL_INTERVAL / (1+(4 * level / 10)))/15; // ファストドロップの速度
        } else {
            // スコアに基づく通常の落下間隔を設定
            fallInterval = FALL_INTERVAL / (1+(4 * level / 10)); // スピード倍率に基づく
        }
    }

    public void softDrop(){
        if (canMove(currentTetromino.x, currentTetromino.y+1)) {
            soundPool.play(movesound,1.0f,1.0f,1,0,1.0f);
            currentTetromino.y++; // 右に移動
        }
        draw();
    }

    //ミノを一瞬で下まで落とす
    public void hardDrop() {
        // テトリミノの y 座標が 0 未満の場合のみハードドロップを実行
        if (currentTetromino.y >= 4) {
            if (isHardDropping) {
                return; // すでにハードドロップ中の場合は何もしない
            }
            soundPool.play(movesound,1.0f,1.0f,1,0,1.0f);
            isHardDropping = true; // ハードドロップ中に設定

            while (canMoveDown(currentTetromino)) {
                currentTetromino.y++;  // 下に1マスずつ移動
            }
            fixTetromino();
            draw();

            // ハードドロップ処理が終わったらフラグをリセット
            isHardDropping = false;
        }

    }

    //ミノが移動可能かの判定
    private boolean canMoveDown(Tetromino tetromino) {
        // テトリミノの形状を取得
        int[][] shape = tetromino.shape;

        // テトリミノの各ブロックについて衝突を確認
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) { // ブロックが存在する部分のみチェック
                    int newY = tetromino.y + i + 1; // 下に1マス移動した位置

                    // ボードの端や他のブロックとの衝突を確認
                    if (newY >= NUM_ROWS || board[newY][tetromino.x + j] != 0) {
                        soundPool.play(canselsound,1.0f,1.0f,2,0,1.0f);
                        return false; // 衝突する場合、下には移動できない
                    }
                }
            }
        }
        return true; // 下に移動できる場合
    }

    // ミノの回転
    public void rightrotateBlock() {
        int originalRotation = currentTetromino.rotation;

        // 次の回転状態を計算 (90度時計回り)
        int nextRotation = (currentTetromino.rotation + 1) % 4;
        // 新しい形状を設定
        int[][] rotatedShape = new int[currentTetromino.shape[0].length][currentTetromino.shape.length];
        for (int i = 0; i < currentTetromino.shape.length; i++) {
            for (int j = 0; j < currentTetromino.shape[i].length; j++) {
                rotatedShape[j][currentTetromino.shape.length - 1 - i] = currentTetromino.shape[i][j];
            }
        }

        // 一時的に形状を保存
        int[][] originalShape = currentTetromino.shape;

        // テトリミノの回転を一時的に更新
        currentTetromino.shape = rotatedShape;
        currentTetromino.rotation = nextRotation;
        Log.d("Tetris", "Original Rotation: " + originalRotation);
        Log.d("Tetris", "Next Rotation: " + nextRotation);

        if(currentTetromino.shape.length<4){
            shapenumber=0;
        }else{
            shapenumber=1;
        }

        int[][] rotationOffsets = currentTetromino.getWallKickOffsets(originalRotation, nextRotation,shapenumber);

        for(int []offset : rotationOffsets){
            currentTetromino.shape = rotatedShape;
            int newx=currentTetromino.x+offset[0];
            int newy=currentTetromino.y+offset[1];
            if(canMove(newx,newy)){
                //SE
                soundPool.play(movesound,1.0f,1.0f,1,0,1.0f);
                currentTetromino.x=newx;
                currentTetromino.y=newy;
                movecount=0;
                break;
            }else{
                currentTetromino.shape=originalShape;
            }
        }

        draw();
    }

    public void leftrotateBlock() {
        int originalRotation = currentTetromino.rotation;

        // 次の回転状態を計算 (90度反時計回り)
        int nextRotation = (currentTetromino.rotation + 3) % 4;

        // 新しい形状を設定 (90度反時計回り)
        int[][] rotatedShape = new int[currentTetromino.shape[0].length][currentTetromino.shape.length];
        for (int i = 0; i < currentTetromino.shape.length; i++) {
            for (int j = 0; j < currentTetromino.shape[i].length; j++) {
                rotatedShape[currentTetromino.shape[i].length - 1 - j][i] = currentTetromino.shape[i][j];
            }
        }

        // 一時的に形状を保存
        int[][] originalShape = currentTetromino.shape;

        // テトリミノの回転を一時的に更新
        currentTetromino.shape = rotatedShape;
        currentTetromino.rotation = nextRotation;
        Log.d("Tetris", "Original Rotation: " + originalRotation);
        Log.d("Tetris", "Next Rotation: " + nextRotation);

        if(currentTetromino.shape.length<4){
            shapenumber=0;
        }else{
            shapenumber=1;
        }

        int[][] rotationOffsets = currentTetromino.getWallKickOffsets(originalRotation, nextRotation,shapenumber);

        for(int []offset : rotationOffsets){
            currentTetromino.shape = rotatedShape;
            int newx=currentTetromino.x+offset[0];
            int newy=currentTetromino.y+offset[1];
            if(canMove(newx,newy)){
                //SE
                soundPool.play(movesound,1.0f,1.0f,1,0,1.0f);
                currentTetromino.x=newx;
                currentTetromino.y=newy;
                movecount=0;
                break;
            }else{
                currentTetromino.shape=originalShape;
            }
        }

        draw();
    }

    public void swapHold() {
        if(holddis) return;//holddisが機能する場合hold不可
        if (hasSwapped) return; // 次のテトリミノが生成されるまで入れ替え禁止
        //SE
        soundPool.play(movesound,1.0f,1.0f,1,0,1.0f);


        if (holdMino == null) {
            // `holdMino`が空なら、現在のテトリミノを保存
            holdMino = currentTetromino;
            currentTetromino = nextTetrominoes.poll(); // 次のテトリミノを生成
            generateNextTetrominoes();
        } else {
            // `holdMino`と`currentTetromino`を入れ替える
            Tetromino temp = currentTetromino;
            currentTetromino = holdMino;
            holdMino = temp;
        }

        // 初期位置にリセット
        currentTetromino.x = 8;
        currentTetromino.y = 3;
        swapcount++;

        if(swapcount>0) {
            swapcount=0;
            hasSwapped = true; // 一度入れ替えたら次のテトリミノ生成まで禁止
        }

        draw();
    }


    // テトリミノを固定する処理を追加
    private void fixTetromino() {

        if(obstacle) fixcount++;

        for (int i = 0; i < currentTetromino.shape.length; i++) {
            for (int j = 0; j < currentTetromino.shape[i].length; j++) {
                if (currentTetromino.shape[i][j] != 0) {
                    board[currentTetromino.y + i][currentTetromino.x + j] = currentTetromino.color; // 色をボードに保存
                }
            }
        }

        //T-Spinの判定
        if(isTSpin(currentTetromino, board) == true){
            TSpinBonus=4;
        }else{
            TSpinBonus=1;
        }

        clearFullRows(); // 行が揃っているか確認し削除

        //お邪魔ミノの追加を判定
        if(fixcount == randomfix){
        addGarbageRows();
        }

        currentTetromino = nextTetrominoes.poll();
        generateNextTetrominoes();
        hasSwapped = false; // 新しいテトリミノ生成でholdminoのフラグリセット
        draw();
    }

    //ミノが下に移動できるのかを判定
    public void moveDown() {
        handler.removeCallbacksAndMessages(null);// 以前のコールバックを削除

        // ファストドロップの状態に応じてfallIntervalを設定
        if (!isFastDropping) {
            fallInterval = FALL_INTERVAL / (1+(4 * level / 10)); // スピード倍率に基づく
            fallInterval = fallInterval*newDropSpeed/10; // スピード倍率に基づく
        }

        if (canMove(currentTetromino.x, currentTetromino.y + 1)) {
            currentTetromino.y++; // 下に移動
        } else {
            //ゲームオーバー処理の一部
            if (currentTetromino.y < 5) {
                gameOver(); // ゲームオーバー処理を呼び出す
            }else {
                if (!isInGracePeriod) {
                    isInGracePeriod = true; // 猶予を開始
                    graceStartTime = System.currentTimeMillis(); // 現在の時間を取得
                } else {
                    // 猶予時間が経過したかチェック
                    if (System.currentTimeMillis() - graceStartTime >= GRACE_PERIOD) {
                        fixTetromino(); // 落ちたミノを固定
                        isInGracePeriod = false; // 猶予をリセット
                    }
                }
            }
        }
        draw();
        // 更新間隔を再設定
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveDown(); // ミノを下に移動
            }
        }, fallInterval); // 再度呼び出す
    }

    private boolean ALLclear(){//オールクリアの確認
        for (int y = 5; y < NUM_ROWS; y++) {
            for (int x = 5; x < NUM_COLUMNS; x++) {
                if (board[y][x] != 0) {  // ブロックが残っている場合
                    return false;
                }
            }
        }
        return true;
    }

    // 一列が揃っているか確認し、揃った行を削除する
    private void clearFullRows() {
        rowsCleared = 0; // 消えた行数をカウントする

        for (int row = 4; row < NUM_ROWS; row++) {
            boolean isFull = true;
            for (int col = 5; col < NUM_COLUMNS; col++) {
                if (board[row][col] == 0) {
                    isFull = false;
                    break;
                }
            }

            // 行が揃っている場合
            if (isFull) {
                rowsCleared++; // 消えた行をカウント

                // 上の行を1行ずつ下にずらす
                for (int r = row; r > 0; r--) {
                    for (int c = 0; c < NUM_COLUMNS; c++) {
                        board[r][c] = board[r - 1][c];
                    }
                }

                // 一番上の行を空にする
                for (int c = 0; c < NUM_COLUMNS; c++) {
                    board[0][c] = 0;
                }
                row--; // 行を削除したため、再チェック

                if(ALLclear()){//オールクリアでのボーナス
                    score += (1200 + 1200 * level)*newScoreMultiplier / 10 * 2;//tetris２つ分のスコア
                }
            }
        }

        // スコアを加算
        if (rowsCleared > 0) {

            rencount++;
            if(maxcombo<rencount){
                maxcombo=rencount;
            }

            soundPool.play(breaksound,0.4f,0.4f,1,0,1.0f);


            if(rowsCleared == 1){
                score += (40 + 40 * level)*newScoreMultiplier/10 * TSpinBonus;
            } else if (rowsCleared == 2) {
                score += (100 + 100 * level)*newScoreMultiplier/10 * TSpinBonus;
            } else if (rowsCleared == 3) {
                score += (300 + 300 * level)*newScoreMultiplier/10 * TSpinBonus;
            } else if (rowsCleared == 4) {
                score += (1200 + 1200 * level)*newScoreMultiplier/10 * TSpinBonus;
            }

            if(3 > rencount){
                score += (40 * (level + 1) * (rencount - 1))*newScoreMultiplier/10;
            } else if (11 > rencount && rencount >= 3) {
                score += (40 * (level + 1) * (rencount - 1) * 2)*newScoreMultiplier/10;
            } else if (rencount >= 11) {
                score += (20 * (level+1) * (rencount - 1))*newScoreMultiplier/10;
            }

            System.out.println("score: " + score);

            level = levelcheck();

            ((GameActivity) getContext()).updateScore(); // スコアをActivityに反映
            ((GameActivity) getContext()).updateLEVEL();
            ((GameActivity) getContext()).updateCombo();
        }else{
            rencount = 0;
        }
    }

    // ゲームオーバー処理
    private void gameOver() {
        fallInterval = FALL_INTERVAL;
        Tetromino.clearBag();
        ((GameActivity) getContext()).showResultScreen(score,level,maxcombo); // GameActivityに通知してリザルト画面に遷移させる
    }

    // スコア更新メソッド（GameActivityから呼び出せるようにする）
    public int getScore() {
        return score;
    }

    public int getlevel() {
        return level;
    }

    public int getCombo() {
        return maxcombo;
    }

    //アイテム系統
    private ItemManager itemManager;

    public void setSelectedItem(Items selectedItem) {
        this.selectedItem = selectedItem;
        applyItemEffect(selectedItem); // アイテムの効果を適用
    }


    public void applyItemEffect(Items item) {
        newScoreMultiplier = (int)item.getScoreMultiplier();
        newDropSpeed = (int)item.getDropSpeedModifier();
        holddis = item.isHoldDisabled();
        obstacle = item.isGenerateObstacle();
    }

    //Tspinの判定
    public boolean isTSpin(Tetromino tetromino, int[][] board) {
        // T型以外の場合はfalseを返す
        if (tetromino.color!=Color.rgb(128, 0, 128)) {
            return false;
        }

        // コーナーの位置を取得（例として、テトリミノの4つの角を基準にする）
        int cornerCount = 0;

        // 4つのコーナーを確認
        if(currentTetromino.y==23){
            cornerCount = 2;
            if (board[currentTetromino.y][currentTetromino.x] != 0) cornerCount++; // 左上
            if (board[currentTetromino.y][currentTetromino.x + 2] != 0) cornerCount++; // 右上
        } else if (currentTetromino.x==13) {
            cornerCount = 2;
            if (board[currentTetromino.y][currentTetromino.x] != 0) cornerCount++; // 左上
            if (board[currentTetromino.y + 2][currentTetromino.x] != 0) cornerCount++; // 左下
        } else if (currentTetromino.x==4) {
            cornerCount = 2;
            if (board[currentTetromino.y][currentTetromino.x + 2] != 0) cornerCount++; // 右上
            if (board[currentTetromino.y + 2][currentTetromino.x + 2] != 0) cornerCount++; // 右下
        }else  {
            if (board[currentTetromino.y][currentTetromino.x] != 0) cornerCount++; // 左上
            if (board[currentTetromino.y][currentTetromino.x + 2] != 0) cornerCount++; // 右上
            if (board[currentTetromino.y + 2][currentTetromino.x] != 0) cornerCount++; // 左下
            if (board[currentTetromino.y + 2][currentTetromino.x + 2] != 0) cornerCount++; // 右下
        }


        System.out.println("TSpin corner:"+cornerCount+",move:"+movecount);

        // 3つ以上のコーナーが埋まっている場合、Tスピンと判定
        if(cornerCount >= 3&&movecount==0){
            System.out.println("Tspin");
            return true;
        }else{
            System.out.println("not");
            return false;
        }
    }

    // 4行のお邪魔ミノを最下部に追加するメソッド
    public void addGarbageRows() {
        int NUM_COLUMNS = board[0].length; // 列数の取得
        int NUM_ROWS = board.length; // 行数の取得

        if(newScoreMultiplier == 15){
            randomplus=2;
            randommin=1;
        } else if (newScoreMultiplier == 25) {
            randomplus=4;
            randommin=1;
        }else{
            randomplus=4;
            randommin=2;
        }

        int randomadd = (int)(Math.random() * randomplus) + randommin;

        // 上から4行分を下にシフト
        for (int r = 0; r < NUM_ROWS - randomadd; r++) {
            for (int c = 0; c < NUM_COLUMNS; c++) {
                board[r][c] = board[r + randomadd][c];
            }
        }

        // 最下部の4行にランダムなお邪魔ミノを配置
        for (int r = NUM_ROWS - randomadd; r < NUM_ROWS; r++) {
            int randomInt = (int)(Math.random() * 10) + 1 + 4;
            for (int c = NUM_COLUMNS - 10; c < NUM_COLUMNS; c++) {

                if(randomInt==5){
                    randomInt = 7;
                }else if(randomInt == 15){
                    randomInt = 8;
                }

                if(c != randomInt){
                    board[r][c] = 2;
                }else{
                    board[r][c] = 0;
                }

            }
        }

        fixcount=0;
        randomfix = (int)(Math.random() * 4) + 7;

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // 何もしない場合でもメソッドを実装する必要があります。
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        handler.removeCallbacksAndMessages(null); // すべてのコールバックを削除
        // SoundPoolのリソースを解放
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}