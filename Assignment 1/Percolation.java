/******************************************************************************
 *  Compilation:  javac Percolation.java
 *  Execution:    Percolation
 *  Dependencies: WeightedQuickUnionUF.java
 *
 *  Author: Alex Erhardt
 *  Class: Princeton Algorithms Part I - Problem 1
 * 
 *  Creates a percolation model of size n*n. Sites in the grid can be opened;
 *  if a site from the top row is connected with a site in the bottom row,
 *  the grid is said to percolate. This is a scientific application of the
 *  connected dots problem and union-find algorithms.
 * 
 *  To simulate percolations, use PercolationStats or Princeton's
 *  PercolationVisualizer and Interactive Percolation alongside this one.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  
  private int n; // grid size
  private int siteCount; // total count of sites (n*n)
  private WeightedQuickUnionUF wQuickUnionMain; // main UF structure
  private WeightedQuickUnionUF wQuickUnionFill; // helper UF for visualization
  private boolean[] openSite; // stores whether site is open or not
  private boolean[] isSiteFull; // stores whether site is full or not
   
  public Percolation(int n) {
    
    if (n <= 0) {
      throw new IllegalArgumentException("Grid size n must be a positive integer");
    }  
    
    this.n = n;
    siteCount = n*n;
    
    // Union-Find structure with size n*n + 2 spots for virtual top and bottom
    wQuickUnionMain = new WeightedQuickUnionUF(siteCount + 2);
    
    // Helper Union-Find to keep track of filled positions; no virtual bottom
    wQuickUnionFill = new WeightedQuickUnionUF(siteCount + 1);
    
    // Initialize all positions to closed and not filled
    openSite = new boolean[n*n + 2];
    isSiteFull = new boolean[n*n + 2];
    for (int i = 1; i < openSite.length; i++) {
      openSite[i] = false;
      isSiteFull[i] = false;
    }
    
    // Create virtual top and bottom
    for (int i = 1; i <= n; i++) {
        wQuickUnionMain.union(0, i);
        wQuickUnionFill.union(0, i);
    }
    // No virtual bottom in Helper Union-Find to avoid backwash
    for (int j = n*n; j > n*n - n; j--) {
        wQuickUnionMain.union(n*n + 1, j);
    }     
  }
  
  // Opens a site in the grid
  public void open(int row, int col) {
    validateIndex(row, col);  
    int thisIndex = xyto1D(row, col);
    openSite[thisIndex] = true;
    
    // Checks adjacent cells for newly opened site
    for (int i = (row > 1 ? -1 : 0); i <= (row < n ? 1 : 0); i++) {
        for (int j = (col > 1 ? -1 : 0); j <= (col < n ? 1 : 0); j++) {
            if (Math.abs(i) != Math.abs(j)) {
                int adjacentIndex = xyto1D(row + i, col + j);
                // Connect sites adjacent to newly opened one
                if (openSite[adjacentIndex] && !wQuickUnionMain.connected(thisIndex, adjacentIndex)) {
                    wQuickUnionMain.union(thisIndex, adjacentIndex);                                  
                }
                // Also connect the helper sites
                if (openSite[adjacentIndex] && !wQuickUnionFill.connected(thisIndex, adjacentIndex)) {
                    wQuickUnionFill.union(thisIndex, adjacentIndex);
                }
            }
        }
    } 
  }
  
  // Checks if a site is open
  public boolean isOpen(int row, int col) {
      validateIndex(row, col);
      int thisIndex = xyto1D(row, col);
      return openSite[thisIndex];
  }
  
  // Checks if a site is full
  public boolean isFull(int row, int col) {
      validateIndex(row, col);
      int thisIndex = xyto1D(row, col);
      // First row
      if((row == 1 && openSite[thisIndex])) {
          isSiteFull[thisIndex] = true;
          return true;
      }
      // For other rows, use helper UF
      else if (row != 1 && wQuickUnionFill.connected(0, thisIndex)) {
          isSiteFull[thisIndex] = true;
          return true;
      }
      return false;
  }
  
  public boolean percolates() {
      // Handles n = 1 corner case
      if (n == 1 && !openSite[1]) { 
          return false;
      }
      // If virtual top is connected to virtual bottom
      if (wQuickUnionMain.connected(0, siteCount + 1)) {
          return true;
      }
      return false;
  }
    
  // Converts 2D coordinate into 1D index
  private int xyto1D(int row, int col) {
    int thisIndex = row * n - n + col;
    return thisIndex;
  }
  
  // Ensures row and column are within bounds
  private void validateIndex(int row, int col) {
    if (row <= 0 || row > n || col <= 0 || col > n) {
      throw new IndexOutOfBoundsException("Row: " + row + " Col " + col + " out of bounds");
    }
  }
  
  public static void main(String[] args) {
  } 
}