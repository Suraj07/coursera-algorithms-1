import edu.princeton.cs.algs4.Queue;

public final class Board {
    
    private final int[][] grid;
    private final int n;
    
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        // get n
        n = blocks.length;
        
        grid = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = blocks[i][j];
            }
        }
    }
    
    // board dimension n
    public int dimension() {
        return n;
    }
    
    // number of blocks out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int num = grid[i][j];
                if (num == 0) {
                    continue;
                }
                if (num != ((i * n) + (j + 1))) {
                    count++;
                }
            }
        }
        return count;
    }
    
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int num = grid[i][j];
                if (num == 0) {
                    continue;
                }
                if (num != ((i* n) + (j + 1))) {
                    int posy = (num - 1) / n;
                    int posx = (num - 1) % n;
                    count += Math.abs(posy - i) + Math.abs(posx - j);
                }
            }
        }
        return count;        
    }
    
    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            int xlimit = n;
            if (i == (n - 1)) {
                xlimit = n - 1;
            }
            for (int j = 0; j < xlimit; j++) {
                if (grid[i][j] != ((i* n) + (j + 1))) {
                    return false;
                }                
            }
        }
        return true;
    }
    
    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = grid[i][j];
            }
        }
        int x, y;
        if (board[0][0] == 0) {
            x = 1;
            y = 0;
        } else {
            x = 0;
            y = 0;
        }
        int x1 = (n - 1), y1 = (n - 1);
        boolean first = true, done = false;
        for (int i = 0; i < n; i++) {
            if (done) {
                break;
            }
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 0) {
                    continue;
                }
                if (first) {
                    first = false;
                } else {
                    x1 = j;
                    y1 = i;
                    done = true;
                    break;
                }
            }
        }
        // exchange
        int temp = board[y][x];
        board[y][x] = board[y1][x1];
        board[y1][x1] = temp;
        
        return new Board(board);
    }
    
    // does this board equal y?
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Board that = (Board) other;
        if (n != that.dimension()) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] != that.grid[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    // all neighboring boards
    public Iterable<Board> neighbors() {
        // use queue to return iterator
        Queue<Board> boardQueue = new Queue<Board>();
        
        int posx = 0, posy = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0) {
                    posx = j;
                    posy = i;
                    break;
                }
            }
        }
        
        int[][] board = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = grid[i][j];
            }
        }
        
        // from up
        if (posy > 0) {
            // exchange empty block with element up of it.
            board[posy][posx] = board[posy - 1][posx];
            board[posy - 1][posx] = 0;
            boardQueue.enqueue(new Board(board));
            // reset board
            board[posy - 1][posx] = board[posy][posx];
            board[posy][posx] = 0;
        }
        
        // from down
        if (posy < (n - 1)) {
            // exchange empty block with element up of it.
            board[posy][posx] = board[posy + 1][posx];
            board[posy + 1][posx] = 0;
            boardQueue.enqueue(new Board(board));
            // reset board
            board[posy + 1][posx] = board[posy][posx];
            board[posy][posx] = 0;            
        }
        
        // from left
        if (posx > 0) {
            // exchange empty block with element up of it.
            board[posy][posx] = board[posy][posx - 1];
            board[posy][posx - 1] = 0;
            boardQueue.enqueue(new Board(board));
            // reset board
            board[posy][posx - 1] = board[posy][posx];
            board[posy][posx] = 0;
        }
        
        // from right
        if (posx < (n - 1)) {
            // exchange empty block with element up of it.
            board[posy][posx] = board[posy][posx + 1];
            board[posy][posx + 1] = 0;
            boardQueue.enqueue(new Board(board));
            // reset board
            board[posy][posx + 1] = board[posy][posx];
            board[posy][posx] = 0;
        }
        
        return boardQueue;
    }
    
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", grid[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    // unit tests (not graded)
    public static void main(String[] args) {
        
    }
}
