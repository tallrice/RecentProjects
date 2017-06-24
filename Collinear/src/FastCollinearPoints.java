import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.ArrayList;
    
public class FastCollinearPoints {

    private ArrayList<LineSegment> lsArrayList = new ArrayList<LineSegment>();
    private int numOfSegs;

    public FastCollinearPoints(Point[] points) {
    // finds all line segments containing 4 points
        // corner cases
        if (points == null) {
            throw new java.lang.NullPointerException(
            "Null point arrays not allowed");
        }
        int n = points.length;
        int ignore = 0;
        double slope;
        boolean pass;
        double lastSlope;
        int counterDupes;
        int m;
        Point max;
        Point min;
        LineSegment ls;
        numOfSegs = 0;
        Point[] pointsSorted1;
        ArrayList<Point> ignorePointMax = new ArrayList<Point>();
        ArrayList<Point> ignorePointMin = new ArrayList<Point>();
        ArrayList<Double> ignoreSlope = new ArrayList<Double>();
        pointsSorted1 = Arrays.copyOf(points, n);
        for (int i = 0; i < n; i++) {
            min = pointsSorted1[i];
            max = pointsSorted1[i];
            lastSlope = Double.NaN;
            counterDupes = 1;
            // corner cases
            if (i > 0 && pointsSorted1[i].compareTo(pointsSorted1[i-1]) == 0) {
                throw new java.lang.IllegalArgumentException(
                        "Repeated points not allowed");
            }
            // sort the array by slope order
            Arrays.sort(pointsSorted1, i, n, pointsSorted1[i].slopeOrder());
            for (int j = i; (j < n); j++) {
                // create array of slopes
                slope = pointsSorted1[i].slopeTo(pointsSorted1[j]);
                // if current slope is different than last slope,
                // or if the last point in the array is reached,
                // determine if the line segment should be saved
                if (slope != lastSlope) {
                    // if there are 3 or more duplicates
                    if (counterDupes >= 3) {
                        pass = false;
                        for (int q = 0; q < ignore; q++) {
                            // check to see if this slope and max/min point
                            // should be ignored
                            if (lastSlope == ignoreSlope.get(q) &&
                                    (max == ignorePointMax.get(q) ||
                                     min == ignorePointMin.get(q))) {
                                pass = true;
                            }
                        }
                        // ignore combination of slope and max/min point
                        // for future line segments
                        if (counterDupes > 3) {
                            ignorePointMax.add(max);
                            ignorePointMin.add(min);
                            ignoreSlope.add(lastSlope);
                            ignore++;
                        }
                        if (!pass) {
                            // create line segment
                            ls = new LineSegment(min, max);
                            // increase # of line segments
                            numOfSegs++;
                            // add the line segment to the array
                            lsArrayList.add(ls);
                        }
                    }
                    // reset counter
                    counterDupes = 1;
                    min = pointsSorted1[i];
                    max = pointsSorted1[i];
                    if (pointsSorted1[j].compareTo(max) > 0) max = pointsSorted1[j];
                    if (pointsSorted1[j].compareTo(min) < 0) min = 
                        pointsSorted1[j];
                }
                else {
                    // increment counter
                    counterDupes++;
                    if (pointsSorted1[j].compareTo(max) > 0) max = pointsSorted1[j];
                    if (pointsSorted1[j].compareTo(min) < 0) min = 
                        pointsSorted1[j];
                }
                // reset lastSlope
                lastSlope = slope;
            }
            if (counterDupes >= 3) {
                pass = false;
                for (int p = 0; p < ignore; p++) {
                    // check to see if this slope and max/min point
                    // should be ignored
                    if (lastSlope == ignoreSlope.get(p) &&
                            (max == ignorePointMax.get(p) ||
                             min == ignorePointMin.get(p))) {
                        pass = true;
                    }
                }
                // ignore combination of slope max/min point
                // for future line segments
                if (counterDupes > 3) {
                    ignorePointMax.add(max);
                    ignorePointMin.add(min);
                    ignoreSlope.add(lastSlope);
                    ignore++;
                }
                if (!pass) {
                    // create line segment
                    ls = new LineSegment(min, max);
                    // increase # of line segments
                    numOfSegs++;
                    // add the line segment to the array
                    lsArrayList.add(ls);
                }
            }
            // reset counter
            counterDupes = 1;
        }
    }

    public int numberOfSegments() {
    // the number of line segments
        return numOfSegs;
    }
    public LineSegment[] segments() {
    // the line segments
        LineSegment[] lsA;
        lsA = new LineSegment[numOfSegs];
        for (int i = 0; i < numOfSegs; i++) {
            lsA[i] = lsArrayList.get(i);
        }
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
