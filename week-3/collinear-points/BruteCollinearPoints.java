import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    
    private LineSegment[] lineSegments;
    
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new NullPointerException("points array is null");
        }
        
        Arrays.sort(points, 0, points.length);
        Point prev = null;
        for (Point point : points) {
            if (prev == null) {
                prev = point;
            } else {
                if (prev.compareTo(point) == 0) {
                    throw new IllegalArgumentException("Repeated points");
                }
                prev = point;
            }
        }
        
        int length = points.length;
        LineSegment[] lineSegment = new LineSegment[length];
        int numOfSeg = 0;
        
        for (int p = 0; p < length; p++) {
            for (int q = p+1; q < length; q++) {
                double slope1 = points[p].slopeTo(points[q]);
                for (int r = q+1; r < length; r++) {
                    double slope2 = points[p].slopeTo(points[r]);
                    if (slope1 != slope2) {
                        continue;
                    }
                    for (int s = r+1; s < length; s++) {
                        double slope3 = points[p].slopeTo(points[s]);
                        if (slope1 == slope3) {
                            lineSegment[numOfSeg++] = 
                                new LineSegment(points[p], points[s]);
                        }
                    }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}