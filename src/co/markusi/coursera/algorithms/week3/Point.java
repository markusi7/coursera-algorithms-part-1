package co.markusi.coursera.algorithms.week3; /******************************************************************************
 *  Compilation:  javac co.markusi.coursera.algorithms.week3.Point.java
 *  Execution:    java co.markusi.coursera.algorithms.week3.Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        if (this.compareTo(that) == 0) {
            return Double.NEGATIVE_INFINITY;
        } else if (this.x == that.x) {
            return Double.POSITIVE_INFINITY;
        } else if (this.y == that.y) {
            return 0;
        } else {
            return 1.0d * (that.y - this.y) / (that.x - this.x);
        }
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     * point (x0 = x1 and y0 = y1);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    public int compareTo(Point that) {
        if (this.y < that.y) {
            return -1;
        } else if (this.y > that.y) {
            return 1;
        } else return Integer.compare(this.x, that.x);
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return (first, second) -> {
            double firstSlope = this.slopeTo(first);
            double secondSlope = this.slopeTo(second);
            return Double.compare(firstSlope, secondSlope);
        };
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the co.markusi.coursera.algorithms.week3.Point data type.
     */
    public static void main(String[] args) {
        testCompareTo();
        testSlopeTo();
        testSlopeComparator();
    }

    private static void testCompareTo() {
        Point point = new Point(2, 2);

        Point smallerYPoint = new Point(2, 1);
        Point smallerXPoint = new Point(1, 2);
        Point sameValuePoint = new Point(2, 2);

        assert point.compareTo(smallerYPoint) > 0;
        assert smallerYPoint.compareTo(point) < 0;
        assert point.compareTo(smallerXPoint) > 0;
        assert smallerXPoint.compareTo(point) < 0;
        assert point.compareTo(sameValuePoint) == 0;
    }

    private static void testSlopeTo() {
        Point point = new Point(2, 2);

        Point upperLeftPoint = new Point(1, 3);
        Point upperRightPoint = new Point(3, 3);
        Point downerLeftPoint = new Point(1, 1);
        Point downerRightPoint = new Point(3, 1);
        Point sameHorizontalPoint = new Point(2, 3);
        Point sameVerticalPoint = new Point(3, 2);
        Point sameValuePoint = new Point(2, 2);

        assert point.slopeTo(upperLeftPoint) == -1;
        assert point.slopeTo(upperRightPoint) == 1;
        assert point.slopeTo(downerLeftPoint) == 1;
        assert point.slopeTo(downerRightPoint) == -1;
        assert point.slopeTo(sameHorizontalPoint) == Double.POSITIVE_INFINITY;
        assert point.slopeTo(sameVerticalPoint) == 0;
        assert point.slopeTo(sameValuePoint) == Double.NEGATIVE_INFINITY;
    }

    private static void testSlopeComparator() {
        Point point = new Point(2, 2);

        Point upperLeftPoint = new Point(1, 3);
        Point upperRightPoint = new Point(3, 3);
        Point downerLeftPoint = new Point(1, 1);
        Point downerRightPoint = new Point(3, 1);
        Point sameHorizontalPoint = new Point(2, 3);
        Point sameVerticalPoint = new Point(3, 2);
        Point sameValuePoint = new Point(2, 2);

        List<Point> points = new ArrayList<>();
        points.add(upperLeftPoint);
        points.add(upperRightPoint);
        points.add(downerLeftPoint);
        points.add(downerRightPoint);
        points.add(sameHorizontalPoint);
        points.add(sameVerticalPoint);
        points.add(sameValuePoint);

        points.sort(point.slopeOrder());

        assert points.get(0).compareTo(sameValuePoint) == 0;
        assert points.get(1).compareTo(upperLeftPoint) == 0;
        assert points.get(2).compareTo(downerRightPoint) == 0;
        assert points.get(3).compareTo(sameVerticalPoint) == 0;
        assert points.get(4).compareTo(upperRightPoint) == 0;
        assert points.get(5).compareTo(downerLeftPoint) == 0;
        assert points.get(6).compareTo(sameHorizontalPoint) == 0;
    }
}