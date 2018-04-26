import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;
import java.util.List;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray suffix = new CircularSuffixArray(s);
        char[] t = new char[suffix.length()];
        int first = 0;
        for (int i = 0; i < t.length; i++) {
            t[i] = s.charAt((suffix.index(i)+t.length-1) % t.length);
            if (suffix.index(i) == 0) {
                first = i;
            }
        }
        BinaryStdOut.write(first);
        for (int i = 0; i < t.length; i++) {
            BinaryStdOut.write(t[i]);
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        List<Character> l = new ArrayList<>();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            l.add(c);
        }
        char[] t = new char[l.size()];
        for (int i = 0; i < l.size(); i++) {
            t[i] = l.get(i);
        }

        int[] next = new int[t.length];
        int R = 256;
        int[] count = new int[R+1];
        char[] a = new char[t.length];
        for (int i = 0; i < t.length; i++) {
            count[t[i]+1]++;
        }
        for (int i = 0; i < R; i++) {
            count[i+1] += count[i];
        }
        for (int i = 0; i < t.length; i++) {
            a[count[t[i]]] = t[i];
            next[count[t[i]]] = i;
            count[t[i]]++;
        }
        for (int i = 0; i < next.length; i++) {
            BinaryStdOut.write(a[first]);
            first = next[first];
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            BurrowsWheeler.transform();
        } else if (args[0].equals("+")) {
            BurrowsWheeler.inverseTransform();
        }
    }
}