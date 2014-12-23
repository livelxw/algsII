
public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();
        char[] out = new char[s.length()];
        int first = 0;

        CircularSuffixArray csa = new CircularSuffixArray(s);

        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) {
                out[i] = input[input.length - 1];
                first = i;
            } else out[i] = input[csa.index(i) - 1];
        }

        BinaryStdOut.write(first);

        for (char anOut : out) {
            BinaryStdOut.write(anOut, 8);
        }

        BinaryStdOut.close();
    }

    private static void keyIndexCount(char[] a, char[] aux, int[] next) {
        int[] count = new int[257];
        int N = a.length;

        for (char anA : a) {
            count[anA + 1]++;
        }

        for (int i = 0; i < 256; i++) {
            count[i + 1] += count[i];
        }

        for (int i = 0; i < N; i++) {
            aux[count[a[i]]] = a[i];
            next[count[a[i]]++] = i;
        }
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        int first;
        first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();

        char[] t = s.toCharArray();
        char[] sorted = s.toCharArray();
        int[] next = new int[t.length];

        for (int i = 0; i < next.length; i++) next[i] = -1;

        keyIndexCount(t, sorted, next);

        for (int i = 0; i < sorted.length; i++) {
            BinaryStdOut.write(sorted[first], 8);
            first = next[first];
        }

        BinaryStdOut.close();


    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
    }
}
