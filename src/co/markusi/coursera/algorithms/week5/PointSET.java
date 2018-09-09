package co.markusi.coursera.algorithms.week5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class PointSET {

    private final SET<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        set = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validateInput(p);
        set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        validateInput(p);
        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D point : set) {
            StdDraw.point(point.x(), point.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        validateInput(rect);
        List<Point2D> list = new ArrayList<>();
        for (Point2D point : set) {
            if (rect.contains(point)) {
                list.add(point);
            }
        }
        return list;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        validateInput(p);
        Point2D nearest = null;
        double minDistanceSquaredTo = Integer.MAX_VALUE;
        double distanceSquaredTo;
        for (Point2D point : set) {
            distanceSquaredTo = point.distanceSquaredTo(p);
            if (minDistanceSquaredTo > distanceSquaredTo) {
                minDistanceSquaredTo = distanceSquaredTo;
                nearest = point;
            }
        }
        return nearest;
    }

    private void validateInput(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Null input");
        }
    }
}