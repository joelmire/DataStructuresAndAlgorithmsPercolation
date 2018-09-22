import java.util.*;

/**
 * Compute statistics on Percolation after performing T independent experiments on an N-by-N grid.
 * Compute 95% confidence interval for the percolation threshold, and  mean and std. deviation
 * Compute and print timings
 * 
 * @author Kevin Wayne
 * @author Jeff Forbes
 * @author Josh Hug
 */

public class PercolationStats {									// constructor
	public static int RANDOM_SEED = 1234;
	public static Random ourRandom = new Random(RANDOM_SEED);
	double[] array;
	
	public PercolationStats(int N, int T) {
		if (!inBounds(N, T)) {
			throw new IllegalArgumentException("illegal arguments");	
		}
		array = new double[T];
		PercolationUF current;
		IUnionFind finder;
		for (int k = 0; k < T; k++) {					// loop through number of simulations
			finder = new QuickUWPC();
			current = new PercolationUF(N, finder);
			ArrayList<int[]> grid = new ArrayList<>(N * N);
			for (int i = 0; i < N; i++) {					// prepare grid
				for (int j = 0; j < N; j++)
					grid.add(new int[]{i,j});
			}
			Collections.shuffle(grid, ourRandom);			
			for (int q = 0; !current.percolates(); q++)					//open sites until percolates
				current.open(grid.get(q)[0], grid.get(q)[1]);
			array[k] = current.numberOfOpenSites() / (N * N * 1.0);	
		}
	}
	
	public double mean() {							//returns the mean percolation threshold from values in array
		return StdStats.mean(array);
	}

	public double stddev(){							//returns the standard deviation for the percolation thresholds in array
		return StdStats.stddev(array);
	}
	
	public double confidenceLow() {										//lower bound of 95% confidence interval
		return mean() - (stddev() * 1.96) / Math.sqrt(array.length);
	}
	
	public double confidenceHigh() {
		return mean() + (stddev() * 1.96) / Math.sqrt(array.length);    //upper bound of 95% confidence interval
	}	
	
	protected boolean inBounds(int N, int T) {							//ensures that N and T are legal arguments... cannot be negative
		if (N <= 0 || T <= 0) return false;
		return true;
	}
	
	public static void main(String[] args) {							// main method used for analysis
		int N = 50;
		int T = 100;
		double start =  System.nanoTime();
		PercolationStats example = new PercolationStats(N, T);
		double end =  System.nanoTime();
		double time =  (end-start)/1e9;
		System.out.printf("mean: %1.4f, "
				+ "stddev: %1.4f, "
				+ "C.I: %1.4f - %1.4f, "
				+ "time: %1.4f\n"
				,example.mean(),example.stddev(),example.confidenceLow(), example.confidenceHigh(),time);	
	}
}

