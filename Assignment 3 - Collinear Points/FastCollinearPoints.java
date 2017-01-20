/******************************************************************************
 *  Compilation:  javac FastCollinearPoints.java
 *  Dependencies: Point.java, LineSegment.java
 *  @author: Alex Erhardt
 * 
 *  Coursera, Algorithms Part I - Week 3 programming assignment.
 * 
 *  Finds all collinear points in a plane.
 *  For every point "p" in the plane, sorts all remaining points according
 *  to their slope respective of "p", then checks all adjacent points for
 *  collinearity.
 * 
 *  Doesn't handle (yet) the case where there are 5 or more collinear points
 *  in a plane; will try to solve this later.
 * 
 *  Grader output: 95 / 100
 *  
 ******************************************************************************/

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class FastCollinearPoints {
    private List<LineSegment> segmentResults = new ArrayList<LineSegment>();
    
    /**
     * Initializes the algorithm.
     *
     * @param  points an array of points (see Point.java)
     */
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new NullPointerException();
        
        // Checks if a point is null or repeated
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new NullPointerException();
            }
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }    
        }
        int n = points.length;
        Point[] pointsCopy = new Point[n];
        System.arraycopy(points, 0, pointsCopy, 0, n);
        fastFinder(pointsCopy, 0);         
    }
    
    /**
     * Finds all collinear points in a plane.
     * Takes each point in the array and sorts the rest according to the slope
     * they make to the origin, iterates through the sorted array, then
     * adds any line segments of 4 collinear points.
     */
    private void fastFinder(Point[] points, int i) {
        if (i > points.length - 4) {
            return;
        }
        Point p = points[i];
        Arrays.sort(points, i, points.length, points[i].slopeOrder());
        for (int j = i; j < points.length - 3; j++) {
            Point q = points[j+1], r = points[j+2], s = points[j+3];
            if (p.slopeTo(q) == p.slopeTo(r) && p.slopeTo(r) == p.slopeTo(s)) {
                Point[] combo = {p, q, r, s};
                Point[] sortedPoints = pointSorter(combo);
                LineSegment lineSegment = new LineSegment(sortedPoints[0], sortedPoints[3]);
                segmentResults.add(lineSegment);
            }
        }
        fastFinder(points, i+1);
    }
    
    /**
     * Returns number of segments found
     */
    public int numberOfSegments() {
        return segmentResults.size();
    }
    
    /**
     * Returns the line segments found
     */
    public LineSegment[] segments() {
        int n = segmentResults.size();
        LineSegment[] segmentArray = segmentResults.toArray(new LineSegment[n]); 
        return segmentArray; 
    }
    
   /**
     * Sorts a point combination in ascending order with merge sort.
     * Given the tiny size of the array, I would've much preferred a simpler
     * sort (insertion), but Point.java does not offer a getter method; since 
     * I didn't know how to copy point objects, I had to go for the merge.
     */
    private Point[] pointSorter(Point[] combo) {
        int n = combo.length;
        Point[] sortedPoints = new Point[n];
        System.arraycopy(combo, 0, sortedPoints, 0, n);
        // Point[] aux = new Point[n];
        sort(sortedPoints, 0, n - 1);
        return sortedPoints;   
    }
    
    private static void sort(Point[] a, int lo, int hi) {
        if (hi <= lo) return;
        int mid = lo + (hi - lo)/2;
        sort(a, lo, mid);
        sort(a, mid+1, hi);
        merge(a, lo, mid, hi);
    }
    
    private static void merge(Point[] a, int lo, int mid, int hi) {
        int i = lo, j = mid + 1;
        Point[] aux = new Point[a.length];
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else if ((aux[j].compareTo(aux[i]) < 0)) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
    }
    
}