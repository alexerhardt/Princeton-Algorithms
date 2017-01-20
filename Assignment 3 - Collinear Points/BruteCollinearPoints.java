/******************************************************************************
 *  Compilation:  javac BruteCollinearPoints.java
 *  Dependencies: Point.java, LineSegment.java
 *  @author: Alex Erhardt
 * 
 *  Coursera, Algorithms Part I - Week 3 programming assignment.
 * 
 *  Finds all collinear points in a plane through brute-force, ie,
 *  testing all possible combinations of 4 points, returns a list of the
 *  line segments formed by those points.
 * 
 *  Grader output: 95 / 100
 *  
 ******************************************************************************/

import java.util.List;
import java.util.ArrayList;

public class BruteCollinearPoints {
    private List<LineSegment> segmentResults = new ArrayList<LineSegment>();
    
    /**
     * Initializes the algorithm.
     *
     * @param  points an array of points (see Point.java)
     */
    public BruteCollinearPoints(Point[] points) {
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
        bruteFinder(points, 4, 0, new Point[4]);
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
     * Checks all combinations of 4 points in the plane for collinearity
     * Uses an n-choose-4 approach with recursion
     * Upon finding 4 collinear points, sorts them in order to find
     * the end-points of the segment, then saves the result.
     */
    // Checks all combinations of points (n-choose-4) with recursion
    private void bruteFinder(Point[] points, int depth, int startPos, Point[] combo) {
        
        // If 3 points are not collinear, no sense in checking this triple anymore
        if (depth == 1) {
            if (combo[0].slopeTo(combo[1]) != combo[1].slopeTo(combo[2])) {
                return;
            }
        }
        // If 4 points are collinear, sorts them and adds them to the list
        if (depth == 0) {
            if (combo[0].slopeTo(combo[1]) == combo[0].slopeTo(combo[2])
                    && combo[0].slopeTo(combo[2]) == combo[0].slopeTo(combo[3])) {
                Point[] sortedPoints = pointSorter(combo);
                LineSegment lineSegment = new LineSegment(sortedPoints[0], sortedPoints[3]);
                segmentResults.add(lineSegment);
            }
            return;
        }
        
        // Test all point combinations recursively
        for (int i = startPos; i <= points.length - depth; i++) {
            combo[combo.length - depth] = points[i];
            bruteFinder(points, depth - 1, i + 1, combo);
        }       
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
    
    public static void main(String[] args) {
        // Unit testing can be found in separate files 
    }
}