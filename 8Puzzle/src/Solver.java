import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedStack;
import java.util.LinkedList;
import java.util.HashSet;

public class Solver {

    private MinPQ<SearchNode> iMPQ;
    private MinPQ<SearchNode> tMPQ;
    private Board twin;
    private Board initial;
    private int dim;
    private SearchNode iNode;
    private SearchNode tNode;
    private int totalMoves;
    private SearchNode solNode;
    private boolean isSolv;

    public Solver(Board initial) {         // find a solution to the initial board (using the A* algorithm)
        this.initial = initial;
        dim = initial.dimension();
        // create a twin board
        twin = initial.twin();
        // create search nodes for the initial and twin boards
        iNode = new SearchNode(initial,false);
        tNode = new SearchNode(twin,true);
        // create Min Priority Queues for initial and twin
        iMPQ = new MinPQ<>(100);
        // tMPQ = new MinPQ<>();
        // 1. add nodes to mpqs
        iMPQ.insert(iNode);
        iMPQ.insert(tNode);
        SearchNode iCurrent;
        // 2. delete node with the minimum priority
        iCurrent = iMPQ.delMin();
        while (!iCurrent.board.isGoal()) {
        // 3. insert neighborin nodes onto mpq
                for (Board b : iCurrent.board.neighbors()) {
                    SearchNode n = new SearchNode(b,iCurrent.moves+1,iCurrent,iCurrent.twin);
                    if (iCurrent.prev == null) 
                        iMPQ.insert(n);
                    else if (!n.board.equals(iCurrent.prev.board)) {
                        iMPQ.insert(n);
                    }
               }
            iCurrent = iMPQ.delMin();
        }
        totalMoves = (!iCurrent.twin ? iCurrent.moves : -1);
        solNode = (!iCurrent.twin ? iCurrent : null);
        isSolv = !iCurrent.twin;
    }

    private class SearchNode implements Comparable<SearchNode>{

        private Board board;
        private Board goal;
        private int moves;
        private SearchNode prev;
        private int priority;
        private int boardManhattan;
        private boolean twin;

        private SearchNode(Board board, boolean twin) {
            this.twin = twin;
            boardManhattan = board.manhattan();
            initial = board;
            this.board = board;
            moves = 0;
            prev = null;
            priority = this.board.manhattan() + moves;
        }

        private SearchNode(Board board, int moves, SearchNode prev, boolean twin) {
            this.twin = twin;
            boardManhattan = prev.boardManhattan;
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            priority = this.board.manhattan() + moves;
        }

        public int compareTo(SearchNode that) {
            if (priority == that.priority) {
                if (this.board.hamming() < that.board.hamming())
                    return -1;
                else if (this.board.hamming() > that.board.hamming())
                    return +1;
                else
                    if (this.board.manhattan() < that.board.manhattan())
                        return -1;
                    else if (this.board.manhattan() > that.board.manhattan())
                        return +1;
                    else
                        return 0;
            }
            else if (priority < that.priority) return -1;
            else return +1;
        }
    }

    public boolean isSolvable() {          // is the initial board solvable?
        return isSolv;
    }
    public int moves() {                   // min number of moves to solve initial board; -1 if unsolvable
        return totalMoves;
    }
    public Iterable<Board> solution() {    // sequence of boards in a shortest solution; null if unsolvable
        LinkedList<Board> llBoard;
        if (solNode != null) {
            llBoard = new LinkedList<Board>();
            llBoard.push(solNode.board);
            SearchNode currNode = solNode;
            while (currNode.prev != null) {
                llBoard.push(currNode.prev.board);
                currNode = currNode.prev;
            }
        }
        else llBoard = null;
        return llBoard;
    }
    public static void main(String[] args){// solve a slider puzzle (given below)
//         create initial board from file
         In in = new In(args[0]);
//        In in = new In("/Users/tallrice/algs4/4_8puzzle/puzzle3x3-mt.txt");
//        In in = new In("/Users/tallrice/algs4/4_8puzzle/puzzle04.txt");
         int n = in.readInt();
         int[][] blocks = new int[n][n];
         for (int i = 0; i < n; i++)
             for (int j = 0; j < n; j++)
                 blocks[i][j] = in.readInt();
         Board initial = new Board(blocks);
        //
        // generate a test string
//        int[][] blocks = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
//        Board initial = new Board(blocks);
//        System.out.print(initial.toString());
        // // solve the puzzle
        Solver solver = new Solver(initial);

        // // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
//            for (Board board : solver.solution())
//                StdOut.println(board);
        }
    }
}

