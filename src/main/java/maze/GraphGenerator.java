package maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GraphGenerator {
    private static final Random rnd = new Random();
    private static final int[] dx = {-1, 0, 1, 0};
    private static final int[] dy = {0, 1, 0, -1};

    public List<Edge> generate(int n, int m) {
        List<Edge> result = new Generator(n, m - 2).generate();
        int rowEnter = rnd.nextInt(n);
        int rowExit = rnd.nextInt(n);
        result.add(new Edge(new Position(rowEnter, -1), new Position(rowEnter, 0)));
        result.add(new Edge(new Position(rowExit, m - 3), new Position(rowExit, m - 2)));
        Position shift = new Position(0, 1);
        return result.stream()
                .map(e -> e.add(shift))
                .collect(Collectors.toList());
    }

    private static class Generator {
        private final int n, m;

        public Generator(int n, int m) {
            this.n = n;
            this.m = m;
        }

        private boolean inside(Position p) {
            return 0 <= p.getRow() && p.getRow() < n && 0 <= p.getColumn() && p.getColumn() < m;
        }

        private Iterable<Position> neighbours(Position from) {
            return IntStream.range(0, 4)
                    .mapToObj(i -> new Position(from.getRow() + dx[i], from.getColumn() + dy[i]))
                    .filter(this::inside)
                    ::iterator;
        }

        private List<Edge> generate() {
            int[][] fieldCosts = new int[n][m];
            for (int i = 0; i < n; i++) for (int j = 0; j < m; j++) fieldCosts[i][j] = rnd.nextInt(1000);
            boolean[][] taken = new boolean[n][m];
            taken[0][0] = true;
            List<Edge> result = new ArrayList<>();
            for (int it = 0; it < n * m - 1; it++) {
                Position bestFrom = null;
                Position bestTo = null;
                int bestCost = 0;
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) {
                        if (taken[i][j]) {
                            for (Position p : neighbours(new Position(i, j))) {
                                if (!taken[p.getRow()][p.getColumn()]) {
                                    int cost = fieldCosts[i][j] + fieldCosts[p.getRow()][p.getColumn()];
                                    if (bestFrom == null || bestCost > cost) {
                                        bestFrom = new Position(i, j);
                                        bestTo = p;
                                        bestCost = cost;
                                    }
                                }
                            }
                        }
                    }
                }
                Objects.requireNonNull(bestTo);
                taken[bestTo.getRow()][bestTo.getColumn()] = true;
                result.add(new Edge(bestFrom, bestTo));
            }
            return result;
        }
    }
}
