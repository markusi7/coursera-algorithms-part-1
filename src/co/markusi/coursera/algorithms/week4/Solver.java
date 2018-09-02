package co.markusi.coursera.algorithms.week4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Stack;

public class Solver {

    private Iterable<Board> solution;
    private int moves = -1;
    private boolean isSolvable = false;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        checkInput(initial);
        SearchNode searchNode = solveBoardAndItsTwin(initial);
        if (searchNode != null) {
            isSolvable = true;
            moves = searchNode.numberOfMoves;
            solution = iterateSolution(searchNode);
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
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

    private SearchNode solveBoardAndItsTwin(Board initial) {
        MinPQ<SearchNode> minPQ = new MinPQ<>();
        MinPQ<SearchNode> twinMinPQ = new MinPQ<>();
        minPQ.insert(new SearchNode(initial, null, 0));
        twinMinPQ.insert(new SearchNode(initial.twin(), null, 0));

        SearchNode currentNode = minPQ.delMin();
        SearchNode currentTwinNode = twinMinPQ.delMin();
        while (!currentNode.isBoardGoal && !currentTwinNode.isBoardGoal) {
            addToMinPQ(minPQ, currentNode, currentNode.board.neighbors());
            addToMinPQ(twinMinPQ, currentTwinNode, currentTwinNode.board.neighbors());

            currentNode = minPQ.delMin();
            currentTwinNode = twinMinPQ.delMin();
        }
        if (currentNode.isBoardGoal) {
            return currentNode;
        } else {
            return null;
        }
    }

    private void addToMinPQ(MinPQ<SearchNode> minPQ, SearchNode currentNode, Iterable<Board> neighbors) {
        boolean foundPredecessor = currentNode.predecessor == null;
        for (Board neighbor : neighbors) {
            if (!foundPredecessor) {
                foundPredecessor = neighbor.equals(currentNode.predecessor.board);
                if (!foundPredecessor) {
                    minPQ.insert(new SearchNode(neighbor, currentNode, currentNode.numberOfMoves + 1));
                }
            } else {
                minPQ.insert(new SearchNode(neighbor, currentNode, currentNode.numberOfMoves + 1));
            }
        }
    }

    private Iterable<Board> iterateSolution(SearchNode searchNode) {
        Stack<Board> stack = new Stack<>();
        do {
            stack.add(searchNode.board);
            searchNode = searchNode.predecessor;
        } while (searchNode != null);

        Queue<Board> queue = new Queue<>();
        while (!stack.empty()) {
            queue.enqueue(stack.pop());
        }
        return queue;
    }

    private void checkInput(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Input is null");
        }
    }

    private static class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final SearchNode predecessor;
        private final int numberOfMoves;
        private final int compareToValue;
        private final boolean isBoardGoal;

        public SearchNode(Board board, SearchNode predecessor, int numberOfMoves) {
            this.board = board;
            this.predecessor = predecessor;
            this.numberOfMoves = numberOfMoves;
            compareToValue = board.manhattan() + numberOfMoves;
            isBoardGoal = board.isGoal();
        }

        @Override
        public int compareTo(SearchNode other) {
            return Integer.compare(this.compareToValue, other.compareToValue);
        }
    }
}
