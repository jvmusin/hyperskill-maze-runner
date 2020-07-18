package maze;

public class Edge {
    private final Position from;
    private final Position to;

    public Edge(Position from, Position to) {
        this.from = from;
        this.to = to;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Position middle() {
        return from.middle(to);
    }

    public Edge add(Position p) {
        return new Edge(from.add(p), to.add(p));
    }

    public Edge multiply(int k) {
        return new Edge(from.multiply(k), to.multiply(k));
    }
}
