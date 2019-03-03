import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    private int gridSize;
    private int gridSize1D;
    // an array to keep track of sites open
    private boolean[] grid;
    private WeightedQuickUnionUF gridUF;
    // another UF object to avoid backwash
    private WeightedQuickUnionUF gridUF1;
    
    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Invalid grid size n");
        }
        gridSize = n;
        // converting 2D matrix to a 1D array
        // it's size will be n^2
        // 2 is added to account for virtual top and bottom
        gridSize1D = (n * n) + 2;
        gridUF = new WeightedQuickUnionUF(gridSize1D);
        gridUF1 = new WeightedQuickUnionUF(gridSize1D);
        
        grid = new boolean[gridSize1D];
        for (int i = 0; i < gridSize1D; i++) {
            grid[i] = false;
        }
        
    }
    
    private void validateIndices(int x, int y) {
        if ((x < 1) || (x > gridSize)) {
            throw new IndexOutOfBoundsException("row index out of bounds");
        }
        if ((y < 1) || (y > gridSize)) {
            throw new IndexOutOfBoundsException("column index out of bounds");
        }
    }
    
    private int xyTo1D(int x, int y) {
        int result;
        // y-1 is not done to account for virtual top, with index = 0
        result = ((x - 1) * gridSize) + y;
        return result;
    }
    
    private boolean validIndex(int x, int y) {
        if ((x < 1) || (x > gridSize)) {
            return false;
        }
        
        if ((y < 1) || (y > gridSize)) {
            return false;
        }
        
        return true;
    }
    
    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        validateIndices(i, j);
        // mark this site as open
        int index = xyTo1D(i, j);
        if (grid[index]) {
           return; 
        }
        grid[index] = true;
        // quickunion with it's open neighbors
        // 4 neighbors : {(i-1, j), (i, j-1), (i, j+1), (i+1, j)}
        if (validIndex(i - 1, j)) {
            int nIndex = xyTo1D(i - 1, j);
            if (grid[nIndex]) {
                gridUF.union(nIndex, index);
                gridUF1.union(nIndex, index);
            }
        }
        
        if (validIndex(i, j - 1)) {
            int nIndex = xyTo1D(i, j - 1);
            if (grid[nIndex]) {
                gridUF.union(nIndex, index);
                gridUF1.union(nIndex, index);
            }
        }
        
        if (validIndex(i, j + 1)) {
            int nIndex = xyTo1D(i, j + 1);
            if (grid[nIndex]) {
                gridUF.union(nIndex, index);
                gridUF1.union(nIndex, index);
            }
        }
        
        if (validIndex(i + 1, j)) {
            int nIndex = xyTo1D(i + 1, j);
            if (grid[nIndex]) {
                gridUF.union(nIndex, index);
                gridUF1.union(nIndex, index);
            }
        }
        
        // if row is 1, then connect it to virtual top
        if (i == 1) {
            gridUF.union(0, index);
            gridUF1.union(0, index);
        }
        
        // if row is n, then connect it to virtual bottom
        if (i == gridSize) {
            gridUF.union((gridSize1D - 1), index);
        }
    }
    
    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        validateIndices(i, j);
        int index = xyTo1D(i, j);
        return grid[index];
    }
    
    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        validateIndices(i, j);
        int index = xyTo1D(i, j);
        boolean result = gridUF1.connected(0, index);
        return result;
    }
    
    // number of open sites
    public int numberOfOpenSites() {
        int openSiteCount = 0;
        for (int i = 0; i < gridSize1D; i++) {
            if (grid[i]) {
                openSiteCount++;
            }
        }
        return openSiteCount;
    }       
    
    // does the system percolate?
    public boolean percolates() {
        boolean result = gridUF.connected(0, gridSize1D - 1);
        return result;
    }

    // test client (optional)
    public static void main(String[] args) {
        
    }  
}