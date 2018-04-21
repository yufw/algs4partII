public class TrieSET {
    private static final int R = 26; // 'A'-'Z'
    private Node root;               // root of trie

    // R-way trie node
    private static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
    }

    public TrieSET() {
    }

    public void add(String key) {
        if (root == null) {
            root = new Node();
        }
        Node x = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (x.next[c-'A'] == null) {
                x.next[c-'A'] = new Node();
            }
            x = x.next[c-'A'];
        }
        x.isString = true;
    }

    public boolean contains(String key) {
        Node x = root;
        for (int i = 0; i < key.length(); i++) {
            if (x == null) {
                return false;
            }
            char c = key.charAt(i);
            x = x.next[c-'A'];
        }
        return x != null && x.isString;
    }

    public boolean hasPrefix(String prefix) {
        Node x = root;
        for (int i = 0; i < prefix.length(); i++) {
            if (x == null) {
                return false;
            }
            char c = prefix.charAt(i);
            x = x.next[c-'A'];
        }
        return x != null;
    }

    public static void main(String[] args) {
        TrieSET set = new TrieSET();

        set.add("SEA");
        set.add("SHE");
        set.add("SELLS");
        set.add("SHELLS");

        System.out.println(set.contains("HELLO"));
        System.out.println(set.contains("SHELLS"));
        System.out.println(set.hasPrefix("SHELLS"));
        System.out.println(set.contains("SHELL"));
        System.out.println(set.hasPrefix("SHELL"));
        System.out.println(set.contains(""));
        System.out.println(set.hasPrefix(""));
    }
}
