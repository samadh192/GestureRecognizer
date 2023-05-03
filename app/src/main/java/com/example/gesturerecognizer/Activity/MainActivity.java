package com.example.gesturerecognizer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.gesturerecognizer.Common.Point;
import com.example.gesturerecognizer.CustomViews.PaintView;
import com.example.gesturerecognizer.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button clearButton;
    Button submitButton;
    PaintView paintView;
    ArrayList<ArrayList<Point>> strokes;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paintView = findViewById(R.id.paintView);
        clearButton = findViewById(R.id.clearButton);
        submitButton = findViewById(R.id.submitButton);
        textView = findViewById(R.id.textView);


        clearButton.setOnClickListener(v -> {
            paintView.clear();
            textView.setText("");
        });

        submitButton.setOnClickListener(v -> {
            strokes = paintView.getStrokes();
            textView.setText(String.format("Number of strokes:%s",strokes.size()));
        });
    }
}