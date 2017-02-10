/******************************************************************************
 *  Compilation:  javac KdTree.java
 *  Execution: java KdTree "points.txt"
 *  Dependencies: none
 *  @author: Alex Erhardt
 * 
 *  Princeton, Algorithms Part I on Coursera
 *  KdTree programming assignment
 * 
 *  Builds a 2D-tree structure for range search and finding nearest neighboring
 *  points in a plane. Reads in points from input and draws a representation
 *  of the tree.
 * 
 *  Design is largely inspired by the BST implementation in Algorithms, Fourth
 *  Ed. by Robert Sedgewick and Kevin Wayne.
 * 
 *  Grader output: 98 / 100
 * 
 *  The program fails a timing test in the grader. The problem resides in the
 *  nearest() method - the current solution computes the distance between
 *  a rectangle and a point too often - will maybe fix in the future!
 * 
 *  My implementation also uses references to the parent nodes throghout the
 *  tree. This can probably be optimized, doing away with that memory overhead
 *  - couldn't find how though!
 *  
 ******************************************************************************/

import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.In;

public class KdTree {
    private static final boolean HORIZONTAL = false;
    private static final boolean VERTICAL = true;
    
    private Node root;
    private int treeSize;
    private ArrayList<Point2D> inRange;
    private double shortestDist;
    private Point2D nearestP;
    
    /**
     * The nodes in the 2D-tree.
     * I've chosen an implementation that uses a reference to the node parent
     * as I found it handy to know its attributes for insert() and contains()
     */
    private static class Node {
        private Point2D p;
        private RectHV rect;
        private boolean orientation;
        private Node left;
        private Node right;
        private Node parent;
        
        public Node(Point2D p, boolean orientation, RectHV rect, Node parent) {
            this.p = p;
            this.orientation = orientation;
            this.rect = rect;
            this.parent = parent;
        }
    }
    
    public KdTree() {
        treeSize = 0;
    }
    
    public boolean isEmpty() {
        if (treeSize == 0) return true;
        else return false;
    }
    
    public int size() {
        return treeSize;
    }
    
    /**
     * Inserts a point in the 2D-tree
     * See helper method below
     */
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException();
        root = insert(root, p, null);
    }
    
    /**
     * Inserts a new node by traversing the tree recursively.
     * Duplicate points are not inserted.
     */
    private Node insert(Node nd, Point2D p, Node parent) {
        if (nd == null) {
            boolean newNodeOrientation;
            if (parent == null || parent.orientation == HORIZONTAL) {
                newNodeOrientation = VERTICAL;
            }
            else {
                newNodeOrientation = HORIZONTAL;
            }  
            RectHV newRect = rectangleGenerator(parent, p);  
            Node newNode = new Node(p, newNodeOrientation, newRect, parent);
            treeSize++;
            return newNode;
        }
        
        int comparison;
        if (parent == null || nd.orientation == VERTICAL) {
            comparison = Double.compare(p.x(), nd.p.x());
        }
        else { // parent.orientation == HORIZONTAL
            comparison = Double.compare(p.y(), nd.p.y());
        }
       
        if (comparison < 0) {
            nd.left = insert(nd.left, p, nd);
        }
        else if (comparison >= 0 && !p.equals(nd.p)) {
            nd.right = insert(nd.right, p, nd);
        }
        return nd; // the point being inserted already exists; do nothing
    }
    
    /**
     * Builds a rectangle for a new node.
     * Looks at the parent rectangle, orientation, and also the position of 
     * the new node relative to its parent.
     */
    private RectHV rectangleGenerator(Node parent, Point2D p) {
        if (p == null) throw new NullPointerException(); 
        if (parent == null) return new RectHV(0, 0, 1, 1); // the root's rectangle
        
        double minX = parent.rect.xmin();
        double minY = parent.rect.ymin();
        double maxX = parent.rect.xmax();
        double maxY = parent.rect.ymax();
        
        if (parent.orientation == VERTICAL) {
            if (p.x() < parent.p.x()) maxX = parent.p.x(); // new node's on the left
            else  minX = parent.p.x(); // new node's on the right
        }
        else {
            if (p.y() < parent.p.y()) maxY = parent.p.y(); // new node's on the left
            else minY = parent.p.y(); // new node's on the right
        }
        return new RectHV(minX, minY, maxX, maxY);
    }
     
    /**
     * Checks if a points exists in the tree, starting at the root.
     * See helper method below.
     */
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException();
        return contains(root, p);
    }
    
    /**
     * Helper method for contains.
     */
    private boolean contains(Node nd, Point2D p) {
        if (nd == null) return false;
        if (p.equals(nd.p)) return true;
        
        int comparison;
        if (nd.orientation == VERTICAL) {
            comparison = Double.compare(p.x(), nd.p.x());
        }
        else { // parentOrientation == HORIZONTAL
            comparison = Double.compare(p.y(), nd.p.y());
        }
        
        if (comparison < 0) return contains(nd.left, p);
        else return contains(nd.right, p);
    }
    
    /**
     * Draws a representation of the points and the lines in the 2D-tree.
     * See the helper method below
     */
    public void draw() {
        StdDraw.setXscale(-0.1, 1.1);
        StdDraw.setYscale(-0.1, 1.1);
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.0025);
        StdDraw.square(0.5, 0.5, 0.5);
        
        drawAllPointsAndLines(root);
    }
    
    /**
     * Traverses the tree recursively and draws all the points as well
     * as the line limits defined by the 2D-tree.
     */
    private void drawAllPointsAndLines(Node nd) {
        if (nd == null) return;
        double x = nd.p.x();
        double y = nd.p.y();
        double xMin = nd.rect.xmin();
        double yMin = nd.rect.ymin();
        double xMax = nd.rect.xmax();
        double yMax = nd.rect.ymax();
        
        StdDraw.setPenRadius(0.0025);
        if (nd.orientation == VERTICAL) {
            xMin = x;
            xMax = x;
            StdDraw.setPenColor(StdDraw.RED);
        }
        else { // nd.orientation == HORIZONTAL
            yMin = y;
            yMax = y;
            StdDraw.setPenColor(StdDraw.BLUE);
        }
        StdDraw.line(xMin, yMin, xMax, yMax);
        
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(x, y);
        
        drawAllPointsAndLines(nd.left);
        drawAllPointsAndLines(nd.right);      
    }
    
    /**
     * "Range search"
     * Returns all the points in the set that are within a given rectangle
     * See helper method below
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException();
        inRange = new ArrayList<Point2D>();
        range(root, rect);  
        return inRange;
    }
    
    private void range(Node nd, RectHV rect) {
        if (nd == null) return;
        if (rect.intersects(nd.rect)) {
            if (rect.contains(nd.p)) inRange.add(nd.p);
            range(nd.left, rect);
            range(nd.right, rect);
        }
        else return;
    }
    
    /**
     * Find the nearest point to the provided point
     * See helper method below
     */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException();
        nearestP = null;
        nearest(root, p);
        return nearestP;  
    }
    
    /**
     * This method is a current bottleneck and it's making the timing checks
     * in the grader fail.
     * TODO: Try to minimize the amount of calls to distanceSquaredTo
     */
    private void nearest(Node nd, Point2D p) {
        if (nd == null) return;
        if (nearestP == null) {
            shortestDist = p.distanceSquaredTo(nd.p);
            nearestP = nd.p;
        }
        if (shortestDist < (nd.rect).distanceSquaredTo(p)) return;
        if (shortestDist > p.distanceSquaredTo(nd.p)) {
            nearestP = nd.p;
            shortestDist = p.distanceSquaredTo(nd.p);
        }
        nearest(nd.left, p);
        nearest(nd.right, p);
    }
    
    /**
     * Reads a set of points from input and draws a representation of the tree
     * to StdDraw.
     */
    public static void main(String[] args) {
        String file = args[0];
        In in = new In(file);
        KdTree kd = new KdTree();
        
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kd.insert(p);
        }
        in.close();
        
        kd.draw();
    }
}