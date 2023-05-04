package com.example.gesturerecognizer.Common;

public class Result {
    String gesture;
    double confidence;

    public Result(String gesture, double confidence) {
        this.gesture = gesture.split("-")[0];
        this.confidence = confidence;
    }

    public String getGesture() {
        return gesture;
    }

    public void setGesture(String gesture) {
        this.gesture = gesture;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String getResultString() {
        return String.format("Gesture: %s Confidence: %s%%",getGesture(), getConfidencePercentage());
    }

    private int getConfidencePercentage() {
        return (int) Math.round(this.confidence*100);
    }
}
