package co.markusi.coursera.algorithms.percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF unionFind;
    private final int n;
    private final boolean[] openedSites;
    private final int startVirtualSiteIndex;
    private final int endVirtualSiteIndex;
    private int numberOfOpenSites;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        validate(n);
        this.n = n;
        startVirtualSiteIndex = 0;
        endVirtualSiteIndex = n * n + 1;
        numberOfOpenSites = 0;
        openedSites = new boolean[n * n];
        // + 2 for virtual indexes
        unionFind = new WeightedQuickUnionUF(n * n + 2);
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        if (openSite(row, col)) {
            connectWithNeighbors(row, col);
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return isSiteOpened(row, col);
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        return isOpen(row, col) && unionFind.connected(startVirtualSiteIndex, convertToUFIndex(row, col));
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return unionFind.connected(startVirtualSiteIndex, endVirtualSiteIndex);
    }


    private boolean openSite(int row, int col) {
        int index = convertToArrayIndex(row, col);
        if (!openedSites[index]) {
            openedSites[index] = true;
            numberOfOpenSites++;
            return true;
        }
        return false;
    }

    private boolean isSiteOpened(int row, int col) {
        return openedSites[convertToArrayIndex(row, col)];
    }

    private void validate(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException(String.format("n must be not be <=0, received %d", n));
        }
    }

    private void validate(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n) {
            throw new IllegalArgumentException(
                    String.format("The row and column indices are integers between 1 and n," +
                            " received row: %1s, column %2s ", row, col));
        }
    }

    private void connectWithNeighbors(int row, int col) {
        if (row == 1) {
            connectWithVirtualSite(row, col);
            // only connect with bottom
            if (row != n) {
                connectWithBottom(row, col);
            }
        } else if (row == n) {
            connectWithVirtualSite(row, col);
            // only connect with top
            connectWithTop(row, col);
        } else {
            connectWithBottom(row, col);
            connectWithTop(row, col);
            if (col != 1) {
                connectWithLeft(row, col);
            }
            if (col != n) {
                connectWithRight(row, col);
            }
        }
    }

    private void connectWithVirtualSite(int row, int col) {
        int ufIndex = convertToUFIndex(row, col);
        if (row == 1) {
            unionFind.union(ufIndex, startVirtualSiteIndex);
        }
        if (row == n) {
            unionFind.union(ufIndex, endVirtualSiteIndex);
        }
    }

    private void connectWithBottom(int row, int col) {
        connectElements(row, col, row + 1, col);
    }

    private void connectWithTop(int row, int col) {
        connectElements(row, col, row - 1, col);
    }

    private void connectWithLeft(int row, int col) {
        connectElements(row, col, row, col - 1);
    }

    private void connectWithRight(int row, int col) {
        connectElements(row, col, row, col + 1);
    }

    private void connectElements(int firstRow, int firstColumn, int secondRow, int secondColumn) {
        if (isSiteOpened(secondRow, secondColumn)) {
            unionFind.union(convertToUFIndex(firstRow, firstColumn), convertToUFIndex(secondRow, secondColumn));
        }
    }

    private int convertToArrayIndex(int row, int col) {
        return (row - 1) * n + (col - 1);
    }

    private int convertToUFIndex(int row, int col) {
        return convertToArrayIndex(row, col) + 1;
    }
}