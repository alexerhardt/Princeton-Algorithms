/******************************************************************************
 *  Compilation:  javac PercolationStats.java
 *  Execution:    PercolationStats n trials
 *  Dependencies: StdRandom.java, StdStats.java
 *
 *  Author: Alex Erhardt
 *  Class: Princeton Algorithms Part I - Problem 1
 * 
 *  Simulates a percolation model (as seen in Percolation.java)
 *  n determines the size of the gride
 *  trials determines the number of simulations to be carried out
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    
    private double[] resultsArr; // stores the p values of the different tests
    private int trials;
    
    public PercolationStats(int n, int trials) {
        
        this.trials = trials;
        resultsArr = new double[trials];
        
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            double p = 0;
            
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                
                if (!perc.isOpen(row, col)) {
                    perc.open(row, col);
                    p++;
                }            
            }
            
            resultsArr[i] = p / (n*n);
        }
    }
  
    public double mean() {
        return StdStats.mean(resultsArr);
    }
  
    public double stddev() {
        return StdStats.stddev(resultsArr);
    }
  
    public double confidenceLo() {
        return (mean() - 1.96 * stddev() / Math.sqrt(trials));
    }
  
    public double confidenceHi() {
        return (mean() + 1.96 * stddev() / Math.sqrt(trials));
    }
  
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        
        if(n <= 0 || t <= 0) {
            throw new IndexOutOfBoundsException("t " + t + " or n " + n + " out of bounds");
        }
        
        PercolationStats pStats = new PercolationStats(n, t);
        
        System.out.println("mean = " + pStats.mean());
        System.out.println("stddev = " + pStats.stddev());
        System.out.println("95% confidence int. = " 
                           + pStats.confidenceLo() + ", " + pStats.confidenceHi());
                           
   }
   
}