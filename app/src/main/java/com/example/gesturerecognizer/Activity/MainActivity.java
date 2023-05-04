package com.example.gesturerecognizer.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gesturerecognizer.Common.Gesture;
import com.example.gesturerecognizer.Common.JsonUtils;
import com.example.gesturerecognizer.Common.Point;
import com.example.gesturerecognizer.Common.Result;
import com.example.gesturerecognizer.CustomViews.PaintView;
import com.example.gesturerecognizer.R;
import com.example.gesturerecognizer.Recognition.Recognizer;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button clearButton;
    Button submitButton;
    PaintView paintView;
    ArrayList<ArrayList<Point>> strokes;
    ArrayList<Gesture> gestures;
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

        try {
            gestures = JsonUtils.readGestureFromJson(this);
            for (int i = 0; i < gestures.size(); i++) {
                recognizer.AddGesture(gestures.get(i).getName(), true, gestures.get(i).getStrokes());
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }


        clearButton.setOnClickListener(v -> {
            paintView.clear();
            textView.setText("");
        });

        submitButton.setOnClickListener(v -> {
            strokes = paintView.getStrokes();
            result = recognizer.Recognize(strokes);
            textView.setText(result.getResultString());
        });
    }
}