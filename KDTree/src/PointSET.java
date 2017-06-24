import java.util.TreeSet;
import java.util.LinkedList;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {

    private TreeSet<Point2D> ts;

    public PointSET() { // construct an empty set of points
        ts = new TreeSet<>();
        // ad = new ArrayDeque<>();
    }

    public boolean isEmpty() {// is the set empty?
        return ts.isEmpty();
    }

    public int size() {// number of points in the set
        return ts.size();
    }

    public void insert(Point2D p) {// add the point to the set (if it is not already in the set)
        if (p == null) 
            throw new NullPointerException("Null argument not allowed");
        ts.add(p);
        // ad.add(p);
    }

    public boolean contains(Point2D p) {// does the set contain point p?
        if (p == null) 
            throw new NullPointerException("Null argument not allowed");
        return ts.contains(p);
    }

    public void draw() { // draw all points to standard draw
    }

    public Iterable<Point2D> range(RectHV rect) {// all points that are inside the rectangle
        if (rect == null) 
            throw new NullPointerException("Null argument not allowed");
        LinkedList<Point2D> ll = new LinkedList<>();
        for (Point2D p : ts) {
            if (rect.contains(p)) {
                ll.add(p);
            }
        }
        return ll;
    }

    public Point2D nearest(Point2D p) {// a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) 
            throw new NullPointerException("Null argument not allowed");
        Point2D pNearest = ts.first();
        double distance = 2.0;
        for (Point2D pOther : ts) {
            if (p.distanceSquaredTo(pOther) <= distance) {
                pNearest = pOther;
                distance = p.distanceSquaredTo(pOther);
            }
        }
        return pNearest;
    }

    public static void main(String[] args) {// unit testing of the methods (optional)
        PointSET kdt = new PointSET();
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
