package com.example.gesturerecognizer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gesturerecognizer.R;

public class MainActivity extends AppCompatActivity {
    Button mathSymbolRecognizerButton;
    Button dollarNRecognizerButton;
    Button equationRecognizerButton;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mathSymbolRecognizerButton = findViewById(R.id.buttonMathSymbolRecognizer);
        dollarNRecognizerButton = findViewById(R.id.buttonDollarN);
        equationRecognizerButton = findViewById(R.id.buttonEquationRecognizer);
        intent = new Intent(getApplicationContext(), RecognizerActivity.class);

        mathSymbolRecognizerButton.setOnClickListener(v -> {
            intent.putExtra("template",R.raw.equation_recognizer_template);
            intent.putExtra("mode","singleGestureRecognition");
            startActivity(intent);
        });

        dollarNRecognizerButton.setOnClickListener(v -> {
            intent.putExtra("template",R.raw.dollar_n_template);
            intent.putExtra("mode","singleGestureRecognition");
            startActivity(intent);
        });

        equationRecognizerButton.setOnClickListener(v -> {
            intent.putExtra("template",R.raw.equation_recognizer_template);
            intent.putExtra("mode","multiGestureRecognition");
            startActivity(intent);
        });
    }
}