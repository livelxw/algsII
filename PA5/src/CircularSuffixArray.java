public class CircularSuffixArray {

    private char[] text;
    private int N;
    private int[] index;

    // return the dth character of s, -1 if d = length of s
    private int charAt(int s, int d) {
        if (d == N) return -1;

        return s + d > N - 1 ? text[s + d - N] : text[s + d];
    }


    // 3-way string quicksort a[lo..hi] starting at dth character
    private void sort(int lo, int hi, int d) {
        if (hi <= lo) return;

        int lt = lo, gt = hi;
        int v = charAt(index[lo], d);
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(index[i], d);
            if (t < v) exch(lt++, i++);
            else if (t > v) exch(i, gt--);
            else i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
        sort(lo, lt - 1, d);
        if (v >= 0) sort(lt, gt, d + 1);
        sort(gt + 1, hi, d);
    }


    // exchange a[i] and a[j]
    private void exch(int i, int j) {
        int temp = index[i];
        index[i] = index[j];
        index[j] = temp;
    }

    public CircularSuffixArray(String s) throws NullPointerException// circular suffix array of s
    {
        if (s == null) throw new NullPointerException("constructor s == null");

        N = s.length();
        index = new int[s.length()];
        text = s.toCharArray();
        for (int i = 0; i < N; i++)
            index[i] = i;

        sort(0, N - 1, 0);
    }

    public int length()                   // length of s
    {
        return N;
    }

    public int index(int i) throws IndexOutOfBoundsException  // returns index of ith sorted suffix
    {
        if (i < 0 || i > text.length)
            throw new IndexOutOfBoundsException("index i < 0 or i > N - 1");

        return index[i];
    }

    public static void main(String[] args)// unit testing of the methods (optional)
    {
        In in = new In(args[0]);
        String st = in.readAll();
        CircularSuffixArray csa = new CircularSuffixArray(st);
        for (int i = 0; i < csa.length(); i++) {
            StdOut.print(csa.index(i) + " ");
        }

        StdOut.println();

        for (int i = 0; i < csa.length(); i++) {
            StdOut.print(st.charAt(csa.index(i)) + " ");
        }
        StdOut.println();
    }
}
