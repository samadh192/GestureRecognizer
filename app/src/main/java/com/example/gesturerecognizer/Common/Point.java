package com.example.gesturerecognizer.Common;

public class Point {
    private final double X;
    private final double Y;

    public Point(double x, double y) {
        X = x;
        Y = y;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public String getCoordinates() {
        return String.format("(%s,%s)",X,Y);
    }
}
