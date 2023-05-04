package com.example.gesturerecognizer.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gesturerecognizer.Common.Point;
import com.example.gesturerecognizer.Common.Result;
import com.example.gesturerecognizer.CustomViews.PaintView;
import com.example.gesturerecognizer.R;
import com.example.gesturerecognizer.Recognition.Recognizer;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    Button clearButton;
    Button submitButton;
    PaintView paintView;
    ArrayList<ArrayList<Point>> strokes;
    TextView textView;
    Recognizer recognizer;
    Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paintView = findViewById(R.id.paintView);
        clearButton = findViewById(R.id.clearButton);
        submitButton = findViewById(R.id.submitButton);
        textView = findViewById(R.id.textView);

        recognizer = new Recognizer(true);


        clearButton.setOnClickListener(v -> {
            paintView.clear();
            textView.setText("");
        });

        submitButton.setOnClickListener(v -> {
            strokes = paintView.getStrokes();
//            strokes = new ArrayList<>(
//                    Arrays.asList(
//                            new ArrayList<>(Arrays.asList(new Point(30,7), new Point(103,7)))
////                            new ArrayList<>(Arrays.asList(new Point(66,7), new Point(66,87)))
//                    )
//            );ß
            result = recognizer.Recognize(strokes);
            textView.setText(result.getResultString());
        });
    }
}