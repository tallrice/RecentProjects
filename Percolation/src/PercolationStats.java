import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {

    private double[] p;
    private int trials;

    public PercolationStats(int n, int trials) {
        // perform trials independent experiments on an n-by-n grid
        // validate that n and trials are greater than 0
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException(
            "n and trials must be greater than 0");
            }
        int[] openSitesArr = new int[trials];
        this.trials = trials;
        int columns = n;
        p = new double[trials];
        for (int k = 0; k < trials; k++) {
            // create array of integers 1 to n*n
            int[] all_sites = new int[n*n];
            for (int i = 0; i < n*n; i++) {
                all_sites[i] = i;
            }
            // shuffle the all_sites array
            StdRandom.shuffle(all_sites);
            //
            int openSites = 0;
            Percolation perc = new Percolation(n);
            int next_site = 0;
            while (!perc.percolates()) {
                int next_1D = all_sites[next_site];
                int next_i = next_1D / columns + 1;
                int next_j = next_1D % columns + 1;
                // open a random site
                perc.open(next_i, next_j);
                openSites++;
                next_site++;
                }
            openSitesArr[k] = openSites;
            p[k] = (double) openSites/(double) (n*n);
            }
    }
    public double mean() {           // sample mean of percolation threshold
        return StdStats.mean(p);
    }
    public double stddev() {         // sample stddev of percolation threshold
        if (trials == 1) return Double.NaN;
        else return StdStats.stddev(p);
    }
    public double confidenceLo() {   // low  endpoint of 95% confidence interval
        if (trials == 1) return Double.NaN;
        else return mean() - 1.96 * stddev()/Math.sqrt(trials);
    }
    public double confidenceHi() {   // high endpoint of 95% confidence interval
        if (trials == 1) return Double.NaN;
        else return mean() + 1.96 * stddev()/Math.sqrt(trials);
    }

    public static void main(String[] args) {
        // takes two command-line arguments n and trials, performs trials
        // independent computational experiments on an n-by-n grid, and
        // prints the mean, std deviation, and the 95% confidence
        // interval for the percolation threshold. 
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        
        PercolationStats ps = new PercolationStats(n, trials);
        

        
        StdOut.printf(
            "mean                    = %f\n" , ps.mean()
             );
        StdOut.printf(
            "stddev                  = %f\n" , ps.stddev()
             );
        StdOut.printf(
            "95%% confidence interval = %f %f\n", ps.confidenceLo(),
                                                  ps.confidenceHi()
            );
        }
}
