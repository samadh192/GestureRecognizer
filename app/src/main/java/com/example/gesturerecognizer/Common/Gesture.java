package com.example.gesturerecognizer.Common;

import java.util.ArrayList;

public class Gesture {
    String name;
    ArrayList<ArrayList<Point>> strokes;

    public Gesture(String name, ArrayList<ArrayList<Point>> strokes) {
        this.name = name;
        this.strokes = strokes;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ArrayList<Point>> getStrokes() {
        return strokes;
    }
}
