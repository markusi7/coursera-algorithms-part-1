package co.markusi.coursera.algorithms.week4;

import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    private final int n;
    private final int[][] tiles;
    private int emptyTileX = -1;
    private int emptyTileY = -1;
    private int hamming = -1;
    private int manhattan = -1;
    private Iterable<Board> neighbors;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        n = blocks.length;
        tiles = copyTiles(blocks, n);
    }

    private Board(int[][] blocks, int x0, int y0, int x1, int y1) {
        n = blocks.length;
        tiles = copyTiles(blocks, n);
        int tmp;
        tmp = tiles[x0][y0];
        tiles[x0][y0] = tiles[x1][y1];
        tiles[x1][y1] = tmp;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        if (hamming != -1) return hamming;
        hamming = 0;
        int value;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                value = tiles[i][j];
                if (value != i * n + j + 1 && value != 0) hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        if (manhattan != -1) return manhattan;
        manhattan = 0;
        int value;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                value = tiles[i][j];
                // 2 + 2 + 4 + 2 + 3 + 2 + 3 + 3 + 3
                if (value != i * n + j + 1 && value != 0) {
                    int targetIndex = value - 1;
                    int targetRow = targetIndex / n;
                    int targetColumn = targetIndex % n;
                    int xDiff = Math.abs(targetRow - i);
                    int yDiff = Math.abs(targetColumn - j);
                    manhattan = manhattan + xDiff + yDiff;
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        boolean isGoal = true;
        for (int i = 0; isGoal && i < n; i++) {
            for (int j = 0; isGoal && j < n; j++) {
                isGoal = tiles[i][j] == i * n + j + 1 ||
                        i == n - 1 && j == n - 1 && tiles[i][j] == 0;
            }
        }
        return isGoal;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        findEmptyTileIndexes();
        int x0;
        int y0;
        int x1;
        int y1;

        if (emptyTileY + 1 <= n - 1 && emptyTileX + 1 <= n - 1) {
            // exchange bottom and right
            y0 = emptyTileY + 1;
            x0 = emptyTileX;
            y1 = emptyTileY;
            x1 = emptyTileX + 1;
        } else if (emptyTileX + 1 <= n - 1 && emptyTileY - 1 >= 0) {
            // exchange right and upper
            y0 = emptyTileY;
            x0 = emptyTileX + 1;
            y1 = emptyTileY - 1;
            x1 = emptyTileX;
        } else if (emptyTileY - 1 >= 0 && emptyTileX - 1 >= 0) {
            // exchange upper and left
            y0 = emptyTileY - 1;
            x0 = emptyTileX;
            y1 = emptyTileY;
            x1 = emptyTileX - 1;
        } else {
            // exchange left and bottom
            y0 = emptyTileY;
            x0 = emptyTileX - 1;
            y1 = emptyTileY + 1;
            x1 = emptyTileX;
        }
        return new Board(tiles, x0, y0, x1, y1);
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.n != that.n) return false;
        boolean equalTiles = true;
        for (int i = 0; equalTiles && i < n; i++) {
            equalTiles = Arrays.equals(this.tiles[i], that.tiles[i]);
        }
        return equalTiles;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        if (neighbors != null) return neighbors;
        findEmptyTileIndexes();

        Queue<Board> queue = new Queue<>();
        // can move up
        if (emptyTileY != 0) {
            queue.enqueue(new Board(tiles, emptyTileX, emptyTileY, emptyTileX, emptyTileY - 1));
        }
        // can move down
        if (emptyTileY != n - 1) {
            queue.enqueue(new Board(tiles, emptyTileX, emptyTileY, emptyTileX, emptyTileY + 1));
        }
        // can move right
        if (emptyTileX != 0) {
            queue.enqueue(new Board(tiles, emptyTileX - 1, emptyTileY, emptyTileX, emptyTileY));
        }
        // can move left
        if (emptyTileX != n - 1) {
            queue.enqueue(new Board(tiles, emptyTileX + 1, emptyTileY, emptyTileX, emptyTileY));
        }
        neighbors = queue;
        return neighbors;
    }

    // string representation of this board (in the output format specified below)
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n);
        s.append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private int[][] copyTiles(int[][] tilesToCopy, int tilesDimension) {
        int[][] output = new int[tilesDimension][tilesDimension];
        for (int i = 0; i < tilesDimension; i++) {
            for (int j = 0; j < tilesDimension; j++) {
                output[i][j] = tilesToCopy[i][j];
            }
        }
        return output;
    }

    private void findEmptyTileIndexes() {
        if (emptyTileX != -1 && emptyTileY != -1) return;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    emptyTileX = i;
                    emptyTileY = j;
                    return;
                }
            }
        }
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        testDimension();
        testEquals();
        testToString();
        testIsGoal();
        testTwins();
        testNeighbors();
        testHamming();
        testManhattan();
    }

    private static void testDimension() {
        int[][] tiles = new int[][]{
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8}};
        Board board = new Board(tiles);
        assert board.dimension() == 3;
    }

    private static void testEquals() {
        int[][] tiles = new int[][]{
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8}};

        int[][] equalTiles = new int[][]{
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8}};

        int[][] smallerDimensionTiles = new int[][]{
                {0, 1},
                {2, 3}};

        int[][] largerDimensionTiles = new int[][]{
                {0, 1, 2, 3},
                {4, 5, 6, 7},
                {8, 9, 10, 11},
                {12, 13, 14, 15}};

        int[][] differentTiles = new int[][]{
                {4, 1, 2},
                {3, 0, 5},
                {6, 7, 8}
        };
        Board board = new Board(tiles);
        Board equalBoard = new Board(equalTiles);
        Board smallerDimensionBoard = new Board(smallerDimensionTiles);
        Board largerDimensionBoard = new Board(largerDimensionTiles);
        Board differentBoard = new Board(differentTiles);

        assert board.equals(board);
        assert board.equals(equalBoard);
        assert !board.equals(smallerDimensionBoard);
        assert !board.equals(largerDimensionBoard);
        assert !board.equals(differentBoard);
    }

    private static void testToString() {
        int[][] tiles = new int[][]{
                {0, 1, 3},
                {4, 2, 5},
                {7, 8, 6}};

        Board board = new Board(tiles);

        String expectedOutput = "3\n" +
                " 0  1  3 \n" +
                " 4  2  5 \n" +
                " 7  8  6 \n";

        assert board.toString().equals(expectedOutput);
    }

    private static void testIsGoal() {
        int[][] notGoalTiles = new int[][]{
                {6, 7, 8},
                {0, 1, 2},
                {3, 4, 5}};

        int[][] goalTiles = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}};

        Board notGoalBoard = new Board(notGoalTiles);
        Board goalBoard = new Board(goalTiles);

        assert !notGoalBoard.isGoal();
        assert goalBoard.isGoal();
    }

    private static void testTwins() {
        int[][] tiles = new int[][]{
                {1, 2},
                {3, 0}};

        int[][] twinTiles = new int[][]{
                {1, 3},
                {2, 0}};
        Board board = new Board(tiles);
        Board twinBoard = new Board(twinTiles);
        assert board.twin().equals(twinBoard);

        tiles = new int[][]{
                {1, 0},
                {2, 3}};

        twinTiles = new int[][]{
                {3, 0},
                {2, 1}};
        board = new Board(tiles);
        twinBoard = new Board(twinTiles);
        assert board.twin().equals(twinBoard);

        tiles = new int[][]{
                {0, 1},
                {2, 3}};

        twinTiles = new int[][]{
                {0, 2},
                {1, 3}};
        board = new Board(tiles);
        twinBoard = new Board(twinTiles);
        assert board.twin().equals(twinBoard);

        tiles = new int[][]{
                {1, 2},
                {0, 3}};

        twinTiles = new int[][]{
                {3, 2},
                {0, 1}};
        board = new Board(tiles);
        twinBoard = new Board(twinTiles);
        assert board.twin().equals(twinBoard);
    }

    private static void testNeighbors() {
        testEmptyIndexNotConstrained();
        testWithCorneredEmptyIndex();
        testWithEmptyIndexAtBottom();
        testWithEmptyIndexAtTop();
        testWithEmptyIndexAtLeft();
        testWithEmptyIndexAtRight();
    }

    private static void testHamming() {
        int[][] notGoalTiles = new int[][]{
                {6, 7, 8},
                {4, 1, 2},
                {3, 0, 5}};
        int[][] goalTiles = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}};
        Board notGoalBoard = new Board(notGoalTiles);
        Board goalBoard = new Board(goalTiles);

        int notGoalBoardHamming = notGoalBoard.hamming();
        int goalBoardHamming = goalBoard.hamming();

        assert notGoalBoardHamming == 7;
        assert goalBoardHamming == 0;
    }

    private static void testManhattan() {
        // 3 + 3 + 3 + 0 + 2 + 2 + 4 +2
        int[][] notGoalTiles = new int[][]{
                {8, 1, 3},
                {4, 0, 2},
                {7, 6, 5}};
        int[][] goalTiles = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}};
        Board notGoalBoard = new Board(notGoalTiles);
        Board goalBoard = new Board(goalTiles);

        int notGoalBoardManhattan = notGoalBoard.manhattan();
        int goalBoardManhattan = goalBoard.manhattan();

        assert notGoalBoardManhattan == 1 + 2 + 2 + 2 + 3;
        assert goalBoardManhattan == 0;
    }

    private static void testEmptyIndexNotConstrained() {
        int[][] tilesWithEmptyIndexNotConstrained = new int[][]{
                {1, 2, 3},
                {4, 0, 6},
                {7, 8, 9}};
        int[][] tilesMoveBottom = new int[][]{
                {1, 2, 3},
                {4, 8, 6},
                {7, 0, 9}};
        int[][] tilesMoveTop = new int[][]{
                {1, 0, 3},
                {4, 2, 6},
                {7, 8, 9}};
        int[][] tilesMoveLeft = new int[][]{
                {1, 2, 3},
                {0, 4, 6},
                {7, 8, 9}};
        int[][] tilesMoveRight = new int[][]{
                {1, 2, 3},
                {4, 6, 0},
                {7, 8, 9}};
        Board board = new Board(tilesWithEmptyIndexNotConstrained);
        Board boardMoveBottom = new Board(tilesMoveBottom);
        Board boardMoveTop = new Board(tilesMoveTop);
        Board boardMoveLeft = new Board(tilesMoveLeft);
        Board boardMoveRight = new Board(tilesMoveRight);

        assert checkNeighborsAreTheSame(board, boardMoveBottom, boardMoveTop, boardMoveLeft, boardMoveRight);
    }

    private static void testWithCorneredEmptyIndex() {
        int[][] tilesWithCorneredEmptyIndex = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}};
        int[][] tilesMoveTop = new int[][]{
                {1, 2, 3},
                {4, 5, 0},
                {7, 8, 6}};
        int[][] tilesMoveLeft = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 0, 8}};
        Board board = new Board(tilesWithCorneredEmptyIndex);
        Board boardMoveTop = new Board(tilesMoveTop);
        Board boardMoveLeft = new Board(tilesMoveLeft);

        assert checkNeighborsAreTheSame(board, boardMoveTop, boardMoveLeft);
    }

    private static void testWithEmptyIndexAtBottom() {
        int[][] tilesWithEmptyIndexAtBottom = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 0, 8}};
        int[][] tilesMoveTop = new int[][]{
                {1, 2, 3},
                {4, 0, 6},
                {7, 5, 8}};
        int[][] tilesMoveLeft = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {0, 7, 8}};
        int[][] tilesMoveRight = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}};
        Board board = new Board(tilesWithEmptyIndexAtBottom);
        Board boardMoveTop = new Board(tilesMoveTop);
        Board boardMoveLeft = new Board(tilesMoveLeft);
        Board boardMoveRight = new Board(tilesMoveRight);

        assert checkNeighborsAreTheSame(board, boardMoveTop, boardMoveLeft, boardMoveRight);

    }

    private static void testWithEmptyIndexAtTop() {
        int[][] tilesWithEmptyIndexAtTop = new int[][]{
                {7, 0, 8},
                {4, 5, 6},
                {1, 2, 3}};
        int[][] tilesMoveBottom = new int[][]{
                {7, 5, 8},
                {4, 0, 6},
                {1, 2, 3}};
        int[][] tilesMoveLeft = new int[][]{
                {0, 7, 8},
                {4, 5, 6},
                {1, 2, 3}};
        int[][] tilesMoveRight = new int[][]{
                {7, 8, 0},
                {4, 5, 6},
                {1, 2, 3}};
        Board board = new Board(tilesWithEmptyIndexAtTop);
        Board boardMoveBottom = new Board(tilesMoveBottom);
        Board boardMoveLeft = new Board(tilesMoveLeft);
        Board boardMoveRight = new Board(tilesMoveRight);

        assert checkNeighborsAreTheSame(board, boardMoveBottom, boardMoveLeft, boardMoveRight);

    }

    private static void testWithEmptyIndexAtLeft() {
        int[][] tilesWithEmptyIndexAtLeft = new int[][]{
                {4, 7, 8},
                {0, 5, 6},
                {1, 2, 3}};
        int[][] tilesMoveTop = new int[][]{
                {0, 7, 8},
                {4, 5, 6},
                {1, 2, 3}};
        int[][] tilesMoveBottom = new int[][]{
                {4, 7, 8},
                {1, 5, 6},
                {0, 2, 3}};
        int[][] tilesMoveRight = new int[][]{
                {4, 7, 8},
                {5, 0, 6},
                {1, 2, 3}};
        Board board = new Board(tilesWithEmptyIndexAtLeft);
        Board boardMoveTop = new Board(tilesMoveTop);
        Board boardMoveBottom = new Board(tilesMoveBottom);
        Board boardMoveRight = new Board(tilesMoveRight);

        assert checkNeighborsAreTheSame(board, boardMoveTop, boardMoveBottom, boardMoveRight);
    }

    private static void testWithEmptyIndexAtRight() {
        int[][] tilesWithEmptyIndexAtRight = new int[][]{
                {7, 8, 6},
                {4, 5, 0},
                {1, 2, 3}};
        int[][] tilesMoveTop = new int[][]{
                {7, 8, 0},
                {4, 5, 6},
                {1, 2, 3}};
        int[][] tilesMoveBottom = new int[][]{
                {7, 8, 6},
                {4, 5, 3},
                {1, 2, 0}};
        int[][] tilesMoveLeft = new int[][]{
                {7, 8, 6},
                {4, 0, 5},
                {1, 2, 3}};
        Board board = new Board(tilesWithEmptyIndexAtRight);
        Board boardMoveTop = new Board(tilesMoveTop);
        Board boardMoveBottom = new Board(tilesMoveBottom);
        Board boardMoveLeft = new Board(tilesMoveLeft);

        assert checkNeighborsAreTheSame(board, boardMoveBottom, boardMoveTop, boardMoveLeft);


    }

    private static boolean checkNeighborsAreTheSame(Board board, Board... expectedBoards) {
        List<Board> neighborsList = new ArrayList<>();
        for (Board neighbor : board.neighbors()) {
            neighborsList.add(neighbor);
        }

        for (Board expectedBoard : expectedBoards) {
            if (!neighborsList.contains(expectedBoard)) return false;
        }
        return true;
    }
}
