import java.util.ArrayDeque;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {

    private Node root;

    public KdTree() { // construct an empty set of points
       root = new Node(0, 1, 0, 1);
    }
    private static class Node {
        private Point2D p; // the point
        private RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb; // the left/bottom subtree
        private Node rt; // the right/top subtree
        private int N; // # nodes in subtree rooted here
        private boolean isVertical; //

        public Node(double xmin, double xmax, double ymin, double ymax) {
            this.rect = new RectHV(xmin, ymin, xmax, ymax);
            this.N = 0;
        }
        public Node(Point2D p, int N, boolean isVertical,
                    double xmin, double xmax, double ymin, double ymax) {
            this.p = p;
            this.N = N;
            this.isVertical = isVertical;
            this.rect = new RectHV(xmin, ymin, xmax, ymax);
        }
    }
    public boolean isEmpty() { // is the set empty?
        return root.N == 0;
    }
    public int size() { // number of points in the set
        return size(root);
    }
    private int size(Node n) {
        if (n == null) 
            throw new NullPointerException("Null argument not allowed");
        if (n == null) return 0;
        else           return n.N;
    }
    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        if (p == null) 
            throw new NullPointerException("Null argument not allowed");
        int xmin = 0;
        int xmax = 1;
        int ymin = 0;
        int ymax = 1;
        root = insert(root, p, true, xmin, xmax, ymin, ymax);
    }
    private Node insert(Node n, Point2D p, boolean isVertical,
                        double xmin, double xmax, double ymin, double ymax) {
        if (n.p == null) {
            Node lb = new Node(0,0,0,0);
            Node rt = new Node(0,0,0,0);
            if (isVertical) {
                double x = p.x();
                lb = new Node(xmin, x, ymin, ymax);
                rt = new Node(x, xmax, ymin, ymax);
            }
            if (!isVertical) {
                double y = p.y();
                lb = new Node(xmin, xmax, ymin, y);
                rt = new Node(xmin, xmax, y, ymax);
            }
            Node newNode = new Node(p, 1, isVertical, xmin, xmax, ymin, ymax);
            newNode.lb = lb;
            newNode.rt = rt;
            return newNode;
        }
        int cmp = 0;
        if (n.p.equals(p)) {
            // n.p = p;
            // n.N = size(n.lb) + size(n.rt) + 1;
            return n;
        }
        if (isVertical) {
            cmp = Point2D.X_ORDER.compare(p, n.p);
            isVertical = false;
        }
        else {
            cmp = Point2D.Y_ORDER.compare(p, n.p);
            isVertical = true;
        }
        if      (cmp <= 0) {
            if (isVertical) ymax = n.p.y();
            if (!isVertical) xmax = n.p.x();
            n.lb = insert(n.lb, p, isVertical, xmin, xmax, ymin, ymax);
        }
        else if (cmp > 0) {
            if (isVertical) ymin = n.p.y();
            if (!isVertical) xmin = n.p.x();
            n.rt = insert(n.rt, p, isVertical, xmin, xmax, ymin, ymax);
        }
        n.N = size(n.lb) + size(n.rt) + 1;
        return n;
    }
    public boolean contains(Point2D p) { // does the set contain point p?
        if (p == null) 
            throw new NullPointerException("Null argument not allowed");
        return contains(root, p, true);
    }
    private boolean contains(Node n, Point2D p, boolean isVertical) {
        if (n.p == null) return false;
        int cmp = 0;
        if (n.p.equals(p)) {
            return true;
        }
        if (isVertical) {
            cmp = Point2D.X_ORDER.compare(p, n.p);
            isVertical = false;
        }
        else {
            cmp = Point2D.Y_ORDER.compare(p, n.p);
            isVertical = true;
        }
        if      (cmp <= 0)  return contains(n.lb, p, isVertical);
        else                return contains(n.rt, p, isVertical);
    }

    public void draw() { // draw all points to standard draw
        StdDraw.clear();
        draw(root);
        StdDraw.show();
    }
    private void draw(Node n) {
        if (n.p != null) {
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            n.p.draw();
        }
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius();
        n.rect.draw();
        if (n.lb != null) {
            draw(n.lb);
        }
        if (n.rt != null) {
            draw(n.rt);
        }
    }
    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle
        if (rect == null) 
            throw new NullPointerException("Null argument not allowed");
        ArrayDeque<Point2D> ad = new ArrayDeque<>();
        rangeAdd(rect, root, ad);
        return ad;
    }

    private void rangeAdd(RectHV rect, Node n, ArrayDeque<Point2D> ad) {
        if (n.rect.intersects(rect) && n.p != null) {
            if (rect.contains(n.p)) ad.add(n.p);
            rangeAdd(rect, n.lb, ad);
            rangeAdd(rect, n.rt, ad);
        }
    }

    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) 
            throw new NullPointerException("Null argument not allowed");
        if (isEmpty()) return null;
        Point2D champion = bestPoint(root, p, root.p);
        return champion;
    }

    private Point2D bestPoint(Node root, Point2D p, Point2D champion) {
        if (root.p == null) return champion;
        double distHorz = root.p.x() - p.x();
        distHorz *= distHorz;
        double distVert = root.p.y() - p.y();
        distVert *= distVert;
        double distToSplit = 2.0;
        double distToNearEdge = 2.0;
        boolean rt = false;
        boolean lb = false;
        double distSquaredToRt = root.rt.rect.distanceSquaredTo(p);
        double distSquaredToLb = root.lb.rect.distanceSquaredTo(p);
        // 1. check root point
        if (p.distanceSquaredTo(root.p) < p.distanceSquaredTo(champion))
            champion = root.p;
        // 2. check near rectangle (right/top)
        if (distSquaredToRt <= distSquaredToLb &&
            distSquaredToRt < p.distanceSquaredTo(champion)) {
            champion = bestPoint(root.rt, p, champion);
            // 3. check far rectangle, if necessary
            if (distSquaredToLb < p.distanceSquaredTo(champion))
                champion = bestPoint(root.lb, p, champion);
        }
        // 2. check near rectangle (left/bottom)
        else if (distSquaredToLb <= distSquaredToRt &&
            distSquaredToLb < p.distanceSquaredTo(champion)) {
            champion = bestPoint(root.lb, p, champion);
            // 3. check far rectangle, if necessary
            if (distSquaredToRt < p.distanceSquaredTo(champion))
                champion = bestPoint(root.rt, p, champion);
        }
        return champion;
    }

    public static void main(String[] args) { // unit testing of the methods (optional)
        KdTree kdt = new KdTree();
        Point2D p = new Point2D(0.5,0.5);
        Point2D p1 = new Point2D(0.3,0.3);
        Point2D p2 = new Point2D(0.2,0.2);
        Point2D p5 = new Point2D(0.1,0.1);
        Point2D p6 = new Point2D(0.6,0.6);
        kdt.insert(p);
        kdt.insert(p1);
        kdt.insert(p2);
        System.out.println(kdt.contains(p));
        System.out.println(kdt.contains(p1));
        System.out.println(kdt.contains(p2));
        kdt.draw();
        RectHV aset = new RectHV(0,0,1,1);
        for (Point2D p4 : kdt.range(aset)) System.out.println(p4);
        System.out.println(kdt.nearest(p5));
        System.out.println(kdt.nearest(p6));
    }
}
