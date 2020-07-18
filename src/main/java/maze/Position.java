package maze;

import java.util.Objects;

public class Position {
    private final int row;
    private final int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Position add(Position p) {
        return new Position(row + p.row, column + p.column);
    }

    public Position multiply(int k) {
        return new Position(row * k, column * k);
    }

    public Position middle(Position to) {
        return new Position((row + to.row) / 2, (column + to.column) / 2);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row &&
                column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
