import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    
    private LineSegment[] lineSegments;
    
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] tpoints) {
        if (tpoints == null) {
            throw new NullPointerException("points array is null");
        }
        
        Arrays.sort(tpoints, 0, tpoints.length);
        Point prev = null;
        for (Point point : tpoints) {
            if (prev == null) {
                prev = point;
            } else {
                if (prev.compareTo(point) == 0) {
                    throw new IllegalArgumentException("Repeated points");
                }
                prev = point;
            }
        }

        int length = tpoints.length;        
        Point[] points = new Point[length];
        System.arraycopy(tpoints, 0, points, 0, length);

        LineSegment[] lineSegment = new LineSegment[(length * length) + 2];
        int numOfSeg = 0;
        
        Point[] linePoints = new Point[(length * length) + 2];
        
        for (int i = 0; i < length; i++) {
            Point[] sortPoints = new Point[length];
            System.arraycopy(points, 0, sortPoints, 0, length);
            Arrays.sort(sortPoints, points[i].slopeOrder());
            Point[] temp = new Point[length];
            temp[0] = sortPoints[0];
            int count = 0;
            for (int j = 1; j < (length - 1); j++) {
                if ((points[i].slopeTo(sortPoints[j]) == 
                     points[i].slopeTo(sortPoints[j + 1]))) {
                    count++;
                    temp[count] = sortPoints[j];
                    temp[count + 1] = sortPoints[j + 1];
                } else {
                    if (count > 1) {
                        Arrays.sort(temp, 0, count + 2);
                        LineSegment line = new LineSegment(temp[0], temp[count + 1]);
                        boolean add = true;
                        
                        for (int k = 0; k < numOfSeg; k++) {
                            // TODO: too many compareTo
                            if (((linePoints[2 * k].compareTo(temp[0])) == 0) && 
                                ((linePoints[(2 * k) + 1].compareTo(temp[count + 1]))
                                     == 0)) {
                                add = false;
                            }
                        }
                        
                        if (add) {
                            linePoints[2 * numOfSeg] = temp[0];
                            linePoints[(2 * numOfSeg) + 1] = temp[count + 1];
                            lineSegment[numOfSeg++] = line;
                        }
                        temp = null;
                        temp = new Point[length];
                        temp[0] = points[i];
                    }
                    count = 0;
                }
            }
            if (count > 1) {
                Arrays.sort(temp, 0, count + 2);
                LineSegment line = new LineSegment(temp[0], temp[count + 1]);
                boolean add = true;
                
                for (int k = 0; k < numOfSeg; k++) {
                    if (((linePoints[2 * k].compareTo(temp[0])) == 0) && 
                        ((linePoints[(2 * k) + 1].compareTo(temp[count + 1]))
                             == 0)) {
                        add = false;
                    }
                }
                
                if (add) {
                    linePoints[2 * numOfSeg] = temp[0];
                    linePoints[(2 * numOfSeg) + 1] = temp[count + 1];
                    lineSegment[numOfSeg++] = line;
                }
            }
        }
        lineSegments = new LineSegment[numOfSeg];
        for (int i = 0; i < numOfSeg; i++) {
            lineSegments[i] = lineSegment[i];
        }
        
    }
    
    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }
    
    // the line segments
    public LineSegment[] segments() {
        int len = numberOfSegments();
        LineSegment[] temp = new LineSegment[len];
        System.arraycopy(lineSegments, 0, temp, 0, len);
        return temp;
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
