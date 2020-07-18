package maze;

public class MazeGenerator {
    public Maze generate(int height, int width) {
        Maze maze = new Maze(height, width, Cell.WALL);
        new GraphGenerator().generate((height - 1) / 2, (width + 1) / 2).stream()
                .map(e -> e.multiply(2))
                .map(e -> e.add(new Position(1, 0)))
                .forEach(e -> {
                    maze.set(e.getFrom(), Cell.EMPTY);
                    maze.set(e.getTo(), Cell.EMPTY);
                    maze.set(e.middle(), Cell.EMPTY);
                });
        if (height % 2 == 0) {
            for (int i = 0; i < width - 1; i++) {
                maze.set(
                        new Position(height - 2, i),
                        maze.get(new Position(height - 3, i))
                );
            }
        }
        if (width % 2 == 0) {
            int initialEntryHeight = -1;
            for (int i = 1; i < height - 1; i++) {
                if (maze.get(new Position(i, width - 2)) == Cell.EMPTY) {
                    initialEntryHeight = i;
                }
                maze.set(
                        new Position(i, width - 2),
                        Cell.EMPTY
                );
            }
            maze.set(new Position(initialEntryHeight, width - 1), Cell.EMPTY);
        }
        return maze;
    }
}
