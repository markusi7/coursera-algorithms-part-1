package co.markusi.coursera.algorithms.week5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private Node root;
    private int size;
    private Point2D nearest;
    private double nearestDistance;

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validateInput(p);
        root = insert(root, root, p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        validateInput(p);
        return get(root, p) != null;
    }

    // draw all points to standard draw
    public void draw() {
        drawNode(root);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        validateInput(rect);
        List<Point2D> points = new ArrayList<>();
        addToRange(points, rect, root);
        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        validateInput(p);
        if (size == 0) {
            return null;
        }
        nearest = root.p;
        nearestDistance = nearest.distanceSquaredTo(p);
        findNearest(p, root);
        return nearest;
    }

    private Point2D findNearest(Point2D p, Node node) {
        if (node == null) return null;
        if (node == root) {
            findNearest(p, node.lb);
            findNearest(p, node.rt);
        }
        double distance = node.p.distanceSquaredTo(p);
        if (nearestDistance > distance) {
            nearestDistance = distance;
            nearest = node.p;
        }
        double lbDistance = node.lb != null ? node.lb.rect.distanceSquaredTo(p) : nearestDistance;
        boolean shouldVisitLb = lbDistance < nearestDistance;
        double rTDistance = node.rt != null ? node.rt.rect.distanceSquaredTo(p) : nearestDistance;
        boolean shouldVisitRt = rTDistance < nearestDistance;
        if (shouldVisitLb && shouldVisitRt) {
            if ((node.horizontalSplit && p.x() < node.p.x()) || (!node.horizontalSplit && p.y() < node.p.y())) {
                Point2D nearer = findNearest(p, node.lb);
                if (nearer != null) {
                    return nearer;
                } else {
                    return findNearest(p, node.rt);
                }
            } else {
                Point2D nearer = findNearest(p, node.rt);
                if (nearer != null) {
                    return nearer;
                } else {
                    return findNearest(p, node.lb);
                }
            }
        } else if (shouldVisitLb) {
            return findNearest(p, node.lb);
        } else if (shouldVisitRt) {
            return findNearest(p, node.rt);
        }
        return null;
    }

    private Node insert(Node node, Node parentNode, Point2D point) {
        if (parentNode == null) {
            RectHV rect = createRectangle(point, null);
            size++;
            return new Node(point, rect, true);
        }
        if (node == null) {
            RectHV rect = createRectangle(point, parentNode);
            size++;
            return new Node(point, rect, !parentNode.horizontalSplit);
        }
        if (node.p.equals(point)) return node;
        if (node.horizontalSplit) {
            if (point.x() >= node.p.x()) {
                node.rt = insert(node.rt, node, point);
            } else {
                node.lb = insert(node.lb, node, point);
            }
        } else {
            if (point.y() >= node.p.y()) {
                node.rt = insert(node.rt, node, point);
            } else {
                node.lb = insert(node.lb, node, point);
            }
        }
        return node;
    }

    private RectHV createRectangle(Point2D point, Node parentNode) {
        if (parentNode == null) {
            return new RectHV(0, 0, 1, 1);
        }
        Point2D parentPoint = parentNode.p;
        RectHV parentRect = parentNode.rect;
        if (parentNode.horizontalSplit) {
            if (point.x() < parentPoint.x()) {
                return new RectHV(parentRect.xmin(), parentRect.ymin(), parentPoint.x(), parentRect.ymax());
            } else {
                return new RectHV(parentPoint.x(), parentRect.ymin(), parentRect.xmax(), parentRect.ymax());
            }
        } else {
            if (point.y() < parentPoint.y()) {
                return new RectHV(parentRect.xmin(), parentRect.ymin(), parentRect.xmax(), parentPoint.y());
            } else {
                return new RectHV(parentRect.xmin(), parentPoint.y(), parentRect.xmax(), parentRect.ymax());
            }
        }
    }

    private Node get(Node node, Point2D point) {
        if (node == null) return null;
        if (node.p.equals(point)) return node;
        boolean lookToRight = (node.horizontalSplit && point.x() >= node.p.x()) ||
                (!node.horizontalSplit && point.y() >= node.p.y());
        return get(lookToRight ? node.rt : node.lb, point);
    }

    private void drawNode(Node node) {
        if (node == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(node.p.x(), node.p.y());
        StdDraw.setPenColor(node.horizontalSplit ? StdDraw.RED : StdDraw.BLUE);
        StdDraw.setPenRadius();
        node.rect.draw();

        drawNode(node.lb);
        drawNode(node.rt);
    }

    private void addToRange(List<Point2D> points, RectHV rect, Node node) {
        if (node == null) return;
        if (rect.contains(node.p)) points.add(node.p);
        RectHV nodeRect;
        if (node.horizontalSplit) {
            nodeRect =  new RectHV(node.p.x(), 0, node.p.x(), 1);
        } else {
            nodeRect = new RectHV(0, node.p.y(), 1, node.p.y());
        }
        if (nodeRect.intersects(rect)) {
            addToRange(points, rect, node.rt);
            addToRange(points, rect, node.lb);
        } else {
            boolean lookToRight = (node.horizontalSplit && rect.xmax() >= node.p.x()) ||
                    (!node.horizontalSplit && rect.ymin() >= node.p.y());
            addToRange(points, rect, lookToRight ? node.rt : node.lb);
        }
    }

    private void validateInput(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Null input");
        }
    }

    private static class Node {
        private final Point2D p;
        private final RectHV rect;
        private Node lb;
        private Node rt;
        private final boolean horizontalSplit;

        public Node(Point2D p, RectHV rect, boolean horizontalSplit) {
            this.p = p;
            this.rect = rect;
            this.horizontalSplit = horizontalSplit;
        }
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.7, 0.2));
        kdTree.insert(new Point2D(0.5, 0.4));
        kdTree.insert(new Point2D(0.2, 0.3));
        kdTree.insert(new Point2D(0.4, 0.7));
        kdTree.insert(new Point2D(0.9, 0.6));
    }
}