package com.example.gesturerecognizer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.gesturerecognizer.Recognition.Segment;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class RecognizerActivity extends AppCompatActivity {
    Integer template;
    String mode;
    String title;
    Button clearButton;
    Button submitButton;
    PaintView paintView;
    ArrayList<ArrayList<Point>> strokes;
    ArrayList<ArrayList<ArrayList<Point>>> recognitionObjects;
    ArrayList<Gesture> gestures;
    TextView resultTextView;
    TextView titleTextView;
    Recognizer recognizer;
    Segment segment;
    Result result;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognizer);

        paintView = findViewById(R.id.paintView);
        clearButton = findViewById(R.id.clearButton);
        submitButton = findViewById(R.id.submitButton);
        resultTextView = findViewById(R.id.textViewResult);
        titleTextView = findViewById(R.id.textViewTitle);
        intent = getIntent();
        template = intent.getIntExtra("template",0);
        mode = intent.getStringExtra("mode");
        title = intent.getStringExtra("title");

        titleTextView.setText(title);
        recognizer = new Recognizer(true);

        try {
            gestures = JsonUtils.readGestureFromJson(this, template);
            for (int i = 0; i < gestures.size(); i++) {
                recognizer.AddGesture(gestures.get(i).getName(), true, gestures.get(i).getStrokes());
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

        clearButton.setOnClickListener(v -> {
            paintView.clear();
            resultTextView.setText("");
        });

        submitButton.setOnClickListener(v -> {
            strokes = paintView.getStrokes();
            if(Objects.equals(mode, "singleGestureRecognition")){
                result = recognizer.Recognize(strokes);
                resultTextView.setText(result.getResultString());
            }
            else if(Objects.equals(mode, "multiGestureRecognition")){
                String equation = getEquation(strokes);
                double expressionResult = 0;
                try{
                    Expression expression = new ExpressionBuilder(transformExpression(equation)).build();
                    expressionResult = expression.evaluate();
                    resultTextView.setText(String.format("Equation: %s Result=%s",equation,expressionResult));
                }
                catch (Exception exception){
                    Log.println(Log.ERROR,"EquationEvaluation", String.valueOf(exception));
                    resultTextView.setText(String.format("Equation %s is invalid!",equation));
                }
            }
        });
    }

    private String getEquation(ArrayList<ArrayList<Point>> strokes) {
        StringBuilder equation = new StringBuilder();
        segment = new Segment(strokes,15);
        recognitionObjects = segment.getRecognitionObjects();
        for(ArrayList<ArrayList<Point>> combinedStrokes:recognitionObjects) {
            result = recognizer.Recognize(combinedStrokes);
            equation.append(result.getGesture());
        }
        return String.valueOf(equation);
    }

    private String transformExpression(String equation) {
        return equation.replace('X','*');
    }
}