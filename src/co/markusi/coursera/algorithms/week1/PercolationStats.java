package co.markusi.coursera.algorithms.week1;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_INTERVAL = 1.96;

    private final int n;
    private final int trials;
    private final double[] openSitesPercentage;

    private double stdDev = 0;
    private double mean = 0;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        validate("n", n);
        validate("trials", trials);
        this.n = n;
        this.trials = trials;
        this.openSitesPercentage = new double[trials];
        runTrials();
    }

    // sample mean of percolation threshold
    public double mean() {
        if (mean == 0) {
            mean = StdStats.mean(openSitesPercentage);
        }
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (stdDev == 0) {
            stdDev = StdStats.stddev(openSitesPercentage);
        }
        return stdDev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - CONFIDENCE_INTERVAL * stdDev / Math.sqrt(openSitesPercentage.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + CONFIDENCE_INTERVAL * stdDev / Math.sqrt(openSitesPercentage.length);
    }

    // test client
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(n, trials);

        System.out.println(String.format("mean                    = %f", percolationStats.mean()));
        System.out.println(String.format("stddev                  = %f", percolationStats.stddev()));
        System.out.println(String.format("95%% confidence interval = [%1f, %2f]",
                percolationStats.confidenceLo(), percolationStats.confidenceHi()));
    }

    private void validate(String parameterName, int value) {
        if (value <= 0) {
            throw new IllegalArgumentException(String.format("%s must be not be <=0, received %d", parameterName, value));
        }
    }

    private void runTrials() {
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int column = StdRandom.uniform(n) + 1;
                while (percolation.isOpen(row, column)) {
                    row = StdRandom.uniform(n) + 1;
                    column = StdRandom.uniform(n) + 1;
                }
                percolation.open(row, column);
            }
            openSitesPercentage[i] = 1.0 * percolation.numberOfOpenSites() / (n * n);
        }
    }
}