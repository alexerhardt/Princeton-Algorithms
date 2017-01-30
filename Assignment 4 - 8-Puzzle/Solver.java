/******************************************************************************
 *  Compilation:  javac Solver.java
 *  Execution: java Solver "file.txt"
 *  Dependencies: Board.java
 *  @author: Alex Erhardt
 * 
 *  Princeton, Algorithms Part I on Coursera
 *  8-puzzle programming assignment
 * 
 *  Solves the 8-puzzle and its generalizations using an A* algorithm.
 *  Uses Princeton's priority queue implementation.
 * 
 *  Grader output: 99 / 100
 *  
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import java.util.Stack;

public class Solver {
    private boolean isSolvable;
    private SearchNode goalNode;
    
    /**
     * Constructs nodes to be inserted in priority queue 
     */
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private SearchNode previous;
        private int storedMoves;
        private int priority;
        
        public SearchNode(Board b, SearchNode prv, int moves) {
            this.board = b;
            this.previous = prv;
            this.storedMoves = moves;
            this.priority = board.manhattan() + storedMoves;
        }
        
        public int compareTo(SearchNode that) {
            if (this.priority > that.priority) return 1;
            if (this.priority < that.priority) return -1;
            if (this.priority == that.priority) {
                int manhattanDif = this.board.manhattan() - that.board.manhattan();
                if (manhattanDif != 0) return manhattanDif;
                else {
                    int hammingDif = this.board.hamming() - that.board.hamming();
                    return hammingDif;
                }
            }
            return 0;
        }
    }
    
    /**
     * Attempts to solve a board and its twin.
     * If the twin is solvable, then the board isn't.
     * Could use some refactoring...
     */
    public Solver(Board initial) {
        int moves = 0;
        
        Board twin = initial.twin();
        
        SearchNode firstMainNode = new SearchNode(initial, null, moves);
        MinPQ<SearchNode> mainPQ = new MinPQ<SearchNode>();
        mainPQ.insert(firstMainNode);
        
        SearchNode firstTwinNode = new SearchNode(twin, null, moves);
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>();
        twinPQ.insert(firstTwinNode);
        
        SearchNode dequeuedMainNode, dequeuedTwinNode;
        Board dequeuedMainBoard, dequeuedTwinBoard;

        while(true) {
            dequeuedMainNode = mainPQ.delMin();
            dequeuedMainBoard = dequeuedMainNode.board;

            if (!dequeuedMainBoard.isGoal()) {
                Iterable<Board> neighbors = dequeuedMainBoard.neighbors();
                for (Board neighbor : neighbors) {
                    if (dequeuedMainNode.previous == null || 
                        !neighbor.equals(dequeuedMainNode.previous.board)) {
                        moves = dequeuedMainNode.storedMoves + 1;
                        SearchNode newNode = new SearchNode(neighbor, dequeuedMainNode, moves);
                        mainPQ.insert(newNode);
                    }
                }                                              
            }
            else { // we've a solution for the board
                isSolvable = true;
                goalNode = dequeuedMainNode;
                break;
            }
            
            dequeuedTwinNode = twinPQ.delMin();
            dequeuedTwinBoard = dequeuedTwinNode.board;
            
            if (!dequeuedTwinBoard.isGoal()) {
                Iterable<Board> twinNeighbors = dequeuedTwinBoard.neighbors();
                for (Board neighbor : twinNeighbors) {
                    if (dequeuedTwinNode.previous == null || neighbor != dequeuedTwinNode.previous.board) {
                        moves = dequeuedTwinNode.storedMoves + 1;
                        SearchNode newNode = new SearchNode(neighbor, dequeuedTwinNode, moves);
                        twinPQ.insert(newNode);
                    }
                }
            } // we've found a solution for the twin
            else {
                isSolvable = false;
                break;
            }    
        }
    }
    
    
    /**
     * Is the board solvable?
     */
    public boolean isSolvable() {
        return isSolvable;
    }
    
    /**
     * Min number of moves to solve the initial board; -1 if unsolvable
     */
    public int moves() {
        if(isSolvable) return goalNode.storedMoves;
        return -1;
    }
    
    /**
     * Sequence of boards in shortest solution
     */
    public Iterable<Board> solution() {
        if(isSolvable) {
            Stack<Board> stepStack = new Stack<Board>();
            SearchNode pointer = goalNode;
            while (pointer != null) {
                stepStack.push(pointer.board);
                pointer = pointer.previous;
            }
            // Reverse the order so we can iterate from move 0 to move n
            Stack<Board> reversedSteps = new Stack<Board>();
            while (!stepStack.empty()) reversedSteps.push(stepStack.pop());
            stepStack = null;
            return reversedSteps;
        }
        return null;
    }
    
    /**
     * Solve a given puzzle
     */
    public static void main(String[] args) {        
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
                
        }
}
}    