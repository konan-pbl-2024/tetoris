package com.example.tetoris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.ContextCompat;

public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private int COLUMNS = 16; // テトリスの列数
    private int ROWS = 26; // テトリスの行数
    private int cell=20;
    private Paint paint;
    private Paint transparentPaint;

    public CustomSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);

        // 透過背景の設定
        setZOrderOnTop(true);
        getHolder().setFormat(android.graphics.PixelFormat.TRANSPARENT);

        // 描画に使用するPaintオブジェクトの初期化
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL); // 塗りつぶしスタイル
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas();
        if (canvas != null) {
            // 背景画像の設定
            Bitmap backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.surfaceback2);

            // クリッピング領域を定義
            Path clipPath = new Path();
            clipPath.addRect(820, 50, 1050, 280, Path.Direction.CW);  // ネクスト部分1
            clipPath.addRect(630, 30, 810, 210, Path.Direction.CW);  // ネクスト部分2
            clipPath.addRect(440, 20, 620, 200, Path.Direction.CW);  // ネクスト部分3
            clipPath.addRect(250, 10, 430, 190, Path.Direction.CW);  // ネクスト部分4

            clipPath.addRect(350, 320, 1050, 1750, Path.Direction.CW); // フィールド部分
            clipPath.addRect(20, 320, 345, 650, Path.Direction.CW);  // ホールド部分

            // クリッピング開始
            canvas.save();
            canvas.clipOutPath(clipPath);  // API 24以降対応
            // 画像の描画（透過部分以外に表示）
            canvas.drawBitmap(backgroundImage, null, new Rect(0, 0, getWidth(), getHeight()), null);
            canvas.restore();  // クリッピング解除

            holder.unlockCanvasAndPost(canvas);
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Surfaceのサイズやフォーマットが変更されたときの処理
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surfaceが破棄されたときの処理
    }
}
