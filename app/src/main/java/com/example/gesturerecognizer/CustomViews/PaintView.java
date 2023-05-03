package com.example.gesturerecognizer.CustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.gesturerecognizer.Common.Point;
import com.example.gesturerecognizer.R;

import java.util.ArrayList;

@SuppressLint("ViewConstructor")
public class PaintView extends View {
    private final Paint drawPaint;
    private final Path path = new Path();
    private final ArrayList<ArrayList<Point>> strokes = new ArrayList<>();
    private final ArrayList<Point> stroke = new ArrayList<>();

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.println(Log.ASSERT, "TEST", "Paint View Initialized!");
        setFocusable(true);
        setFocusableInTouchMode(true);
        drawPaint = new Paint();
        drawPaint.setColor(ContextCompat.getColor(context, R.color.draw_blue));
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }
    public void clear() {
        path.reset();
        strokes.clear();
        postInvalidate();
    }

    public ArrayList<ArrayList<Point>> getStrokes() {
        return strokes;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX, pointY);
                stroke.add(new Point((int)pointX,(int)pointY));
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX, pointY);
                stroke.add(new Point((int)pointX,(int)pointY));
                break;
            case MotionEvent.ACTION_UP:
                strokes.add(new ArrayList<>(stroke));
                stroke.clear();
                break;
            default:
                return false;
        }
        postInvalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, drawPaint);
    }
}

