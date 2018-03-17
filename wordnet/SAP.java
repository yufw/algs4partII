import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {
    private final Digraph G;

    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("WordNet: G must not be null");
        }
        this.G = new Digraph(G);
    }

    public int length(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V()) {
            throw new IllegalArgumentException("length: v and w must between 0 and G.V() - 1");
        }
        BreadthFirstDirectedPaths p1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths p2 = new BreadthFirstDirectedPaths(G, w);
        int min = -1;
        for (int i = 0;  i < G.V(); i++) {
            if (!p1.hasPathTo(i) || !p2.hasPathTo(i)) {
                continue;
            }
            int len = p1.distTo(i) + p2.distTo(i);
            if (min == -1 || len < min) {
                min = len;
            }
        }
        return min;
    }

    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V()) {
            throw new IllegalArgumentException("ancestor: v and w must between 0 and G.V() - 1");
        }
        BreadthFirstDirectedPaths p1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths p2 = new BreadthFirstDirectedPaths(G, w);
        int min = -1;
        int anc = -1;
        for (int i = 0;  i < G.V(); i++) {
            if (!p1.hasPathTo(i) || !p2.hasPathTo(i)) {
                continue;
            }
            int len = p1.distTo(i) + p2.distTo(i);
            if (min == -1 || len < min) {
                min = len;
                anc = i;
            }
        }
        return anc;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("length: v and w must not be null");
        }
        for (int i : v) {
            if (i < 0 || i >= G.V()) {
                throw new IllegalArgumentException("length: v must between 0 and G.V() - 1");
            }
        }
        for (int i : w) {
            if (i < 0 || i >= G.V()) {
                throw new IllegalArgumentException("length: w must between 0 and G.V() - 1");
            }
        }
        BreadthFirstDirectedPaths p1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths p2 = new BreadthFirstDirectedPaths(G, w);
        int min = -1;
        for (int i = 0;  i < G.V(); i++) {
            if (!p1.hasPathTo(i) || !p2.hasPathTo(i)) {
                continue;
            }
            int len = p1.distTo(i) + p2.distTo(i);
            if (min == -1 || len < min) {
                min = len;
            }
        }
        return min;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("ancestor: v and w must not be null");
        }
        for (int i : v) {
            if (i < 0 || i >= G.V()) {
                throw new IllegalArgumentException("ancestor: v must between 0 and G.V() - 1");
            }
        }
        for (int i : w) {
            if (i < 0 || i >= G.V()) {
                throw new IllegalArgumentException("ancestor: w must between 0 and G.V() - 1");
            }
        }
        BreadthFirstDirectedPaths p1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths p2 = new BreadthFirstDirectedPaths(G, w);
        int min = -1;
        int anc = -1;
        for (int i = 0;  i < G.V(); i++) {
            if (!p1.hasPathTo(i) || !p2.hasPathTo(i)) {
                continue;
            }
            int len = p1.distTo(i) + p2.distTo(i);
            if (min == -1 || len < min) {
                min = len;
                anc = i;
            }
        }
        return anc;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
