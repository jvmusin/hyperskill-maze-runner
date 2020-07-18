package maze;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class PathFinder {

    public List<Position> find(Maze maze) {
        return new Finder(maze).findPath();
    }

    private static class Finder {
        final Maze maze;
        Map<Position, Position> parent = new HashMap<>();

        private Finder(Maze maze) {
            this.maze = maze;
        }

        List<Position> findEntrance() {
            List<Position> result = new ArrayList<>();
            for (int side : new int[]{0, maze.getWidth() - 1}) {
                for (int i = 0; i < maze.getHeight(); i++) {
                    Position p = new Position(i, side);
                    if (maze.get(p) == Cell.EMPTY) {
                        result.add(p);
                    }
                }
            }
            return result;
        }

        List<Position> findPath() {
            List<Position> entrances = findEntrance();
            Position start = entrances.get(0);
            Position end = entrances.get(1);
            Queue<Position> q = new ArrayDeque<>();
            q.add(start);
            while (!q.isEmpty()) {
                Position cur = q.poll();
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (Math.abs(dx) + Math.abs(dy) == 1) {
                            Position to = new Position(cur.getRow() + dx, cur.getColumn() + dy);
                            if (maze.contains(to) && maze.get(to) == Cell.EMPTY && !parent.containsKey(to) && !start.equals(to)) {
                                parent.put(to, cur);
                                q.add(to);
                            }
                        }
                    }
                }
            }
            return Stream.iterate(end, Objects::nonNull, p -> parent.get(p)).collect(toList());
        }
    }
}
