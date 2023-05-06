package com.example.gesturerecognizer.Recognition;

import com.example.gesturerecognizer.Common.Point;

import java.util.ArrayList;

public class Segment {
    ArrayList<ArrayList<Point>> strokes;
    ArrayList<ArrayList<ArrayList<Point>>> recognitionObjects;
    double threshold;

    public ArrayList<ArrayList<ArrayList<Point>>> getRecognitionObjects() {
        return recognitionObjects;
    }

    public Segment(ArrayList<ArrayList<Point>> strokes, double threshold) {
        this.strokes = strokes;
        this.threshold = threshold;
        this.recognitionObjects = new ArrayList<>();
        startSegmentation();
    }

    private void startSegmentation() {
        boolean skipEvaluation = false;
        ArrayList<ArrayList<Point>> combinedStrokes = new ArrayList<>();
        for (int i=0;i<strokes.size()-1;i++) {
            if (skipEvaluation) {
                skipEvaluation = false;
                continue;
            }
            ArrayList<Point> strokeA = new ArrayList<>(strokes.get(i));
            ArrayList<Point> strokeB = new ArrayList<>(strokes.get(i+1));
            if (isOverlapping(strokeA, strokeB)) {
                combinedStrokes.add(strokeA);
                combinedStrokes.add(strokeB);
                skipEvaluation = true;
            }
            else {
                combinedStrokes.add(strokeA);
            }
            recognitionObjects.add(new ArrayList<>(combinedStrokes));
            combinedStrokes.clear();
        }
        if (!skipEvaluation) {
            combinedStrokes.add(strokes.get(strokes.size()-1));
            recognitionObjects.add(combinedStrokes);
        }
    }

    private boolean isOverlapping(ArrayList<Point> strokeA, ArrayList<Point> strokeB) {
        for (Point pointA:strokeA) {
            for (Point pointB:strokeB) {
                if (getEucledianDistance(pointA,pointB) <= this.threshold) {
                    return true;
                }
            }
        }
        return false;
    }

    private double getEucledianDistance(Point pointA, Point pointB) {
        return Math.sqrt(Math.pow(pointA.getX()-pointB.getX(),2)+Math.pow(pointA.getY()- pointB.getY(),2));
    }
}
