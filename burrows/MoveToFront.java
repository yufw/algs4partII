import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        int R = 256;
        char[] seq = new char[R];
        for (char i = 0; i < R; i++) {
            seq[i] = i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char i;
            for (i = 0; i < R; i++) {
                if (seq[i] == c) {
                    break;
                }
            }
            BinaryStdOut.write(i);
            for (char j = i; j > 0; j--) {
                seq[j] = seq[j-1];
            }
            seq[0] = c;
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        int R = 256;
        char[] seq = new char[R];
        for (char i = 0; i < R; i++) {
            seq[i] = i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char i = BinaryStdIn.readChar();
            BinaryStdOut.write(seq[i]);
            char c = seq[i];
            for (char j = i; j > 0; j--) {
                seq[j] = seq[j-1];
            }
            seq[0] = c;
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            MoveToFront.encode();
        } else if (args[0].equals("+")) {
            MoveToFront.decode();
        }
    }
}
