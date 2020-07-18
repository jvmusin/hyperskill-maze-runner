package maze;

import java.io.Serializable;

public enum Cell implements Serializable {
    EMPTY(' '),
    WALL('\u2588');

    private final char representation;

    Cell(char representation) {
        this.representation = representation;
    }

    public char getRepresentation() {
        return representation;
    }
}
