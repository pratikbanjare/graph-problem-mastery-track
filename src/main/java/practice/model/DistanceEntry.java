package practice.model;

public class DistanceEntry {
    private final int vertex;
    private final int distance;

    public DistanceEntry(int vertex, int distance) {
        this.vertex = vertex;
        this.distance = distance;
    }

    public int getVertex() {
        return this.vertex;
    }

    public int getDistance() {
        return this.distance;
    }
}
