import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.TreeSet;

public class BoggleSolver {
    private final TrieSET dict = new TrieSET();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String s : dictionary) {
            dict.add(s);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        int rows = board.rows();
        int cols = board.cols();
        boolean[][] marked = new boolean[rows][cols];
        TreeSet<String> words = new TreeSet<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                build(i, j, "", words, marked, board, rows, cols);
            }
        }
        return words;
    }

    private void build(int m, int n, String prefix, TreeSet<String> words, boolean[][] marked, BoggleBoard board, int rows, int cols) {
        marked[m][n] = true;

        char letter = board.getLetter(m, n);
        String word = prefix + letter;
        if (letter == 'Q') {
            word += 'U';
        }

        if (word.length() >= 3 && dict.contains(word)) {
            words.add(word);
        }
        if (dict.hasPrefix(word)) {
            for (int i = m - 1; i <= m + 1; i++) {
                for (int j = n - 1; j <= n + 1; j++) {
                    if (i < 0 || i >= rows || j < 0 || j >= cols || marked[i][j]) continue;
                    build(i, j, word, words, marked, board, rows, cols);
                }
            }
        }
        marked[m][n] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!dict.contains(word)) return 0;
        int length = word.length();
        if (length < 3) return 0;
        if (length < 5) return 1;
        if (length == 5) return 2;
        if (length == 6) return 3;
        if (length == 7) return 5;
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
