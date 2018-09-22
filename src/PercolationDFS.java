import java.util.Arrays;

/**
 * Simulate percolation thresholds for a grid-base system using
 * depth-first-search, aka 'flood-fill' techniques for determining if the top of
 * a grid is connected to the bottom of a grid.
 * <P>
 * Modified from the COS 226 Princeton code for use at Duke. The modifications
 * consist of supporting the <code>IPercolate</code> interface, renaming methods
 * and fields to be more consistent with Java/Duke standards and rewriting code
 * to reflect the DFS/flood-fill techniques used in discussion at Duke.
 * <P>
 * 
 * @author Kevin Wayne, wayne@cs.princeton.edu
 * @author Owen Astrachan, ola@cs.duke.edu
 * @author Jeff Forbes, forbes@cs.duke.edu
 */

public class PercolationDFS implements IPercolate {
	protected int[][] myGrid;
	protected int myOpenCount;
	/**
	 * Initialize a grid so that all cells are blocked.
	 * 
	 * @param n
	 *            is the size of the simulated (square) grid
	 */
	public PercolationDFS(int n) {					//constructor- initalizes grid and open count. Marks each grid site as blocked to begin
		myGrid = new int[n][n];
		myOpenCount = 0;
		for (int[] row : myGrid)
			Arrays.fill(row, BLOCKED);
	}

	public void open(int row, int col) {			//'opens' sites in the grid and makes a call to updateOnOpen
		if (myGrid[row][col] != BLOCKED)
			return;
		myOpenCount += 1;
		myGrid[row][col] = OPEN;
		if (row == 0) {
			myGrid[row][col]	 = FULL;
		}
		updateOnOpen(row,col);
	}

	public boolean isOpen(int row, int col) {				//determines if the site is open or closed with a boolean
		return myGrid[row][col] != BLOCKED;
	}

	public boolean isFull(int row, int col) {				//determines if water has flowed into an open site
		return myGrid[row][col] == FULL;
	}

	private void clearFull() {								//resets the full sites to open sites...part of this DFS technique
		for (int i = 0; i < myGrid.length; i++) {
			for (int j = 0; j < myGrid[0].length; j++) {
				if (myGrid[i][j] == FULL) {
					myGrid[i][j] = OPEN;
				}
			}
		}
	}

	protected void updateOnOpen(int row, int col) {				// Depth first search update method
		clearFull();
		for (int k = 0; k < myGrid[0].length; k++)
			dfs(0, k);
	}

	public boolean percolates() {											//checks for percolation (is there a path of full sites from top to bottom?)
		for (int col = 0; col < myGrid[myGrid.length - 1].length; col++)
			if (isFull(myGrid.length - 1, col))
				return true;
		return false;
	}

	/**
	 * Private helper method to mark all cells that are open and reachable from
	 * (row,col).
	 * 
	 * @param row
	 *            is the row coordinate of the cell being checked/marked
	 * @param col
	 *            is the col coordinate of the cell being checked/marked
	 */
	protected void dfs(int row, int col) {			//dfs uses recursie calls (see lines 95-98)
		// out of bounds?
		if (! inBounds(row,col)) return;
		
		// full or open, don't process
		if (isFull(row, col) || !isOpen(row, col))
			return;
		
		myGrid[row][col] = FULL;
		dfs(row - 1, col);
		dfs(row, col - 1);
		dfs(row, col + 1);
		dfs(row + 1, col);
	}

	public int numberOfOpenSites() {			//returns the number of open sites, which is a class variable, so it is easily accessible
		return myOpenCount;
	}
	
	/**
	 * Determine if (row,col) is valid for given grid
	 * @param row specifies row
	 * @param col specifies column
	 * @return true if (row,col) on grid, false otherwise
	 */
	protected boolean inBounds(int row, int col) {					//ensures that the row and col are in the bounds of the grid
		if (row < 0 || row >= myGrid.length) return false;
		if (col < 0 || col >= myGrid[0].length) return false;
		return true;
	}

}
