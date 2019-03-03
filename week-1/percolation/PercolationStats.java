import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    
    private double[] x;
    private int trials;
    
    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if ((n <= 0) || (trials <= 0)) {
            throw new IllegalArgumentException("Illegal argument n or trials");
        }
        this.trials = trials;
        int nsq = n * n;
        x = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                percolation.open(row, col);
            }
            x[i] = (double) percolation.numberOfOpenSites()/nsq;
        }
    }
    
    // sample mean of percolation threshold
    public double mean() {
        double mean = StdStats.mean(x);
        return mean;
    }
    
    // sample standard deviation of percolation threshold
    public double stddev() {
        double stddev = StdStats.stddev(x);
        return stddev;
    }
    
    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        double ans;
        ans = (mean() - ((1.96 * stddev())/Math.sqrt(trials)));
        return ans;
    }
    
    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double ans;
        ans = (mean() + ((1.96 * stddev())/Math.sqrt(trials)));
        return ans;
    }

    // test client (described below)
    public static void main(String[] args) {
        int n, trials;
        n = Integer.parseInt(args[0]);
        trials = Integer.parseInt(args[1]);
        
        PercolationStats ps = new PercolationStats(n, trials);
        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = [" 
                               + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}