package com.example.gesturerecognizer.Recognition;

import android.util.Log;

import com.example.gesturerecognizer.Common.Point;
import com.example.gesturerecognizer.Common.Rectangle;

import java.util.ArrayList;
import java.util.Collections;

public class Utils {
    static double Phi = 0.5 * (-1.0 + Math.sqrt(5.0));

    public static ArrayList<Point> resample(ArrayList<Point> points, int n) {
        double I = getPathLength(points) / (n - 1);
        double D = 0.0;
        ArrayList<Point> resampledPoints = new ArrayList<>();
        Point firstPoint = new Point(points.get(0).getX(), points.get(0).getY());
        resampledPoints.add(firstPoint);
        int i = 1;
        while (i < points.size()) {
            double d = getDistance(points.get(i - 1), points.get(i));
            if ((D + d) >= I) {
                double qx = points.get(i - 1).getX() + ((I - D) / d) * (points.get(i).getX() - points.get(i - 1).getX());
                double qy = points.get(i - 1).getY() + ((I - D) / d) * (points.get(i).getY() - points.get(i - 1).getY());
                Point q = new Point(qx, qy);
                resampledPoints.add(q);
                points.add(i, q);
                D = 0.0;
            } else {
                D += d;
            }
            i++;
        }
        if (resampledPoints.size() == n - 1) {
            Point lastPoint = new Point(points.get(points.size() - 1).getX(), points.get(points.size() - 1).getY());
            resampledPoints.add(lastPoint);
        }
        return resampledPoints;
    }

    public static Rectangle getBoundingBox(ArrayList<Point> points) {
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        for (Point p : points) {
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
        }
        return new Rectangle((int) minX, (int) minY, (int) (maxX - minX), (int) (maxY - minY));
    }

    public static double[] vectorize(ArrayList<Point> points, boolean useBoundedRotationInvariance) {
        double cosine = 1.0;
        double sine = 0.0;
        if (useBoundedRotationInvariance) {
            double iAngle = Math.atan2(points.get(0).getY(), points.get(0).getX());
            double baseOrientation = (Math.PI / 4.0) * Math.floor((iAngle + Math.PI / 8.0) / (Math.PI / 4.0));
            cosine = Math.cos(baseOrientation - iAngle);
            sine = Math.sin(baseOrientation - iAngle);
        }
        double sum = 0.0;
        double[] vector = new double[2 * points.size()];
        for (int i = 0; i < points.size(); i++) {
            double newX = points.get(i).getX() * cosine - points.get(i).getY() * sine;
            double newY = points.get(i).getY() * cosine + points.get(i).getX() * sine;
            vector[2 * i] = newX;
            vector[2 * i + 1] = newY;
            sum += newX * newX + newY * newY;
        }
        double magnitude = Math.sqrt(sum);
        for (int i = 0; i < vector.length; i++) {
            vector[i] /= magnitude;
        }
        return vector;
    }

    public static void heapPermute(int n, ArrayList<Integer> order, ArrayList<ArrayList<Integer>> orders) {
        if (n == 1) {
            orders.add(new ArrayList<>(order)); // append copy of order to orders list when n == 1
        } else {
            for (int i = 0; i < n; i++) {
                heapPermute(n - 1, order, orders); // recursively generate permutations
                if (n % 2 == 1) { // swap 0, n-1 if n is odd
                    Collections.swap(order, 0, n - 1);
                } else { // swap i, n-1 if n is even
                    Collections.swap(order, i, n - 1);
                }
            }
        }
    }

    public static ArrayList<ArrayList<Point>> makeUnistrokes(ArrayList<ArrayList<Point>> strokes, ArrayList<ArrayList<Integer>> orders) {
        ArrayList<ArrayList<Point>> unistrokes = new ArrayList<>();
        for (int r = 0; r < orders.size(); r++) {
            for (int b = 0; b < (1 << orders.get(r).size()); b++) {
                ArrayList<Point> unistroke = new ArrayList<>();
                for (int i = 0; i < orders.get(r).size(); i++) {
                    ArrayList<Point> pts;
                    if (((b >> i) & 1) == 1) {
                        pts = new ArrayList<>(strokes.get(orders.get(r).get(i)));
                        Collections.reverse(pts);
                    } else {
                        pts = new ArrayList<>(strokes.get(orders.get(r).get(i)));
                    }
                    unistroke.addAll(pts);
                }
                unistrokes.add(unistroke);
            }
        }
        return unistrokes;
    }

    public static ArrayList<Point> combineStrokes(ArrayList<ArrayList<Point>> strokes) {
        ArrayList<Point> points = new ArrayList<>();
        for (int s = 0; s < strokes.size(); s++) {
            for (int p = 0; p < strokes.get(s).size(); p++) {
                Point point = strokes.get(s).get(p);
                points.add(new Point(point.getX(), point.getY())); // extract x, y coordinates from strokes and append to points list
            }
        }
        return points;
    }

    public static double angleBetweenUnitVectors(double[] v1, double[] v2) {
        double n = v1[0] * v2[0] + v1[1] * v2[1];
        double c = Math.max(-1.0, Math.min(1.0, n));
        return Math.acos(c);
    }

    public static double getDistanceAtBestAngle(ArrayList<Point> pointsA, ArrayList<Point> pointsB, double a, double b, double threshold) {
        double x1 = Phi * a + (1.0 - Phi) * b;
        double f1 = getDistanceAtAngle(pointsA, pointsB, x1);
        double x2 = (1.0 - Phi) * a + Phi * b;
        double f2 = getDistanceAtAngle(pointsA, pointsB, x2);
        while (Math.abs(b - a) > threshold) {
            if (f1 < f2) {
                b = x2;
                x2 = x1;
                f2 = f1;
                x1 = Phi * a + (1.0 - Phi) * b;
                f1 = getDistanceAtAngle(pointsA, pointsB, x1);
            } else {
                a = x1;
                x1 = x2;
                f1 = f2;
                x2 = (1.0 - Phi) * a + Phi * b;
                f2 = getDistanceAtAngle(pointsA, pointsB, x2);
            }
        }
        return Math.min(f1, f2);
    }

    public static double getPathDistance(ArrayList<Point> pts1, ArrayList<Point> pts2) {
        double d = 0.0;
        for (int i = 0; i < pts1.size(); i++) {
            d += getDistance(pts1.get(i), pts2.get(i));
        }
        return d / pts1.size();
    }

    public static double getDistanceAtAngle(ArrayList<Point> pointsA, ArrayList<Point> pointsB, double radians) {
        ArrayList<Point> newPoints = rotateBy(pointsA, radians);
        return getPathDistance(newPoints, pointsB);
    }

    public static ArrayList<Point> rotateBy(ArrayList<Point> points, double radians) {
        Point c = getCentroid(points);
        double cosine = Math.cos(radians);
        double sine = Math.sin(radians);
        ArrayList<Point> newPoints = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            double qx = ((points.get(i).getX() - c.getX()) * cosine - (points.get(i).getY() - c.getY()) * sine + c.getX());
            double qy = ((points.get(i).getX() - c.getX()) * sine + (points.get(i).getY() - c.getY()) * cosine + c.getY());
            newPoints.add(new Point(qx, qy));
        }
        return newPoints;
    }

    public static ArrayList<Point> scaleDimTo(ArrayList<Point> points, double size, double ratio1D) {
        Rectangle R = getBoundingBox(points);
        if (R.getHeight() == 0) {
            R.setHeight(0.1);
        }
        if (R.getWidth() == 0) {
            R.setHeight(0.1);
        }
        boolean uniformly = Math.min(R.getWidth() / R.getHeight(), R.getHeight() / R.getWidth()) <= ratio1D;
        ArrayList<Point> newPoints = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            double qx = points.get(i).getX() * (size / Math.max(R.getWidth(), R.getHeight()));
            double qy = points.get(i).getY() * (size / Math.max(R.getWidth(), R.getHeight()));
            if (!uniformly) {
                qx = points.get(i).getX() * (size / R.getWidth());
                qy = points.get(i).getY() * (size / R.getHeight());
            }
            newPoints.add(new Point(qx, qy));
        }
        return newPoints;
    }

    public static ArrayList<Point> translateTo(ArrayList<Point> points, Point pt) {
        Point centroid = getCentroid(points);
        ArrayList<Point> newPoints = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            double qx = points.get(i).getX() + pt.getX() - centroid.getX();
            double qy = points.get(i).getY() + pt.getY() - centroid.getY();
            newPoints.add(new Point(qx, qy));
        }
        return newPoints;
    }

    public static double deg2rad(double d) {
        return (d * Math.PI / 180.0); // Convert angle from degrees to radians
    }

    public static double[] calcStartUnitVector(ArrayList<Point> points, int index) {
        double[] v = {points.get(index).getX() - points.get(0).getX(), points.get(index).getY() - points.get(0).getY()};
        double len = Math.sqrt((v[0] * v[0]) + (v[1] * v[1]));
        return new double[]{v[0] / len, v[1] / len};
    }

    public static double getIndicativeAngle(ArrayList<Point> points) {
        Point c = getCentroid(points);
        return Math.atan2(c.getY() - points.get(0).getY(), c.getX() - points.get(0).getX());
    }

    public static Point getCentroid(ArrayList<Point> points) {
        double x = 0.0;
        double y = 0.0;
        for (Point p : points) {
            x += p.getX();
            y += p.getY();
        }
        x /= points.size();
        y /= points.size();
        return new Point(x, y);
    }

    public static double getPathLength(ArrayList<Point> points) {
        double length = 0.0;
        for (int i = 1; i < points.size(); i++) {
            length += getDistance(points.get(i - 1), points.get(i));
        }
        return length;
    }

    public static double getDistance(Point p1, Point p2) {
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
