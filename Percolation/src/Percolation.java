import edu.princeton.cs.algs4.WeightedQuickUnionUF;
//import edu.princeton.cs.algs4.PercolationStats;

public class Percolation 
    {
    
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF uf2;
    private int size;
    private int columns;
    private int rows;
    private int topSite;
    private int bottomSite;
    private int totalSites;
    private boolean[] siteIsOpen;
    private boolean[] siteIsFull;
    
    public Percolation(int n)
        {
        // create n-by-n grid
        // idealized as an instance of 1-D union-find data structure
        // n argument is the number of rows and/or columns
        //
        // define a pseudo-site at top, at site size
        // define a pseudo-site at bottom, at site size+1
        // define a dummy-site that is always closed, at site size+2
        // Each site is initially in its own component.
        // Each site is initially closed, empty, and unconnected
        //
        // size of grid is equal to number of rows/columns squared.
        //
        // validate that n is greater than 0
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException(
                "n must be greater than 0");
        }
        size = n*n;
        rows = n;
        columns = n;
        topSite = size;
        bottomSite = size+1;
        totalSites = size+2;
        
        uf = new WeightedQuickUnionUF(totalSites);
        uf2 = new WeightedQuickUnionUF(totalSites);
        
        // initialize all sites to closed and empty
        siteIsOpen = new boolean[totalSites];
        siteIsFull = new boolean[totalSites];
        for (int i = 0; i < totalSites; i++) {
            siteIsOpen[i] = false;
            siteIsFull[i] = false;
        }
        }

    private int xyTo1D(int i, int j)
        {
        // convert a 2-D (row, column) pair to 1-D union-find object index,
        // beginning at index zero.
        // number of columns is the square root of the size
        return (i - 1) * columns + j - 1;
        }

    private void validate(int i, int j)
        {
        // validate the row, column indices
        // must be between 1 and n inclusive
        if (i < 1 || i > columns) {
            throw new IndexOutOfBoundsException("row index i out of bounds");
        }
        if (j < 1 || j > columns) {
            throw new IndexOutOfBoundsException("column index j out of bounds");
        }
        }

    public void open(int i, int j)          
        {
        validate(i, j);
        // open site (row i, column j)
        // if (!isOpen(i, j)) {
        // }
        siteIsOpen[xyTo1D(i, j)] = true;
        // connect to pseudo-site at the top
        if (i == 1) {
            uf.union(topSite, xyTo1D(i, j));
            uf2.union(topSite, xyTo1D(i, j));
        }
        // connect to pseudo-site at the bottom
        if (i == rows) uf2.union(bottomSite, xyTo1D(i, j));
        // connect to adjacent sites below
        if (i > 1 && siteIsOpen[xyTo1D(i-1, j)]) {
            uf.union(xyTo1D(i-1, j), xyTo1D(i, j));
            uf2.union(xyTo1D(i-1, j), xyTo1D(i, j));
        }
        // connect to adjacent sites above
        if (i < rows && siteIsOpen[xyTo1D(i+1, j)]) {
            uf.union(xyTo1D(i+1, j), xyTo1D(i, j));
            uf2.union(xyTo1D(i+1, j), xyTo1D(i, j));
        }
        // connect to adjacent sites to the left
        if (j > 1 && siteIsOpen[xyTo1D(i, j-1)]) {
            uf.union(xyTo1D(i, j-1), xyTo1D(i, j));
            uf2.union(xyTo1D(i, j-1), xyTo1D(i, j));
        }
        // connect to adjacent sites to the right
        if (j < columns && siteIsOpen[xyTo1D(i, j+1)]) {
            uf.union(xyTo1D(i, j+1), xyTo1D(i, j));
            uf2.union(xyTo1D(i, j+1), xyTo1D(i, j));
        }
        }


    public boolean isOpen(int i, int j)     
        {
        // is site (row i, column j) open?
        validate(i, j);
        return (siteIsOpen[xyTo1D(i, j)]);
        }

    public boolean isFull(int i, int j)     
        {
        // is site (row i, column j) full?
        // determine if site is in same component as top pseudo-site   
        // uf.connected(int site-ij, int top-site)   
        validate(i, j);
        if (uf.connected(topSite, xyTo1D(i, j))) {
            siteIsFull[xyTo1D(i, j)] = true;
        }
        else {
            siteIsFull[xyTo1D(i, j)] = false;
        }
        return siteIsFull[xyTo1D(i, j)];
        }

    public boolean percolates()             
        {
        // does the system percolate?
        // determine if any sites in bottom row are full.
        // if full, connect with bottom site
        //
        // for (int i = (rows-1)*columns; i < size; i++) {
        //     if (siteIsFull[i]) uf.union(i, bottomSite);
        // }
        //
        // determine if top pseudo-site is in same 
        // component as bottom pseudo-site   
        return uf2.connected(topSite, bottomSite);
        }

    public static void main(String[] args)  // test client (optional)
        {
        }
    }
