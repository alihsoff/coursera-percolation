import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF union;
    private WeightedQuickUnionUF backwash;
    private boolean[][] isOpen;
    private final int SIZE, SOURCE_INDEX, SINK_INDEX;
    private int numberOfOpenSites;

    // creates SIZE-by-SIZE grid, with all sites initially blocked
    public Percolation(int n){
        if (n <= 0)
            throw new IllegalArgumentException("n cannot be less than one");
        int numNodes = n*n + 2;
        union = new WeightedQuickUnionUF(numNodes);
        backwash = new WeightedQuickUnionUF(numNodes-1);
        isOpen = new boolean[n+1][n+1];
        SIZE = n;
        SOURCE_INDEX = n * n;
        SINK_INDEX = n * n + 1;

        for (int i = 0; i < n; i++) {
            union.union(SOURCE_INDEX, i);
            union.union(SINK_INDEX, (n - 1) * n + i); // the last row connect to sink

            backwash.union(SOURCE_INDEX, i); // the first row connect to source
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col){

        validate(row,col);

        if(!isOpen(row,col)) {
            isOpen[row][col] = true;
            numberOfOpenSites++;
        }

        // connect to neighbor sites
        if (row - 1 > 0 && isOpen[row - 1][col]) {
            union.union(encode(row, col), encode(row - 1, col));
            backwash.union(encode(row, col), encode(row - 1, col));
        }
        if (row + 1 <= SIZE && isOpen[row + 1][col]) {
            union.union(encode(row, col), encode(row + 1, col));
            backwash.union(encode(row, col), encode(row + 1, col));
        }
        if (col - 1 > 0 && isOpen[row][col - 1]) {
            union.union(encode(row, col), encode(row, col - 1));
            backwash.union(encode(row, col), encode(row, col - 1));
        }
        if (col + 1 <= SIZE && isOpen[row][col + 1]) {
            union.union(encode(row, col), encode(row, col + 1));
            backwash.union(encode(row, col), encode(row, col + 1));
        }
        
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        validate(row,col);

        return isOpen[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row,col);

        // if any box is open and connected to source, we'll fill up the substance
        return isOpen[row][col] && isConnectForBackwash(encode(row, col), SOURCE_INDEX);
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates(){
        if (SIZE == 1) return isOpen(1, 1);
        return isConnectForUnion(SOURCE_INDEX,SINK_INDEX);
    }

    private void validate(int row, int col) {
        if (row <= 0 || row > SIZE) throw new IndexOutOfBoundsException("row index out of bounds");
        if (col <= 0 || col > SIZE) throw new IndexOutOfBoundsException("col index out of bounds");
    }

    private int encode(int row,int col){
        validate(row,col);

        return (row - 1) * SIZE + (col - 1);
    }

    private boolean isConnectForUnion(int p, int q) {
        return union.find(p) == union.find(q);
    }

    private boolean isConnectForBackwash(int p, int q) {
        return backwash.find(p) == backwash.find(q);
    }

}