import java.util.*;

public class WordNet {

    private ArrayList<String> synsetsList = new ArrayList<String>();
    private HashMap<String, Stack<Integer>> nounMap = new HashMap<String, Stack<Integer>>();
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) throws NullPointerException, IllegalArgumentException {
        if (synsets == null || hypernyms == null)
            throw new NullPointerException("constructor input null");

        In synsetsInput = new In(synsets);
        In hypernymsInput = new In(hypernyms);

        int v = 0;

        while (synsetsInput.hasNextLine()) {
            String synsetsLine = synsetsInput.readLine();
            String[] totalWord = synsetsLine.split(",");
            String[] splitWord = totalWord[1].split(" ");

            for (String aSplitWord : splitWord) {
                if (nounMap.containsKey(aSplitWord))
                    nounMap.get(aSplitWord).push(v);
                else {
                    Stack<Integer> a = new Stack<Integer>();
                    a.push(v);
                    nounMap.put(aSplitWord, a);
                }

            }
            v = v + 1;
            synsetsList.add(totalWord[1]);
        }

        Digraph digraph = new Digraph(v);

        while (hypernymsInput.hasNextLine()) {
            String hypernymsLine = hypernymsInput.readLine();
            String[] net = hypernymsLine.split(",");

            for (int i = 1; i < net.length; i++) {
                digraph.addEdge(Integer.parseInt(net[0]), Integer.parseInt(net[i]));
            }
        }

        int rootNum = 0;

        for (int i = 0; i < v; i++) {
            if (!digraph.adj(i).iterator().hasNext()) //////////////////////// return null？
                rootNum++;
        }

        if (rootNum != 1)
            throw new IllegalArgumentException("not one root");
        else {
            DirectedCycle DC = new DirectedCycle(digraph);

            if (DC.hasCycle())
                throw new IllegalArgumentException("has circle");
        }

        sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) throws NullPointerException {
        if (word == null)
            throw new NullPointerException("isNoun input null");
        return nounMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) throws NullPointerException, IllegalArgumentException {
        if (nounA == null || nounB == null)
            throw new NullPointerException("distance input null");
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("distance not a noun");

        return sap.length(nounMap.get(nounA), nounMap.get(nounB));

    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) throws NullPointerException, IllegalArgumentException {
        if (nounA == null || nounB == null)
            throw new NullPointerException("sap input null");
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("sap not a noun");

        return synsetsList.get(sap.ancestor(nounMap.get(nounA), nounMap.get(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}