import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;

public class KdTree {
    
    private Node root;
    private int size;
    
    // construct an empty set of points 
    public KdTree() {
        root = null;
        size = 0;
    }
    
    // is the set empty? 
    public boolean isEmpty() {
        return (root == null);
    }
    
    // number of points in the set 
    public int size() {
        return size;
    }
    
    // private recursive method to insert points in tree
    private Node put(Node node, Point2D p, boolean useX, Node parent) {
        if (node == null) {
            double xmin, ymin, xmax, ymax;
            if (parent == null) {
                xmin = 0.0;
                ymin = 0.0;
                xmax = 1.0;
                ymax = 1.0;
            } else {
                if (!useX) {
                     if (p.x() < parent.p.x()) {
                         xmin = parent.rect.xmin();
                         xmax = parent.p.x();
                         ymin = parent.rect.ymin();
                         ymax = parent.rect.ymax();
                     } else {
                         xmin = parent.p.x();
                         xmax = parent.rect.xmax();
                         ymin = parent.rect.ymin();
                         ymax = parent.rect.ymax();
                     }
                 } else {
                     if (p.y() < parent.p.y()) {
                         ymin = parent.rect.ymin();
                         ymax = parent.p.y();
                         xmin = parent.rect.xmin();
                         xmax = parent.rect.xmax();
                     } else {
                         ymin = parent.p.y();
                         ymax = parent.rect.ymax();
                         xmin = parent.rect.xmin();
                         xmax = parent.rect.xmax();
                     }
                 }
            }
            RectHV r = new RectHV(xmin, ymin, xmax, ymax);
            size++;
            return new Node(p, r, null, null);
        }
        
        if (node.p.equals(p)) {
            return node;
        }
        
        if (useX) {
            if (p.x() < node.p.x()) {
                node.lb = put(node.lb, p, false, node);
            } else {
                node.rt = put(node.rt, p, false, node);
            }
        } else {
            if (p.y() < node.p.y()) {
                node.lb = put(node.lb, p, true, node);
            } else {
                node.rt = put(node.rt, p, true, node);
            }
        }
        return node;
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new NullPointerException("argument is null");
        }
        root = put(root, p, true, null);
    }
    
    // private helper method to get a point in the tree
    private Node get(Node node, Point2D p, boolean useX) {
        if (node == null) {
            return null;
        }
        
        if (p.equals(node.p)) {
            return node;
        }
        
        if (useX) {
            if (p.x() < node.p.x()) {
                return get(node.lb, p, false);
            } else {
                return get(node.rt, p, false);
            }
        } else {
            if (p.y() < node.p.y()) {
                return get(node.lb, p, true);
            } else {
                return get(node.rt, p, true);
            }
        }
    }
    
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new NullPointerException("argument is null");
        }
        return (get(root, p, true) != null);
    }
    
    // private helper method for draw
    private void draw(Node n) {
        if (n == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(n.p.x(), n.p.y());
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.line(n.rect.xmin(), n.rect.ymin(), n.rect.xmax(), n.rect.ymin());
        StdDraw.line(n.rect.xmin(), n.rect.ymax(), n.rect.xmax(), n.rect.ymax());
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.line(n.rect.xmin(), n.rect.ymin(), n.rect.xmin(), n.rect.ymax());
        StdDraw.line(n.rect.xmax(), n.rect.ymin(), n.rect.xmax(), n.rect.ymax());
        draw(n.lb);
        draw(n.rt);
    }
    
    // draw all points to standard draw 
    public void draw() {
        draw(root);
    }
    
    // private helper method for range search
    private void rangeSearch(Node node, Queue<Point2D> q, RectHV r) {
        if (node == null) {
            return;
        }
        
        if (node.rect.intersects(r)) {
            if (r.contains(node.p)) {
                q.enqueue(node.p);
            }
            rangeSearch(node.lb, q, r);
            rangeSearch(node.rt, q, r);
            return;
        } else {
            return;
        }
    }
    
    // all points that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException("argument is null");
        }
        Queue<Point2D> q = new Queue<Point2D>();
        rangeSearch(root, q, rect);
        return q;
    }
    
    // private helper method for nearest neighbor search
    private Point2D nearestNeighbor(Point2D query, Node node, Point2D closest,
                                    double d, boolean useX) {
        if (node == null) {
            // check again
            return null;
        }
        
        Point2D pt = null;
        
        if (node == root) {
            closest = node.p;
            d = query.distanceSquaredTo(closest);
            if (query.x() < node.p.x()) {
                pt = nearestNeighbor(query, node.lb, closest, d, !useX);
                if (pt != null) {
                    closest = pt;
                    d = query.distanceSquaredTo(closest);
                }
                pt = nearestNeighbor(query, node.rt, closest, d, !useX);
                if (pt != null) {
                    closest = pt;
                }
            } else {
                pt = nearestNeighbor(query, node.rt, closest, d, !useX);
                if (pt != null) {
                    closest = pt;
                    d = query.distanceSquaredTo(closest);
                }
                pt = nearestNeighbor(query, node.lb, closest, d, !useX);
                if (pt != null) {
                    closest = pt;
                }            
            }
        } else if (node.rect.distanceSquaredTo(query) > d) {
            return null;
        } else {
            double dis = query.distanceSquaredTo(node.p);
            if (d > dis) {
                closest = node.p;
                d = dis;
            }
            boolean lFirst = false;
            if (useX) {
                if (query.x() < node.p.x()) {
                    lFirst = true;
                }
            } else {
                if (query.y() < node.p.y()) {
                    lFirst = true;
                }
            }
            if (lFirst) {
                pt = nearestNeighbor(query, node.lb, closest, d, !useX);
                if (pt != null) {
                    closest = pt;
                    d = query.distanceSquaredTo(closest);
                }
                pt = nearestNeighbor(query, node.rt, closest, d, !useX);
                if (pt != null) {
                    closest = pt;
                }
            } else {
                pt = nearestNeighbor(query, node.rt, closest, d, !useX);
                if (pt != null) {
                    closest = pt;
                    d = query.distanceSquaredTo(closest);
                }
                pt = nearestNeighbor(query, node.lb, closest, d, !useX);
                if (pt != null) {
                    closest = pt;
                }
            }
        }
        return closest;
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException("argument is null");
        }
        
        if (isEmpty()) {
            return null;
        }
        
        return nearestNeighbor(p, root, null, 0.0, true);
    }

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        
        public Node(Point2D pt, RectHV r, Node l, Node right) {
            p = pt;
            rect = r;
            lb = l;
            rt = right;
        }
    }
    
    public static void main(String[] args) {
        
        String filename = args[0];
        In in = new In(filename);
        
        StdDraw.enableDoubleBuffering();
        
        // initialize the data structures with N points from standard input
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            StdDraw.clear();
            kdtree.draw();
            StdDraw.show();
            System.out.println();
        }
        StdDraw.clear();
        kdtree.draw();
        StdDraw.show();
        System.out.println(kdtree.nearest(new Point2D(0.1, 0.6)));
    }
}
