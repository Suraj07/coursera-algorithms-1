import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Queue;

public class PointSET {
    
    private SET<Point2D> set;
    
    // construct an empty set of points 
    public PointSET() {
        set = new SET<Point2D>();
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
        if (p == null) {
            throw new NullPointerException("argument is null");
        }
        
        if (!set.contains(p)) {
            set.add(p);
        }
    }
    
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new NullPointerException("argument is null");
        }
        
        return set.contains(p);
    }
    
    // draw all points to standard draw 
    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }
    
    // all points that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException("argument is null");
        }
        
        Queue<Point2D> q = new Queue<Point2D>();
        for (Point2D p : set) {
            if (rect.contains(p)) {
                q.enqueue(p);
            }
        }
        return q;
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException("argument is null");
        }
        
        if (isEmpty()) {
            return null;
        }
        
        Point2D ans = null;
        double min = -1.0;
        
        for (Point2D pt : set) {
            if (min == -1.0) {
                ans = pt;
                min = pt.distanceSquaredTo(p);
            } else {
                double dist = pt.distanceSquaredTo(p);
                if (min > dist) {
                    min = dist;
                    ans = pt;
                }
            }
        }
        
        return ans;
    }
}