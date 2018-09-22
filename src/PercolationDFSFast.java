import java.util.NoSuchElementException;

public class PercolationDFSFast extends PercolationDFS{

	public PercolationDFSFast(int n) {						//see constructor for Percolation DFS...this class extends that class
		super(n);
	}
	
	@Override
	public boolean isOpen(int row, int col) {						// checks bounds and then extends the method from Percolation DFS to determine whether or not a site is open
		if (!inBounds(row, col)) {
			throw new IndexOutOfBoundsException("out of bounds");
		}
		return super.isOpen(row,col);
	}
	
	@Override
	public boolean isFull(int row, int col) {					// checks bounds, the determines if the site is full based on the method from Percolation DFS
		if (!inBounds(row, col)) {
			throw new IndexOutOfBoundsException("out of bounds");
		}
		return super.isFull(row, col);
	}
	
	@Override
	public void open(int row, int col) {								//checks bound, then determines if the site is open based on the method from PercolationDFS
		if (!inBounds(row, col)) {
			throw new IndexOutOfBoundsException("out of bounds");
		}
		super.open(row, col);
	}
	
	@Override
	protected void updateOnOpen(int row, int col) {						//checks bounds, then asks about surrounding sites. If a neighbor is in bounds and full, the current site is filled and recursive calls are made to other neighbors. 
//		if (!inBounds(row, col)) {
//			throw new IndexOutOfBoundsException("out of bounds");	
//		}
		if ((row == 0)
			||(inBounds(row -1 , col) && isFull(row - 1, col))
			|| (inBounds(row, col -1) && isFull(row, col -1))
			|| (inBounds(row, col +1) && isFull(row, col + 1))
			|| (inBounds(row +1, col) && isFull(row +1, col))){
			
			myGrid[row][col] = FULL;
			dfs(row -1, col);
			dfs(row, col - 1);
			dfs(row, col + 1);
			dfs(row + 1, col);
		}
	}

}
