public class Outcast {

    private WordNet wordNet;

    public Outcast(WordNet wordnet)         // constructor takes a WordNet object
    {
        this.wordNet = wordnet;
    }

    public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
    {
        int length = -1;
        int noun = -1;

        for (int i = 0; i < nouns.length; i++) {
            int curLength = 0;


            //可将 i -> j 算出来的保存，在以后的 j -> i 使用，减少时间
            for (int j = 0; j < nouns.length; j++) {
                if (i == j)
                    continue;
                curLength += wordNet.distance(nouns[i], nouns[j]);
            }

            if (curLength > length) {
                length = curLength;
                noun = i;
            }

        }

        return nouns[noun];
    }

    public static void main(String[] args)  // see test client below
    {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}