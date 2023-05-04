package com.example.gesturerecognizer.Common;

public class Result {
    String gesture;
    long timeOfExecution;
    double confidence;

    public Result(String gesture, double confidence, long timeOfExecution) {
        this.gesture = gesture.split("-")[0];
        this.confidence = confidence;
        this.timeOfExecution = (long) (Math.round(timeOfExecution * 100) / 100.0);
    }

    public long getTimeOfExecution() {
        return timeOfExecution;
    }

    public String getGesture() {
        return gesture;
    }

    public String getResultString() {
        return String.format("Gesture: %s Confidence: %s%% Time:%sms", getGesture(), getConfidencePercentage(), getTimeOfExecution());
    }

    private int getConfidencePercentage() {
        return (int) Math.round(this.confidence * 100);
    }
}
