package com.example.gesturerecognizer.Recognition;

import static com.example.gesturerecognizer.Recognition.Utils.angleBetweenUnitVectors;
import static com.example.gesturerecognizer.Recognition.Utils.combineStrokes;
import static com.example.gesturerecognizer.Recognition.Utils.deg2rad;
import static com.example.gesturerecognizer.Recognition.Utils.getDistanceAtBestAngle;

import com.example.gesturerecognizer.Common.Point;
import com.example.gesturerecognizer.Common.Result;

import java.util.ArrayList;

public class Recognizer {
    ArrayList<Multistroke> multistrokes;
    boolean boundedRotationInvariance;
    double angleSimilarityThreshold;
    double angleRange;
    double anglePrecision;
    double squareSize;
    double diagonal;
    double halfDiagonal;

    public Recognizer(boolean boundedRotationInvariance) {
        this.boundedRotationInvariance = boundedRotationInvariance;
        this.angleSimilarityThreshold = deg2rad(30.0);
        this.angleRange = deg2rad(45.0);
        this.anglePrecision = deg2rad(2.0);
        this.squareSize = 250.0;
        this.diagonal = Math.sqrt(squareSize * squareSize + squareSize * squareSize);
        this.halfDiagonal = 0.5 * diagonal;
        this.multistrokes = new ArrayList<>();
    }

    public void AddGesture(String name, boolean boundedRotationInvariance, ArrayList<ArrayList<Point>> strokes) {
        this.multistrokes.add(new Multistroke(name, boundedRotationInvariance, strokes));
    }

    public Result Recognize(ArrayList<ArrayList<Point>> strokes) {
        long startTime = System.currentTimeMillis();
        ArrayList<Point> points = combineStrokes(strokes);
        Unistroke candidate = new Unistroke("Candidate", true, points);
        int u = -1;
        double b = Double.POSITIVE_INFINITY;
        for (int i = 0; i < this.multistrokes.size(); i++) {
            for (int j = 0; j < this.multistrokes.get(i).Unistrokes.size(); j++) {
                if (angleBetweenUnitVectors(candidate.startUnitVector, this.multistrokes.get(i).Unistrokes.get(j).startUnitVector) <= this.angleSimilarityThreshold) {
                    double d = getDistanceAtBestAngle(candidate.points, this.multistrokes.get(i).Unistrokes.get(j).points, -angleRange, +angleRange, anglePrecision);
                    if (d < b) {
                        b = d;
                        u = i;
                    }
                }
            }
        }
        long endTime = System.currentTimeMillis();
        if (u == -1) {
            return new Result("No Match!", 0.0, endTime - startTime);
        } else {
            return new Result(this.multistrokes.get(u).Name, (1.0 - b / this.halfDiagonal), (endTime - startTime));
        }
    }
}
