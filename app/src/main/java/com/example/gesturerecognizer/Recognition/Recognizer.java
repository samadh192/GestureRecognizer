package com.example.gesturerecognizer.Recognition;

import static com.example.gesturerecognizer.Recognition.Utils.angleBetweenUnitVectors;
import static com.example.gesturerecognizer.Recognition.Utils.combineStrokes;
import static com.example.gesturerecognizer.Recognition.Utils.deg2rad;
import static com.example.gesturerecognizer.Recognition.Utils.getDistanceAtBestAngle;

import com.example.gesturerecognizer.Common.Point;
import com.example.gesturerecognizer.Common.Result;

import java.util.ArrayList;
import java.util.Arrays;

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

//        this.multistrokes.add(new Multistroke("T",this.boundedRotationInvariance,new ArrayList<>(
//                Arrays.asList(
//                        new ArrayList<>(Arrays.asList(new Point(30,7), new Point(103,7))),
//                        new ArrayList<>(Arrays.asList(new Point(66,7), new Point(66,87)))
//                )
//        )));
//        this.multistrokes.add(new Multistroke("N",this.boundedRotationInvariance,new ArrayList<>(
//                Arrays.asList(
//                        new ArrayList<>(Arrays.asList(new Point(177,92),new Point(177,2))),
//                        new ArrayList<>(Arrays.asList(new Point(182,1),new Point(246,95))),
//                        new ArrayList<>(Arrays.asList(new Point(247,87),new Point(247,1)))
//                )
//        )));
    }

    public void AddGesture(String name, boolean boundedRotationInvariance, ArrayList<ArrayList<Point>> strokes) {
        this.multistrokes.add(new Multistroke(name,boundedRotationInvariance, strokes));
    }

    public Result Recognize(ArrayList<ArrayList<Point>> strokes) {
        ArrayList<Point> points = combineStrokes(strokes);
        Unistroke candidate = new Unistroke("Candidate",true,points);
        int u = -1;
        double b = Double.POSITIVE_INFINITY;
        for (int i=0;i<this.multistrokes.size();i++) {
            for (int j=0;j<this.multistrokes.get(i).Unistrokes.size();j++) {
                if (angleBetweenUnitVectors(candidate.startUnitVector, this.multistrokes.get(i).Unistrokes.get(j).startUnitVector)<=this.angleSimilarityThreshold){
                    double d = getDistanceAtBestAngle(candidate.points, this.multistrokes.get(i).Unistrokes.get(j).points, -angleRange, +angleRange, anglePrecision);
                    if (d<b) {
                        b = d;
                        u = i;
                    }
                }
            }
        }
        if (u==-1) {
            return new Result("No Match!",0.0);
        }
        else{
            return new Result(this.multistrokes.get(u).Name,(1.0-b/this.halfDiagonal));
        }
    }
}
