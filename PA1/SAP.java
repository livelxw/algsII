import java.util.HashMap;

public class SAP {
    private Digraph DG;
    private char[] marked;
    //private int[] edgeTo;
    private int curAncestor = -1;
    private int minAncestor = -1;
    private int curLength = 10000000;
    private int[] distTo;      // distTo[v] = length of shortest s->v path

    private void bfs(Digraph G, int s, char flag) {
        Queue<Integer> q = new Queue<Integer>();

        if (marked[s] == 0) {
            marked[s] = flag;
        } else {
            marked[s] = 3;
            curLength = distTo[s];
            curAncestor = s;
        }

        distTo[s] = 0;
        q.enqueue(s);
        while (!q.isEmpty()) {
            int v = q.dequeue();


            for (int w : G.adj(v)) {
                if (marked[w] == 0) {
                    //edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = flag;
                    q.enqueue(w);
                } else if (marked[w] != flag && marked[w] != 3) {
                    if (distTo[w] + distTo[v] + 1 < curLength) {
                        curLength = distTo[w] + distTo[v] + 1;
                        curAncestor = w;
                    }
                    distTo[w] = distTo[v] + 1;
                    marked[w] = 3;
                    q.enqueue(w);
                }
                if (distTo[w] > curLength)
                    return;
            }
        }
    }

    private void dbfs(Digraph G, int s, int t) {
        Queue<Integer> q = new Queue<Integer>();
        Queue<Integer> r = new Queue<Integer>();
        marked[s] = 1;
        marked[t] = 2;
        distTo[s] = 0;
        distTo[t] = 0;
        boolean distFlag1 = false;
        boolean distFlag2 = false;

        if (s == t) {
            curAncestor = s;
            curLength = 0;
            return;
        }
        HashMap<Integer, Integer> backupOfs = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> backupOft = new HashMap<Integer, Integer>();
        q.enqueue(s);
        r.enqueue(t);
        while (!q.isEmpty() || !r.isEmpty()) {

            if (distFlag1 && distFlag2)
                return;

            if (!q.isEmpty()) {
                int v = q.dequeue();

                for (int w : G.adj(v)) {
                    if (marked[w] == 0) {
                        //edgeTo[w] = v;
                        if (marked[v] == 3)
                            distTo[w] = backupOfs.get(v) + 1;
                        else distTo[w] = distTo[v] + 1;
                        marked[w] = 1;
                        q.enqueue(w);
                    } else if (marked[w] == 2) {
                        if (marked[v] == 3) {
                            if (backupOfs.get(v) + distTo[w] + 1 < curLength) {
                                curAncestor = w;
                                curLength = backupOfs.get(v) + distTo[w] + 1;
                            }
                            if (backupOfs.get(v) > curLength)
                                distFlag1 = true;
                        } else {
                            if (distTo[v] + distTo[w] + 1 < curLength) {
                                curAncestor = w;
                                curLength = distTo[v] + distTo[w] + 1;
                            }
                            if (distTo[v] > curLength)
                                distFlag1 = true;
                        }
                        backupOft.put(w, distTo[w]);
                        distTo[w] = distTo[v] + 1;
                        marked[w] = 4;
                        q.enqueue(w);
                    }

                }

            }

            if (!r.isEmpty()) {
                int v = r.dequeue();

                for (int w : G.adj(v)) {
                    if (marked[w] == 0) {
                        //edgeTo[w] = v;
                        if (marked[v] == 4)
                            distTo[w] = backupOft.get(v) + 1;
                        else distTo[w] = distTo[v] + 1;
                        marked[w] = 2;
                        r.enqueue(w);
                    } else if (marked[w] == 1) {
                        if (marked[v] == 4) {
                            if (backupOft.get(v) + distTo[w] + 1 < curLength) {
                                curLength = backupOft.get(v) + distTo[w] + 1;
                                curAncestor = w;
                            }
                            if (backupOft.get(v) > curLength)
                                distFlag2 = true;
                        } else {
                            if (distTo[v] + distTo[w] + 1 < curLength) {
                                curLength = distTo[v] + distTo[w] + 1;
                                curAncestor = w;
                            }
                            if (distTo[v] > curLength)
                                distFlag2 = true;
                        }
                        backupOfs.put(w, distTo[w]);
                        distTo[w] = distTo[v] + 1;
                        marked[w] = 3;
                        r.enqueue(w);
                    }

                }
            }
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) throws NullPointerException {
        if (G == null)
            throw new NullPointerException("constructor null");
        DG = new Digraph(G);
        marked = new char[DG.V()];
        distTo = new int[DG.V()];
        //edgeTo = new int [G.V()];
    }

    // length of shortest ancestral path between v and w; -1 if no such path

    public int length(int v, int w) throws IndexOutOfBoundsException {
        if (v < 0 || v > DG.V() - 1 || w < 0 || w > DG.V() - 1)
            throw new IndexOutOfBoundsException("out of index");

        curLength = 10000000;
        curAncestor = -1;
        for (int i = 0; i < marked.length; i++) {
            marked[i] = 0;
        }
        for (int i = 0; i < DG.V(); i++) distTo[i] = 10000000;

        dbfs(DG, v, w);

        return (curLength != 10000000) ? curLength : -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) throws IndexOutOfBoundsException {
        if (v < 0 || v > DG.V() - 1 || w < 0 || w > DG.V() - 1)
            throw new IndexOutOfBoundsException("out of index");

        curAncestor = -1;
        curLength = 10000000;
        for (int i = 0; i < marked.length; i++) {
            marked[i] = 0;
        }
        for (int i = 0; i < DG.V(); i++) distTo[i] = 10000000;

        dbfs(DG, v, w);

        return curAncestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) throws NullPointerException {
        if (v == null || w == null)
            throw new NullPointerException("length null");

        int minLength = 10000000;
        int[] curDistTo = new int[distTo.length];



        for (int vs : v) {

            for (int i = 0; i < marked.length; i++) marked[i] = 0;
            for (int i = 0; i < distTo.length; i++) distTo[i] = 10000000;

            curLength = 10000000;

            bfs(DG, vs, (char) 1);
            System.arraycopy(distTo, 0, curDistTo, 0, distTo.length);
            for (int ws : w) {
                if (vs == ws)
                {
                    if (0 < minLength) {
                        minLength = 0;
                        minAncestor = vs;
                        continue;
                    }
                }
                bfs(DG, ws, (char) 2);

                for (int i = 0; i < marked.length; i++) {
                    if (marked[i] == 2) {
                        marked[i] = 0;
                        distTo[i] = 10000000;
                    } else if (marked[i] == 3) {
                        marked[i] = 1;
                        distTo[i] = curDistTo[i];
                    }
                }

                if (curLength < minLength) {
                    minLength = curLength;
                    minAncestor = curAncestor;
                }
            }
        }

        return minLength == 10000000 ? -1 : minLength;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) throws NullPointerException {
        if (v == null || w == null)
            throw new NullPointerException("length null");

        curAncestor = -1;
        minAncestor = -1;
        length(v, w);

        return minAncestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}