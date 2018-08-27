package co.markusi.coursera.algorithms.week3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private static final int POINTS_MIN_LENGTH = 4;
    private final List<LineSegment> segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        Point[] pointsCopy = copyOf(points);
        validateInput(pointsCopy);
        segments = new ArrayList<>();
        findSegments(pointsCopy);
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] output = new LineSegment[segments.size()];
        for (int i = 0; i < segments.size(); i++) {
            output[i] = segments.get(i);
        }
        return output;
    }

    private void findSegments(Point[] points) {
        int length = points.length;

        if (length < POINTS_MIN_LENGTH) {
            return;
        }

        Point pPoint;
        Point qPoint;
        Point rPoint;
        Point sPoint;
        for (int p = 0; p < length; p++) {
            for (int q = p + 1; q < length; q++) {
                for (int r = q + 1; r < length; r++) {
                    for (int s = r + 1; s < length; s++) {
                        pPoint = points[p];
                        qPoint = points[q];
                        rPoint = points[r];
                        sPoint = points[s];
                        double firstSlope = pPoint.slopeTo(qPoint);
                        double secondSlope = pPoint.slopeTo(rPoint);
                        if (firstSlope != secondSlope) {
                            continue;
                        }
                        double thirdSlope = pPoint.slopeTo(sPoint);
                        if (firstSlope == secondSlope && firstSlope == thirdSlope) {
                            segments.add(new LineSegment(pPoint, sPoint));
                        }
                    }
                }
            }
        }
    }

    private Point[] copyOf(Point[] points) {
        if (points == null) {
            return null;
        }
        Point[] pointsCopy = new Point[points.length];
        System.arraycopy(points, 0, pointsCopy, 0, points.length);
        return pointsCopy;
    }

    private void validateInput(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points are null!");
        } else if (nullPointExists(points)) {
            throw new IllegalArgumentException("One of the points is null!");
        } else if (repeatedPointExists(points)) {
            throw new IllegalArgumentException("Repeated point exists!");
        }
    }

    private boolean nullPointExists(Point[] points) {
        for (Point point : points) {
            if (point == null) {
                return true;
            }
        }
        return false;
    }

    private boolean repeatedPointExists(Point[] points) {
        Arrays.sort(points);
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(points[i - 1]) == 0) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        // input8.txt
        Point[] points = new Point[]{
                new Point(10000, 0),
                new Point(0, 10000),
                new Point(3000, 7000),
                new Point(7000, 3000),
                new Point(20000, 21000),
                new Point(3000, 4000),
                new Point(14000, 15000),
                new Point(6000, 7000)
        };
        BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);

        assert bruteCollinearPoints.numberOfSegments() == 2;
        assert bruteCollinearPoints.segments()[0].toString().equals("(10000, 0) -> (0, 10000)");
        assert bruteCollinearPoints.segments()[1].toString().equals("(3000, 4000) -> (20000, 21000)");
    }
}