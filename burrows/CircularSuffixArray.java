import java.util.Arrays;

public class CircularSuffixArray {
    private final String s;
    private final Suffix[] suffixes;

    private class Suffix implements Comparable<Suffix> {
        private final int start;

        private Suffix(int start) {
            this.start = start;
        }

        public int compareTo(Suffix that) {
            if (this == that) return 0;
            int n = s.length();
            for (int i = 0; i < n; i++) {
                if (s.charAt((start+i) % n) < s.charAt((that.start+i) % n)) {
                    return -1;
                }
                if (s.charAt((start+i) % n) > s.charAt((that.start+i) % n)) {
                    return +1;
                }
            }
            return 0;
        }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("argument is null");
        }
        this.s = s;
        int n = s.length();
        suffixes = new Suffix[n];
        for (int i = 0; i < n; i++) {
            suffixes[i] = new Suffix(i);
        }
        Arrays.sort(suffixes);
    }

    // length of s
    public int length() {
        return s.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length()) {
            throw new IllegalArgumentException("argument out of range");
        }
        return suffixes[i].start;
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray suffix = new CircularSuffixArray("ABRACADABRA!");
        System.out.println(suffix.length());
        for (int i = 0; i < suffix.length(); i++) {
            System.out.println(suffix.index(i));
        }
    }
}