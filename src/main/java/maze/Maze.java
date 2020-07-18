package maze;

import java.io.Serializable;
import java.util.Arrays;

public class Maze implements Serializable {
    private final int height, width;
    private final Cell[][] field;

    public Maze(int height, int width, Cell defaultCell) {
        this.height = height;
        this.width = width;
        this.field = new Cell[height][width];
        for (Cell[] row : field) Arrays.fill(row, defaultCell);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Cell get(Position p) {
        return field[p.getRow()][p.getColumn()];
    }

    public void set(Position p, Cell cell) {
        field[p.getRow()][p.getColumn()] = cell;
    }

    public boolean contains(Position to) {
        return 0 <= to.getRow() && to.getRow() < height && 0 <= to.getColumn() && to.getColumn() < width;
    }
}
