
public class PercolationUF implements IPercolate {					//using the IPercolate interface
	IUnionFind myFinder;
	boolean[][] myGrid;
	int count;
	private final int VTOP;									//introducing VTOP and VBOTTOM to decrease runtime with unioning
	private final int VBOTTOM;
	
	
	public PercolationUF(int size, IUnionFind finder) {			//constructor initializes the IUnionFind finder and makes space for VTOP and VBOTTOM in myFinder
		myFinder = finder;
		myFinder.initialize(size * size + 2);
		myGrid = new boolean[size][size];
		count = size;
		VTOP = size * size;
		VBOTTOM = size * size + 1;
	}

	@Override
	public void open(int row, int col) {						// because of union find, myGrid is composed of booleans this time
		if (!inBounds(row, col)) {
			throw new IndexOutOfBoundsException("out of bounds");	
		}
		myGrid[row][col] = true;
		updateOnOpen(row,col);
	}
	
	public void updateOnOpen(int row, int col) {			// unions site based on the condition of neighborhing sites and proximity to VTOP and VBottom (row)
		//union with top and bottom rows if appropriate
		int center = getIndex(row, col);
		
		if (row == 0)
			myFinder.union(center, VTOP);
		
		//union with each of the neighboring sites if appropriate
		if (inBounds(row -1 , col) && isOpen(row - 1, col)) {
			int up = getIndex(row - 1, col);
			myFinder.union(center, up);
		}
		if (inBounds(row, col -1) && isOpen(row, col -1)) {
			int left = getIndex(row, col -1);
			myFinder.union(center, left);
		}
		if (inBounds(row, col +1) && isOpen(row, col + 1)) {
			int right = getIndex(row, col +1);
			myFinder.union(center, right);
		}
		if (row + 1 == myGrid.length)
			myFinder.union(center, VBOTTOM);
		
			else if(isOpen(row +1, col)) {
			int down = getIndex(row +1, col);
			myFinder.union(center, down);	
		}
	}
	
	
	private int getIndex(int row, int col) {			//returns an index value based on location in grid
		return row * myGrid.length + col;
	}
	

	@Override
	public boolean isOpen(int row, int col) {					//returns the boolean at the given site which signifies open or not
		if (!inBounds(row, col)) {
			throw new IndexOutOfBoundsException("out of bounds");	
		}
		return myGrid[row][col];
	}

	@Override
	public boolean isFull(int row, int col) {						//determines if a site is full by checking to see if it is connected to VTOP
		if (!inBounds(row, col)) {
			throw new IndexOutOfBoundsException("out of bounds");	
		}
		int siteIndex = getIndex(row, col);
		return myFinder.connected(VTOP, siteIndex);
	}

	@Override
	public boolean percolates() {							//does the system percolate? this method sees if the top of the grid (VTOP) has a path to the bottom (VBottom)
		return myFinder.connected(VTOP, VBOTTOM);
	}

	protected boolean inBounds(int row, int col) { 				//ensure that the row and column are in the grid
		if (row < 0 || row >= myGrid.length) return false;
		if (col < 0 || col >= myGrid[0].length) return false;
		return true;
	}

	@Override	
	public int numberOfOpenSites() {					//loops through each site in grid and returns a count of number of open sites
		int numOpen = 0;
		for (int k = 0; k < count; k++){
			for (int j = 0; j < count; j++){
				if (myGrid[k][j] == true) 
					numOpen++;
			}
		}
		return numOpen;
	}

}
