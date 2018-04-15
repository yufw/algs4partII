import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BaseballElimination {
    private final int nTeams;
    private final Map<String, Integer> teams;
    private final String[] teamNames;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] games;
    private final Map<String, ArrayList<String>> elimination;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        String line = in.readLine();
        nTeams = Integer.parseInt(line);
        teams = new HashMap<>();
        teamNames = new String[nTeams];
        wins = new int[nTeams];
        losses = new int[nTeams];
        remaining = new int[nTeams];
        games = new int[nTeams][nTeams];
        elimination = new HashMap<>();

        for (int i = 0; i < nTeams; i++) {
            line = in.readLine();
            String[] fields = line.trim().split(" +");
            teams.put(fields[0], i);
            teamNames[i] = fields[0];
            wins[i] = Integer.parseInt(fields[1]);
            losses[i] = Integer.parseInt(fields[2]);
            remaining[i] = Integer.parseInt(fields[3]);
            for (int j = 0; j < nTeams; j++) {
                games[i][j] = Integer.parseInt(fields[4+j]);
            }
        }
    }

    public int numberOfTeams() {
        return nTeams;
    }

    public Iterable<String> teams() {
        return Arrays.asList(teamNames);
    }

    public int wins(String team) {
        if (!teams.containsKey(team)) {
            throw new IllegalArgumentException("invalid team");
        }
        return wins[teams.get(team)];
    }

    public int losses(String team) {
        if (!teams.containsKey(team)) {
            throw new IllegalArgumentException("invalid team");
        }
        return losses[teams.get(team)];
    }

    public int remaining(String team) {
        if (!teams.containsKey(team)) {
            throw new IllegalArgumentException("invalid team");
        }
        return remaining[teams.get(team)];
    }

    public int against(String team1, String team2) {
        if (!teams.containsKey(team1) || !teams.containsKey(team2)) {
            throw new IllegalArgumentException("invalid team");
        }
        return games[teams.get(team1)][teams.get(team2)];
    }

    public boolean isEliminated(String team) {
        if (!teams.containsKey(team)) {
            throw new IllegalArgumentException("invalid team");
        }
        if (elimination.containsKey(team)) {
            return elimination.get(team).size() > 0;
        }
        ArrayList<String> subset = new ArrayList<>();

        // trivial elimination
        int t = teams.get(team);
        for (int i = 0; i < nTeams; i++) {
            if (i == t) continue;
            if (wins[t] + remaining[t] < wins[i]) {
                subset.add(teamNames[i]);
                elimination.put(team, subset);
                return true;
            }
        }

        // nontrivial elimination
        int V = 1 + (nTeams-1)*(nTeams-2)/2 + nTeams;
        FlowNetwork network = new FlowNetwork(V);

        // team vertices to sink
        for (int i = 0; i < nTeams; i++) {
            if (i == t) continue;
            network.addEdge(new FlowEdge(i, t, wins[t]+remaining[t]-wins[i]));
        }

        int gameVertex = nTeams;
        for (int i = 0; i < nTeams; i++) {
            if (i == t) continue;
            for (int j = i+1; j < nTeams; j++) {
                if (j == t) continue;
                // source to game vertices
                network.addEdge(new FlowEdge(V-1, gameVertex, games[i][j]));
                // game vertices to team vertices
                network.addEdge(new FlowEdge(gameVertex, i, Integer.MAX_VALUE));
                network.addEdge(new FlowEdge(gameVertex, j, Integer.MAX_VALUE));
                gameVertex++;
            }
        }

        FordFulkerson ff = new FordFulkerson(network, V-1, t);

        int total = 0;
        for (int i = 0; i < nTeams; i++) {
            if (i == t) continue;
            for (int j = i+1; j < nTeams; j++) {
                if (j == t) continue;
                total += games[i][j];
            }
        }

        if (ff.value() < total) {
            for (int i = 0; i < nTeams; i++) {
                if (i == t) continue;
                if (ff.inCut(i)) {
                    subset.add(teamNames[i]);
                }
            }
            elimination.put(team, subset);
            return true;
        }

        elimination.put(team, subset);
        return false;
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (!teams.containsKey(team)) {
            throw new IllegalArgumentException("invalid team");
        }
        if (!elimination.containsKey(team)) {
            isEliminated(team);
        }
        ArrayList<String> e = elimination.get(team);
        if (e.size() > 0) {
            return e;
        }
        return null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
