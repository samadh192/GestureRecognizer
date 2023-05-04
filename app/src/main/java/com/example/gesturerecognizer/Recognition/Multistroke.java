package com.example.gesturerecognizer.Recognition;

import com.example.gesturerecognizer.Common.Point;
import static com.example.gesturerecognizer.Recognition.Utils.heapPermute;
import static com.example.gesturerecognizer.Recognition.Utils.makeUnistrokes;

import java.util.ArrayList;

public class Multistroke {
    String Name;
    int NumStrokes;
    ArrayList<Unistroke> Unistrokes;

    public Multistroke(String name, boolean useBoundedRotationInvariance, ArrayList<ArrayList<Point>> strokes) {
        this.Name = name;
        this.NumStrokes = strokes.size();

        ArrayList<Integer> order = new ArrayList<>();
        for (int i = 0; i < strokes.size(); i++) {
            order.add(i);
        }

        ArrayList<ArrayList<Integer>> orders = new ArrayList<>();
        heapPermute(strokes.size(), order, orders);

        ArrayList<ArrayList<Point>> unistrokes = makeUnistrokes(strokes, orders);
        ArrayList<Unistroke> unistrokesList = new ArrayList<>();
        for (ArrayList<Point> unistroke : unistrokes) {
            unistrokesList.add(new Unistroke(name, useBoundedRotationInvariance, unistroke));
        }
        this.Unistrokes = unistrokesList;
    }
}
