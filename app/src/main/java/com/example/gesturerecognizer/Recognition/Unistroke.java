package com.example.gesturerecognizer.Recognition;

import static com.example.gesturerecognizer.Recognition.Utils.calcStartUnitVector;
import static com.example.gesturerecognizer.Recognition.Utils.getIndicativeAngle;
import static com.example.gesturerecognizer.Recognition.Utils.resample;
import static com.example.gesturerecognizer.Recognition.Utils.rotateBy;
import static com.example.gesturerecognizer.Recognition.Utils.scaleDimTo;
import static com.example.gesturerecognizer.Recognition.Utils.translateTo;
import static com.example.gesturerecognizer.Recognition.Utils.vectorize;

import android.util.Log;

import com.example.gesturerecognizer.Common.Point;

import java.util.ArrayList;

public class Unistroke {
    int numPoints;
    int startAngleIndex;
    double indicativeAngle;
    double squareSize;
    double oneDThreshold;
    double[] startUnitVector;
    double[] vector;
    String name;
    boolean boundedRotationInvariance;
    ArrayList<Point> points;
    Point origin;

    public Unistroke(String name, boolean boundedRotationInvariance, ArrayList<Point> points) {
        this.name = name;
        this.boundedRotationInvariance = boundedRotationInvariance;
        this.points = points;
        this.numPoints = 96;
        this.squareSize = 250.0;
        this.oneDThreshold = 0.25;
        this.origin = new Point(0, 0);
        this.startAngleIndex = numPoints / 8;

        this.points = resample(points, this.numPoints);
        this.indicativeAngle = getIndicativeAngle(this.points);
        this.points = rotateBy(this.points, -this.indicativeAngle);
        this.points = scaleDimTo(this.points, this.squareSize, this.oneDThreshold);
        if (this.boundedRotationInvariance)
            this.points = rotateBy(this.points, this.indicativeAngle);
        this.points = translateTo(this.points, this.origin);
        this.startUnitVector = calcStartUnitVector(this.points, this.startAngleIndex);
        this.vector = vectorize(this.points, this.boundedRotationInvariance);
    }
}
