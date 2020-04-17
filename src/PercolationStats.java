import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] percolationThreshold;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials){
        if (n <= 0)
            throw new IllegalArgumentException("n cannot be less than one");
        if (trials <= 0)
            throw new IllegalArgumentException("trails cannot be less than one");
        percolationThreshold = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            int row;
            int col;
            int size = n*n;
            while (!percolation.percolates()) {
                row = StdRandom.uniform(n)+1;
                col = StdRandom.uniform(n)+1;
                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                }
            }
            percolationThreshold[i] = (double) percolation.numberOfOpenSites() / size;
        }
    }

    // sample mean of percolation threshold
    public double mean(){
        return StdStats.mean(percolationThreshold);
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return StdStats.stddev(percolationThreshold);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo(){
        return (mean() - 1.96) / Math.sqrt(percolationThreshold.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return (mean() + 1.96) / Math.sqrt(percolationThreshold.length);
    }

    // test client (see below)
    public static void main(String[] args){
        final int n = Integer.valueOf(args[0]);
        final int trails = Integer.valueOf(args[1]);
        PercolationStats ps = new PercolationStats(n,trails);
        System.out.printf("mean                     = %f\n", ps.mean());
        System.out.printf("stddev                   = %f\n", ps.stddev());
        System.out.printf("95%% confidence interval  = [%f, %f]\n"
                , ps.confidenceHi(), ps.confidenceLo());
    }

}