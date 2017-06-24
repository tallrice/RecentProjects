import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.ResizingArrayStack;
import edu.princeton.cs.algs4.LinkedStack;
import java.util.Arrays;
import java.util.ArrayList;

public class Board {

    private int[][] blocks;
    private int[][] goalBlocks;
    private int[] manhattanRows;
    private int[] manhattanCols;
    private int dim;
    private int hamming;
    private int manhatten;

    public Board(int[][] blocks) {     // construct a board from an n-by-n array of blocks
        // (where blocks[i][j] = block in row i, column j)
        dim = blocks[0].length;
        this.blocks = myCopy(blocks);
        goalBlocks = goalBlocks(dim);
        manhattanRows = manhattanRows();
        manhattanCols = manhattanCols();
    }

    private int[][] getBlocks() {
        return this.blocks;
    }

    public int dimension() {           // board dimension n
        return dim;
    }

    public int hamming() {             // number of blocks out of place
        int hmg = 0;
        for (int i=0; i<dim; i++) {
            for (int j=0; j<dim; j++) {
                if (blocks[i][j] != 0 && blocks[i][j] != goalBlocks[i][j]) {
                    hmg++;
                }
            }
        }
        return hmg;
    }

    public int manhattan() {           // sum of Manhattan distances between blocks and goal
        int mnh = 0;
        for (int i=0; i<dim; i++) {
            for (int j=0; j<dim; j++) {
                if (this.blocks[i][j] != 0) {
                    mnh = mnh + Math.abs(i - manhattanRows[this.blocks[i][j]]);
                    mnh = mnh + Math.abs(j - manhattanCols[this.blocks[i][j]]);
                }
            }
        }
        return mnh;
    }

    private int[] manhattanRows() {
        int[] mnhRows = new int[dim*dim+1];
        int k = 1;
        for (int i=0; i<dim; i++) {
            for (int j=0; j<dim; j++) {
                mnhRows[k] = i;
                k++;
            }
        }
        return mnhRows;
    }

    private int[] manhattanCols() {
        int[] mnhCols = new int[dim*dim+1];
        int k = 1;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                mnhCols[k] = j;
                k++;
            }
        }
        return mnhCols;
    }
    public boolean isGoal() {          // is this board the goal board?
        for (int i=0; i<dim; i++) {
            for (int j=0; j<dim; j++) {
                if (this.blocks[i][j] != this.goalBlocks[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    private int[][] goalBlocks(int dim) {
           int[][] goalBlocks = new int[dim][dim];
           int goalBlock = 1;
           for (int i=0; i<dim; i++) {
               for (int j=0; j<dim; j++) {
                   goalBlocks[i][j] = goalBlock;
                   goalBlock++;
               }
           }
           goalBlocks[dim-1][dim-1] = 0;
           return goalBlocks;
    }
    public Board twin() {              // a board that is obtained by exchanging any pair of blocks
        int[][] twin = new int[dim][dim];
        int zeroRow = dim;
        int zeroCol = dim;
        for (int i=0; i<dim; i++) {
            for (int j=0; j<dim; j++) {
                twin[i][j] = this.blocks[i][j];
               if (this.blocks[i][j] == 0) {
                   zeroRow = i;
                   zeroCol = j;
               }
            }
        }
        int k = 0;
        int nonZeroRow1 = dim;
        int nonZeroCol1 = dim;
        int nonZeroRow2 = dim;
        int nonZeroCol2 = dim;
        for (int i=0; i<dim && k < 2; i++) {
            for (int j=0; j<dim && k < 2; j++) {
                if (!(i == zeroRow && j == zeroCol) && k == 0) {
                    nonZeroRow1 = i;
                    nonZeroCol1 = j;
                    k++;
                }
                else if (!(i == zeroRow && j == zeroCol) && k == 1) {
                    nonZeroRow2 = i;
                    nonZeroCol2 = j;
                    k++;
                }
            }
        }
        int temp = twin[nonZeroRow2][nonZeroCol2];
        twin[nonZeroRow2][nonZeroCol2] = twin[nonZeroRow1][nonZeroCol1];
        twin[nonZeroRow1][nonZeroCol1] = temp;
        Board twinBoard = new Board(twin);
        return twinBoard;
    }

    public boolean equals(Object y) {  // does this board equal y?
        if (y == this) return true;

        if (y == null) return false;

        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;

        if (that.getBlocks().length != this.blocks.length) return false;

        for (int i=0; i<dim; i++) {
            for (int j=0; j<dim; j++) {
                if (this.blocks[i][j] != that.blocks[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    public Iterable<Board> neighbors() {   // all neighboring boards
        LinkedStack<Board> lsBoard = new LinkedStack<Board>();
        int[][] neighbor = new int[dim][dim];
        int zeroRow = dim;
        int zeroCol = dim;
        for (int i=0; i<dim; i++) {
            for (int j=0; j<dim; j++) {
                neighbor[i][j] = this.blocks[i][j];
                if (this.blocks[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                }
            }
        }
        int k = 0;
        Board neighborBoard;
        if (zeroRow == 0) {
            neighborBoard = new
                Board(swapBlocks(neighbor, zeroRow+1, zeroCol, 
                            zeroRow, zeroCol));
            lsBoard.push(neighborBoard);
        }
        else if (zeroRow == dim-1) {
            neighborBoard = new
                Board(swapBlocks(neighbor, zeroRow-1, zeroCol, 
                            zeroRow, zeroCol));
            lsBoard.push(neighborBoard);
        }
        else {
            neighborBoard = new
                Board(swapBlocks(neighbor, zeroRow+1, zeroCol, 
                            zeroRow, zeroCol));
            lsBoard.push(neighborBoard);
            neighborBoard = new
                Board(swapBlocks(neighbor, zeroRow-1, zeroCol, 
                            zeroRow, zeroCol));
            lsBoard.push(neighborBoard);
        }
        if (zeroCol == 0) {
            neighborBoard = new
                Board(swapBlocks(neighbor, zeroRow, zeroCol+1, 
                            zeroRow, zeroCol));
            lsBoard.push(neighborBoard);
        }
        else if (zeroCol == dim-1) {
            neighborBoard = new
                Board(swapBlocks(neighbor, zeroRow, zeroCol-1, 
                            zeroRow, zeroCol));
            lsBoard.push(neighborBoard);
        }
        else {
            neighborBoard = new
                Board(swapBlocks(neighbor, zeroRow, zeroCol+1, 
                            zeroRow, zeroCol));
            lsBoard.push(neighborBoard);
            neighborBoard = new
                Board(swapBlocks(neighbor, zeroRow, zeroCol-1, 
                            zeroRow, zeroCol));
            lsBoard.push(neighborBoard);
        }
        return lsBoard;
    }

    private int[][] swapBlocks(int[][] neighbor, int i, int j, 
            int zeroRow, int zeroCol) {
        int[][] n = myCopy(neighbor);
        int temp = n[zeroRow][zeroCol];
        n[zeroRow][zeroCol] = n[i][j];
        n[i][j] = temp;
        return n;
    }
    private int[][] myCopy(int[][] currentArray) {
        int[][] newArray = new int[currentArray.length][];
        // METHOD 1
        // for (int i = 0; i < currentArray.length; i++) {
        //     int[] newInnerArray = new int[currentArray[i].length];
        //     for (int j = 0; j < currentArray[i].length; j++) {
        //         newInnerArray[j] = currentArray[i][j];
        //     }
        //     newArray[i] = newInnerArray;
        // }
        // METHOD 2
        for(int i=0; i<currentArray.length; i++) {
            newArray[i] = new int[currentArray[i].length];
            for(int j=0; j<currentArray[i].length; j++) {
                newArray[i][j]=currentArray[i][j];       // }
            }
        }
        return newArray;
    }

    public String toString() {             // string representation of this board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(dim + "\n");
        for (int i=0; i<dim; i++) {
            for (int j=0; j<dim; j++) {
                s.append(String.format("%2d ", this.blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {// unit tests (not graded)

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        // generate a test string
//        int[][] blocks = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
//        Board initial = new Board(blocks);
//        System.out.print(initial.toString());
        //
        // // solve the puzzle
        // Solver solver = new Solver(initial);

        // // print solution to standard output
        // if (!solver.isSolvable())
        //     StdOut.println("No solution possible");
        // else {
        //     StdOut.println("Minimum number of moves = " + solver.moves());
        //     for (Board board : solver.solution())
        //         StdOut.println(board);
        // }
    }
}

