public class BaseballElimination {
    private String[] team;
    private int num;
    private int vnum = 2;
    private int[] w, l, r;
    private int[][] g;
    private boolean[] flag;
    private Queue<String>[] qs;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        num = in.readInt();
        team = new String[num];
        w = new int[num];
        l = new int[num];
        r = new int[num];
        g = new int[num][num];
        flag = new boolean[num];
        qs = (Queue<String>[]) new Queue[num];

        int max = -1;
        int maxIndex = -1;

        for (int i = 0; i <= num; i++) {
            vnum += i;
        }

        for (int i = 0; i < num; i++) {
            team[i] = in.readString();
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();

            if (max < w[i]) {
                max = w[i];
                maxIndex = i;
            }

            for (int j = 0; j < num; j++) {
//                if (i == j) {
//                    g[i][j] = 0;
//                    in.readInt();
//                    continue;
//                }
                g[i][j] = in.readInt();
            }
        }

        for (int h = 0; h < num; h++) {
            FlowNetwork fn = new FlowNetwork(vnum);
            if (max > r[h] + w[h]) {
                flag[h] = true;
                qs[h] = new Queue<String>();
                qs[h].enqueue(this.team[maxIndex]);
            } else {

                int index = 2 + num;

                for (int j = 0; j < num; j++) {
                    if (j == h)
                        continue;
                    for (int k = j + 1; k < num; k++) {
                        if (k == h)
                            continue;
                        fn.addEdge(new FlowEdge(0, index, g[j][k]));
                        fn.addEdge(new FlowEdge(index, j + 2, Double.POSITIVE_INFINITY));
                        fn.addEdge(new FlowEdge(index, k + 2, Double.POSITIVE_INFINITY));
                        index += 1;
                    }
                }

                for (int i = 0; i < num; i++) {
                    if (i == h)
                        continue;
                    fn.addEdge(new FlowEdge(i + 2, 1,w[h] + r[h] - w[i]));
                }

                FordFulkerson ff = new FordFulkerson(fn, 0, 1);
                
                boolean rnum = false;
                for (int i = 0; i < num; i++) {
                    if (ff.inCut(i + 2)) {
                        rnum = true;
                        break;
                    }
                }

                if (rnum && max + ff.value() > w[h] + r[h]) {
                    qs[h] = new Queue<String>();
                    flag[h] = true;
                    for (int j = 0; j < num; j++) {
                        if (ff.inCut(j + 2))
                            qs[h].enqueue(this.team[j]);
                    }
                }
            }
        }
    }

    public Iterable<String> teams() {
        Queue<String> q = new Queue<String>();
        for (int i = 0; i < num; i++) {
            q.enqueue(team[i]);
        }

        return q;
    }

    public int numberOfTeams() {
        return num;
    }

    public int wins(String team) throws IllegalArgumentException {
        for (int i = 0; i < num; i++) {
            if (team.compareTo(this.team[i]) == 0)
                return w[i];
        }
        throw new IllegalArgumentException("wins");
    }

    public int losses(String team) throws IllegalArgumentException {
        for (int i = 0; i < num; i++) {
            if (team.compareTo(this.team[i]) == 0)
                return l[i];
        }
        throw new IllegalArgumentException("losses");
    }

    public int remaining(String team) throws IllegalArgumentException {
        for (int i = 0; i < num; i++) {
            if (team.compareTo(this.team[i]) == 0)
                return r[i];
        }
        throw new IllegalArgumentException("losses");
    }

    public int against(String team1, String team2) throws IllegalArgumentException {
        int a = -1, b = -1;

        for (int i = 0; i < num; i++) {

            if (a == -1 && team1.compareTo(team[i]) == 0)
                a = i;

            if (b == -1 && team2.compareTo(team[i]) == 0)
                b = i;

            if (a != -1 && b != -1)
                break;
        }
        if (a == -1 || b == -1)
            throw new IllegalArgumentException("against");

        return g[a][b];
    }

    public boolean isEliminated(String team) throws IllegalArgumentException {
        int curTeam = -1;

        for (int i = 0; i < num; i++) {
            if (this.team[i].compareTo(team) == 0) {
                curTeam = i;
                break;
            }
        }

        if (curTeam == -1)
            throw new IllegalArgumentException("isEliminated");

        return flag[curTeam];
    }

    public Iterable<String> certificateOfElimination(String team) throws IllegalArgumentException {
        int curTeam = -1;

        for (int i = 0; i < num; i++) {
            if (this.team[i].compareTo(team) == 0) {
                curTeam = i;
                break;
            }
        }
        if (curTeam == -1)
            throw new IllegalArgumentException("isEliminated");

        if (flag[curTeam])
            return qs[curTeam];
        return null;
    }

    public static void main(String[] args) {
        Stopwatch sw =new Stopwatch();
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
        StdOut.print(sw.elapsedTime());
    }
}
