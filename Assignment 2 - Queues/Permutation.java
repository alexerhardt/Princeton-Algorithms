/******************************************************************************
 *  Compilation:  javac Permutation.java
 *  Execution: java Permutation k < input.txt
 *  Dependencies: RandomizedQueue.java
 *  @author: Alex Erhardt
 * 
 *  Princeton Algorithms Part I on Coursera - Week 3 programming assignment
 * 
 *  Reads a sequence of strings from standard input, and prints exactly k
 *  uniformly at random. 
 * 
 *  Grader output: 99 / 100
 *  
 ******************************************************************************/

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        
        RandomizedQueue<String> rQueue = new RandomizedQueue<String>();
        
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            rQueue.enqueue(item);
        }
        
        for (int i = 0; i < k; i++) {
            System.out.println(rQueue.dequeue());
        }     
    }
}
