package com.example.gesturerecognizer.Common;

public class Rectangle {
    private final double X;
    private final double Y;
    private double width;
    private double height;
    public Rectangle(int x, int y, int width, int height) {
        this.X = x;
        this.Y = y;
        this.width = width;
        this.height = height;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWidth(double width) {
        this.width = width;
    }
}
