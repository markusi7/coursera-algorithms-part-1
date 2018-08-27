package co.markusi.coursera.algorithms.week3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private static final int POINTS_MIN_LENGTH = 4;
    private final List<LineSegment> segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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
        if (points.length < POINTS_MIN_LENGTH) {
            return;
        }
        Point[] auxPoints = new Point[points.length];
        System.arraycopy(points, 0, auxPoints, 0, points.length);

        for (Point point : points) {
            Arrays.sort(auxPoints, point.slopeOrder());
            double previousSlopeOrder = point.slopeTo(auxPoints[1]);
            int subsequentCount = 1;
            for (int i = 2; i < auxPoints.length; i++) {
                double slopeOrder = point.slopeTo(auxPoints[i]);
                if (slopeOrder != previousSlopeOrder) {
                    tryToFindASegment(subsequentCount, i, point, auxPoints, false);
                    subsequentCount = 1;
                } else if (slopeOrder == previousSlopeOrder && hasReachedEnd(i, auxPoints.length)) {
                    subsequentCount++;
                    tryToFindASegment(subsequentCount, i, point, auxPoints, true);
                } else {
                    subsequentCount++;
                }
                previousSlopeOrder = slopeOrder;
            }
        }
    }

    private void tryToFindASegment(int subsequentCount, int endIndex, Point point, Point[] auxPoints,
                                   boolean isLastSubsequent) {
        int totalCount = subsequentCount + 1;
        if (totalCount < POINTS_MIN_LENGTH) {
            return;
        }
        int lastIndex;
        if (isLastSubsequent) {
            lastIndex = endIndex;
        } else {
            lastIndex = endIndex - 1;
        }
        int firstIndex = lastIndex - subsequentCount + 1;
        Arrays.sort(auxPoints, firstIndex, lastIndex + 1);
        if (point.compareTo(auxPoints[firstIndex]) < 0) {
            segments.add(new LineSegment(point, auxPoints[lastIndex]));
        }
    }

    private boolean hasReachedEnd(int i, int length) {
        return i == length - 1;
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
        // input6.txt
        Point[] points = new Point[]{
                new Point(19000, 10000),
                new Point(18000, 10000),
                new Point(32000, 10000),
                new Point(21000, 10000),
                new Point(1234, 5678),
                new Point(14000, 10000),
        };

        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);

        assert fastCollinearPoints.numberOfSegments() == 1;
        assert fastCollinearPoints.segments()[0].toString().equals("(14000, 10000) -> (32000, 10000)");
    }
}