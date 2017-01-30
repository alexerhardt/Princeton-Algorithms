/******************************************************************************
 *  Compilation:  javac Board.java
 *  Dependencies: none
 *  @author: Alex Erhardt
 * 
 *  Princeton, Algorithms Part I on Coursera
 *  8-puzzle programming assignment
 * 
 *  Creates a Board object for use in an A* puzzle solver algorithm
 *  (see Solver.java)
 * 
 *  Grader output: 99 / 100
 *  
 ******************************************************************************/

import java.util.Queue;
import java.util.LinkedList;

public class Board {
    private static int[][] goalBoardArr;
    private static Board goalBoard;
    private int[][] thisBoardArr;
    private int N;
    private int zeroRow;
    private int zeroCol;
     
    /**
     * Constructs a board and its goal board
     * @param  2D array with ints [0, n*n-1] 
     */
    public Board(int[][] blocks) {
        int n = blocks.length;
        thisBoardArr = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // cache row and col positions for later use
                if (blocks[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                }
                thisBoardArr[i][j] = blocks[i][j];
            }
        }
        N = n;
        // initialize a goal board - always the same for a given n
        // overwrite only if board dimensions change
        if (goalBoardArr == null || goalBoardArr.length != N) {
            goalBoardBuilder(n);
        }   
    }
    
    /**
     * Builds a goal board
     */
    private void goalBoardBuilder(int n) {
             goalBoardArr = new int[n][n];
            int tile = 1;
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    goalBoardArr[i][j] = tile++;
            goalBoardArr[n-1][n-1] = 0;
            goalBoard = new Board(goalBoardArr); 
    }
    
    /**
     * Board dimension
     */
    public int dimension() {
        return N;
    }
    
    /**
     * Calculates the sum of Hamming distances for a board
     * (1 if cell is out of position, 0 if not)
     */
    public int hamming() {
        int inWrongPos = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (thisBoardArr[i][j] != goalBoardArr[i][j] && thisBoardArr[i][j] != 0) {
                    inWrongPos++;
                }
            }
        }
        return inWrongPos;
    }
    
    /**
     * Calculates the sum of Manhattan distances for a board
     * (x distance + y distance respective of goal)
     */
    public int manhattan() {
        int numInCell, numRowGoal, numColGoal, xDistance, yDistance;
        int distanceSum = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                numInCell = thisBoardArr[i][j];
                if (numInCell != 0) {
                    numRowGoal = (numInCell - 1) / N;
                    numColGoal = (numInCell - 1) % N;
                    xDistance = Math.abs(numRowGoal - i);
                    yDistance = Math.abs(numColGoal - j);
                    distanceSum += xDistance + yDistance;
                }              
            }
        }
        return distanceSum;
    }
    
    /**
     * Is this the goal board?
     */
    public boolean isGoal() {
        return this.equals(goalBoard);
    }
    
    /**
     * A twin is a board obtained by swapping any two (non-zero) blocks.
     * 
     * It is used in our A* implementation to assert whether a board is 
     * solvable or not (only one of a Board and its Twin is solvable)
     * 
     * Try to swap the first block, or the second one.
     */
    public Board twin() {
        int[][] twinTemp = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                twinTemp[i][j] = thisBoardArr[i][j];
        
        if (twinTemp[0][0] != 0) {
            if (twinTemp[0][1] != 0) {
                int temp = twinTemp[0][0];
                twinTemp[0][0] = twinTemp[0][1];
                twinTemp[0][1] = temp;
            }
            else { // right-adjacent is zero
                int temp = twinTemp[0][0];
                twinTemp[0][0] = twinTemp[1][0];
                twinTemp[1][0] = temp;
            }
        }
        else { // first tile is zero
            int temp = twinTemp[0][1];
            twinTemp[0][1] = twinTemp[1][1];
            twinTemp[1][1] = temp;
        }
        Board twinBoard = new Board(twinTemp);
        return twinBoard;         
    }
    
    /**
     * Override equals to compare two boards
     */
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if(this.thisBoardArr[i][j] != that.thisBoardArr[i][j]) return false;
        return true;
    }
    
    /**
     * Returns all neighboring boards, ie, all legal moves
     */
    public Iterable<Board> neighbors() {
        Queue<Board> neighborQueue = new LinkedList<Board>();
        for (int i = (zeroRow > 0 ? -1 : 0); i <= (zeroRow < N-1 ? 1 : 0); i++) {
            for (int j = (zeroCol > 0 ? -1 : 0); j <= (zeroCol < N-1 ? 1 : 0); j++) {
                if (Math.abs(i) != Math.abs(j)) { // only check up, left, right, down    
                    int[][] swappedCopy = arrayCopy(thisBoardArr);
                    cellSwap(swappedCopy, zeroRow, zeroCol, zeroRow + i, zeroCol + j);
                    Board neighbor = new Board(swappedCopy);
                    neighborQueue.add(neighbor);
                }
            }
        }
        return neighborQueue;
    }
    
    /**
     * String representation of the board
     * Method provided by the course - pretty much unchanged
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", thisBoardArr[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    /**
     * Helper method for copying two-dimensional arrays
     */
    private int[][] arrayCopy(int[][] original) {
        int l = original.length;
        int[][] copy = new int[l][l];
        for (int i = 0; i < l; i++) 
            for (int j = 0; j < l; j++)
                copy[i][j] = original[i][j];
        return copy;
    }
    
    /**
     * Helper method for swapping cells
     */
    private void cellSwap(int[][] array, int srcRow, int srcCol, int desRow, int desCol) {
        int temp = array[srcRow][srcCol];
        array[srcRow][srcCol] = array[desRow][desCol];
        array[desRow][desCol] = temp;
    }
    
    // unit tests
    public static void main(String[] args) {
    }
}