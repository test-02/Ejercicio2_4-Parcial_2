package com.example.ejercicio2_4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Lienzo extends View{

    private Path drawPath;
    public static Paint drawPaint;
    private Paint canvasPaint;

    private int paintColor = 0xFF000000;

    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    public boolean borrado = true;

    public Lienzo(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing(){
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);

        borrado = true;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w,h,oldw,oldh);
        canvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    public void onDraw(Canvas canvas){
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX,touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX,touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX,touchY);
                drawCanvas.drawPath(drawPath,drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        borrado = false;
        return true;

    }

    public void nuevoDibujo(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
        borrado = true;
    }
}
