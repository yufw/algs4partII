import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;

public class WordNet {
    private final List<String> synsetsArr;
    private final Map<String, TreeSet<Integer>> nounsMap;
    private final SAP sap;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("WordNet: arguments must not be null");
        }
        synsetsArr = new ArrayList<>();
        nounsMap = new TreeMap<>();

        In in1 = new In(synsets);
        while (in1.hasNextLine()) {
            String line = in1.readLine();
            String[] fields = line.split(",");
            int id = Integer.parseInt(fields[0]);
            synsetsArr.add(fields[1]);
            for (String noun : fields[1].split(" ")) {
                if (nounsMap.containsKey(noun)) {
                    TreeSet<Integer> values = nounsMap.get(noun);
                    values.add(id);
                    nounsMap.put(noun, values);
                } else {
                    TreeSet<Integer> values = new TreeSet<>();
                    values.add(id);
                    nounsMap.put(noun, values);
                }
            }
        }

        Digraph G = new Digraph(synsetsArr.size());

        In in2 = new In(hypernyms);
        while (in2.hasNextLine()) {
            String line = in2.readLine();
            String[] fields = line.split(",");
            int v = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int w = Integer.parseInt(fields[i]);
                G.addEdge(v, w);
            }
        }

        // check it is a rooted DAG
        DirectedCycle dc = new DirectedCycle(G);
        if (dc.hasCycle()) {
            throw new IllegalArgumentException("WordNet: not a rooted DAG, has cycle");
        }
        int zeroOut = 0;
        for (int i = 0; i < G.V(); i++) {
            if (G.outdegree(i) == 0) zeroOut++;
        }
        if (zeroOut != 1) {
            throw new IllegalArgumentException("WordNet: not a rooted DAG, must have only 1 root");
        }

        sap = new SAP(G);
    }

    public Iterable<String> nouns() {
        return nounsMap.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("isNoun: argument must not be null");
        }
        return nounsMap.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("distance: arguments must not be null");
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("distance: isNoun returns false");
        }
        Set<Integer> v = nounsMap.get(nounA);
        Set<Integer> w = nounsMap.get(nounB);
        return sap.length(v, w);
    }

    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("sap: arguments must not be null");
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("sap: isNoun returns false");
        }
        Set<Integer> v = nounsMap.get(nounA);
        Set<Integer> w = nounsMap.get(nounB);
        return synsetsArr.get(sap.ancestor(v, w));
    }

    public static void main(String[] args) {
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");

        System.out.println(wn.isNoun("seagull"));
        System.out.println(wn.isNoun("luyao"));
        System.out.println(wn.distance("pigeon", "seagull"));
        System.out.println(wn.sap("pigeon", "seagull"));
    }
}
