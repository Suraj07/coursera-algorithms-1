import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public final class Solver {
    
    private final boolean solvable;
    private final Node last;
    private int numOfMoves;
    private Stack<Board> stack;
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException("board is null");
        }
        
        MinPQ<Node> pq = new MinPQ<Node>();
        pq.insert(new Node(initial, 0, null));
        
        MinPQ<Node> tpq = new MinPQ<Node>();
        tpq.insert(new Node(initial.twin(), 0, null));
        
        while (true) {
            Node node = pq.delMin();
            if (node.isGoal()) {
                solvable = true;
                last = node;
                break;
            }
            
            Node tnode = tpq.delMin();
            if (tnode.isGoal()) {
                solvable = false;
                last = null;
                break;
            }
            
            for (Board b : node.board.neighbors()) {
                if (node.previous != null) {
                    if (b.equals(node.previous.board)) {
                        continue;
                    }
                }
                pq.insert(new Node(b, (node.movesMade + 1), node));
            }
            
            for (Board b : tnode.board.neighbors()) {
                if (node.previous != null) {
                    if (b.equals(tnode.previous.board)) {
                        continue;
                    }
                }
                tpq.insert(new Node(b, (tnode.movesMade + 1), tnode));
            }
        }
        
        if (solvable) {
            stack = new Stack<Board>();
            stack.push(last.board);
            Node node = last;
            while (node.previous != null) {
                node = node.previous;
                stack.push(node.board);
            }
            numOfMoves = stack.size() - 1;
        } else {
            numOfMoves = -1;
            stack  = null;
        }
    }
    
    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }
    
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return numOfMoves;
    }
    
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return stack;
    }
    
    private class Node implements Comparable<Node> {
        
        private final Board board;
        private final int movesMade;
        private final Node previous;
        private final int priority;
        private final int manhattan;
        
        public Node(Board b, int m, Node p) {
            board = b;
            movesMade = m;
            previous = p;
            manhattan = b.manhattan();
            priority = movesMade + manhattan;
        }
        
        public boolean isGoal() {
            return board.isGoal();
        }
        
        public int compareTo(Node that) {
            return (priority - that.priority);
        }
        
        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append("manhattan = " + manhattan);
            s.append("\nmoves = " + movesMade);
            s.append("\npriority = " + priority);
            s.append("\n" + board.toString());
            return s.toString();
        }
        
    }
    
    
    public static void main(String[] args) {
        
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}