import java.util.Iterator;

public class BoggleSolver {

    private TrieSET dict;
    //private TrieSET tempDict;
    private SET<String> allWords;
    //BoggleBoard board;
    //private boolean isNotAll;
//    private double dfsPrefixTime = 0;
//
//    public double getDfsPrefixTime() {
//        return dfsPrefixTime;
//    }

    private class TrieSET implements Iterable<String> {
        private static final int R = 26;        // extended ASCII

        private Node root;      // root of trie
        //private int N;          // number of keys in trie
        //private boolean hasPre;

        // R-way trie node
        private class Node {
            private Node[] next = new Node[R];
            private boolean isString;
        }

        /**
         * Initializes an empty set of strings.
         */
        public TrieSET() {
        }

        public boolean hasPrefix(String key) {
            Node x = get(root, key, 0);
            return x != null;
        }

        /**
         * Does the set contain the given key?
         *
         * @param key the key
         * @return <tt>true</tt> if the set contains <tt>key</tt> and
         * <tt>false</tt> otherwise
         * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
         */
        public boolean contains(String key) {
            Node x = get(root, key, 0);
            return x != null && x.isString;
        }

        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = key.charAt(d);
            return get(x.next[c - 65], key, d + 1);
        }

        /**
         * Adds the key to the set if it is not already present.
         *
         * @param key the key to add
         * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
         */
        public void add(String key) {
            root = add(root, key, 0);
        }

        private Node add(Node x, String key, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                //if (!x.isString) N++;
                x.isString = true;
            } else {
                char c = key.charAt(d);
                x.next[c - 65] = add(x.next[c - 65], key, d + 1);
            }
            return x;
        }

        /**
         * Returns all of the keys in the set, as an iterator.
         * To iterate over all of the keys in a set named <tt>set</tt>, use the
         * foreach notation: <tt>for (Key key : set)</tt>.
         *
         * @return an iterator to all of the keys in the set
         */
        public Iterator<String> iterator() {
            return keysWithPrefix("").iterator();
        }

        /**
         * Returns all of the keys in the set that start with <tt>prefix</tt>.
         *
         * @param prefix the prefix
         * @return all of the keys in the set that start with <tt>prefix</tt>,
         * as an iterable
         */
        public Iterable<String> keysWithPrefix(String prefix) {
            Queue<String> results = new Queue<String>();
            Node x = get(root, prefix, 0);
            collect(x, new StringBuilder(prefix), results);
            return results;
        }

        private void collect(Node x, StringBuilder prefix, Queue<String> results) {
            if (x == null) return;
            if (x.isString) results.enqueue(prefix.toString());

            for (char c = 0; c < R; c++) {
                prefix.append(c);
                collect(x.next[c], prefix, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
    }

    private void dfs(BoggleBoard board, boolean[][] marked, int i, int j, String curStr) {
        if (i < 0 || i >= board.rows() || j < 0 || j >= board.cols() || marked[i][j])
            return;

        //boolean isNotAll = false;
        if (board.getLetter(i, j) == 'Q')
            curStr += "QU";
        else curStr += board.getLetter(i, j);
        //Stopwatch prefixStopWatch = new Stopwatch();

        //if (curStr.length() > 2) {
//            Iterable<String> curWords = dict.keysWithPrefix(curStr);
//
//            if (!curWords.iterator().hasNext()) {//////////
//                marked[i][j] = false;
//                return;
//            }

            if (!dict.hasPrefix(curStr)) {
                marked[i][j] = false;
                return;
            }


            //dfsPrefixTime += prefixStopWatch.elapsedTime();
//            for (String w : curWords) {
//                //Stopwatch sw = new Stopwatch();
//                StringBuilder word = new StringBuilder();
//                for (int k = 0; k < w.length(); k++) {
//                    if (k < curStr.length()) word.append(w.charAt(k));
//                    else word.append ((char)(w.charAt(k) + 65));
//                }
//                //dfsPrefixTime += sw.elapsedTime();
//                if (word.charAt(word.length() - 1) == 'Q')
//                    word.append('U');
//                if (!allWords.contains(word.toString()) && word.length() <= board.cols() * board.rows() * 2) {
//                    isNotAll = true;
//                    break;
//                }
//            }
    //    }


//        if (curStr.compareTo("STAIG") == 0)
//        {
//            curStr = "STAIG";
//        }

//        if (!isNotAll) {
//            return;
//        }

        if (curStr.length() > 2 && dict.contains(curStr))
            allWords.add(curStr);

        int[] offset = {1, 0, -1};
        marked[i][j] = true;

        for (int anOffset : offset) {
            for (int anOffset1 : offset) {
//                if (!isNotAll && curStr.length() > 2) {
//                    marked[i][j] = false;
//                    return;
//                }
                dfs(board, marked, i + anOffset, j + anOffset1, curStr);
            }
        }
        marked[i][j] = false;
    }

    private int getScores(int length) {
        switch (length) {
            case 0:
            case 1:
            case 2:
                return 0;
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dict = new TrieSET();

        //Stopwatch sw = new Stopwatch();
        for (String aDictionary : dictionary) {
            dict.add(aDictionary);
        }

        //StdOut.println(sw.elapsedTime());
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        allWords = new SET<String>();

        //if (board) {
            //this.board = board;
            if (!allWords.iterator().hasNext()) {
                for (int i = 0; i < board.rows(); i++) {
                    for (int j = 0; j < board.cols(); j++) {
                        boolean marked[][] = new boolean[board.rows()][board.cols()];
                        dfs(board, marked, i, j, "");
                    }
                }
            }
      //  }

        return allWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (dict.contains(word))
            return getScores(word.length());
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        Stopwatch sw = new Stopwatch();
        //int num = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
            //num++;
        }
        //StdOut.println(num);
        StdOut.println("Score = " + score);
        //StdOut.println(solver.getDfsPrefixTime());
        StdOut.print(sw.elapsedTime());

//        BoggleSolver BS;
//        In in = new In("C:\\Users\\Asus\\Desktop\\boggle\\dictionary-enable2k.txt");
//        int a = 0;
//        Stopwatch sw = new Stopwatch();
//        while (sw.elapsedTime() < 1.0) {
//            BS = new BoggleSolver(in.readAllStrings());
//            BS.getAllValidWords(new BoggleBoard());
//            a++;
//        }
//
//        StdOut.println(a);
    }
}
