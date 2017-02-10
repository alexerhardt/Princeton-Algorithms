/******************************************************************************
 *  Compilation:  javac PointSET.java
 *  Execution: java PointSET "points.txt"
 *  Dependencies: none
 *  @author: Alex Erhardt
 * 
 *  Princeton, Algorithms Part I on Coursera
 *  PointSET programming assignment
 * 
 *  Uses Java's TreeSet (red-black BST) to store points in a plane.
 *  Can be used for brute-force range searching and nearest-neighbor finding.
 *  For an evolved, efficient solution to these solutions check KdTree.java
 * 
 *  Grader output: 98 / 100
 *  
 ******************************************************************************/

import java.util.TreeSet;
import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.In;

public class PointSET {
    private TreeSet<Point2D> set;
    
    public PointSET() {
        set = new TreeSet<Point2D>();     
    }
     
    public boolean isEmpty() {
        return set.isEmpty();
    }
     
    public int size() {
        return set.size();
    }
            
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException();
        set.add(p);
    }
    
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException();
        return set.contains(p);
    }
        
    // draw all points to standard draw    
    public void draw() {
        StdDraw.setXscale(-0.1, 1.1);
        StdDraw.setYscale(-0.1, 1.1);
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        
        for (Point2D point : set) {
            double x = point.x();
            double y = point.y();
            StdDraw.point(x, y);
        }   
    }
    
    /**
     * Checks all the points in the set to see which ones fall into a given
     * rectangle's boundaries, returning a list of points.
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException();
        
        double upperBound = rect.ymax();
        double rightBound = rect.xmax();
        double lowerBound = rect.ymin();
        double leftBound = rect.xmin();
        
        ArrayList<Point2D> inRange = new ArrayList<Point2D>();
        for (Point2D point : set) {
            double x = point.x();
            double y = point.y();
            if (y <= upperBound && y >= lowerBound && x >= leftBound && x <= rightBound) {
                inRange.add(point);
            }
        }
        return inRange;
    }
    
    /**
     * Checks the distance of all points in the plane to a given point, and 
     * returns the closest find
     */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException();
        
        if (set.isEmpty()) return null;
        
        Point2D closestPoint = null;
        double minDistance = 2.0; // max squared Euclidean distance in plane = 2 
        for (Point2D point : set) {
            if (point.distanceSquaredTo(p) <= minDistance) {
                minDistance = point.distanceSquaredTo(p);
                closestPoint = point;
            }
        }
        return closestPoint;           
    }

    /**
     * Draws the point to StdDraw
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        PointSET pointset = new PointSET();
        
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            pointset.insert(p);
        }
        
        pointset.draw();
    }
}