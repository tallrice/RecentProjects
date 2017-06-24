import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.ArrayList;
    
public class BruteCollinearPoints {

    private LineSegment[] lsArray;
    private int numOfSegs;

    public BruteCollinearPoints(Point[] points) {
    // finds all line segments containing 4 points
        Point[] fourPoints;
        fourPoints = new Point[4];
        LineSegment ls;
        ArrayList<LineSegment> lsArrayList = new ArrayList<LineSegment>();
        if (points == null) {
            throw new java.lang.NullPointerException(
            "Null point arrays not allowed");
        }
        int n = points.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j > i) {
                    checkForDupes(points[i], points[j]);
                    double slopeItoJ = points[i].slopeTo(points[j]);
                    for (int k = 0; k < n; k++) {
                        if (k > i && k > j) {
                            checkForDupes(points[i], points[k]);
                            checkForDupes(points[j], points[k]);
                            double slopeJtoK = points[j].slopeTo(points[k]);
                            for (int l = 0; l < n; l++) {
                                if (l > i && l > j && l > k) {
                                    checkForDupes(points[i], points[l]);
                                    checkForDupes(points[j], points[l]);
                                    checkForDupes(points[k], points[l]);
                                    double slopeKtoL = points[k].slopeTo(points[l]);
                                    if (points[i] == null ||
                                        points[j] == null ||
                                        points[k] == null ||
                                        points[l] == null) {
                                        throw new java.lang.NullPointerException(
                                                "Null points not allowed");
                                    }
                                    if ((slopeItoJ == slopeJtoK) &&
                                        (slopeJtoK == slopeKtoL)) {
                                        // assign the four points to the array
                                        fourPoints[0] = points[i];
                                        fourPoints[1] = points[j];
                                        fourPoints[2] = points[k];
                                        fourPoints[3] = points[l];
                                        // sort the array
                                        Arrays.sort(fourPoints);
                                        // use the first and last points of 
                                        // the sorted
                                        // array to create the line segment
                                        ls = new LineSegment(
                                                fourPoints[0], fourPoints[3]);
                                        // add the line segment to 
                                        // the line segment set
                                        lsArrayList.add(ls);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // convert the line segment set to an array
        numOfSegs = 0;
        lsArray = new LineSegment[lsArrayList.size()];
        for (LineSegment ls1 : lsArrayList) {
            lsArray[numOfSegs] = ls1;
            numOfSegs++;
        }
    }

    private void checkForDupes(Point pi, Point pj) {
        if (pi.compareTo(pj) == 0) {
            throw new java.lang.IllegalArgumentException(
                    "Repeated points not allowed");
        }
    }

    public int numberOfSegments() {
    // the number of line segments
        return numOfSegs;
    }
    public LineSegment[] segments() {
    // the line segments
        LineSegment[] lsA;
        lsA = Arrays.copyOf(lsArray, numOfSegs);
        return lsA;
    }

public static void main(String[] args) {

    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
        int x = in.readInt();
        int y = in.readInt();
        points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
        p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
        StdOut.println(segment);
        segment.draw();
    }
    StdDraw.show();
}
}
