package practice.model;


public class Edge {
    private int from;
    private int to;
    private double weight;

    public Edge(int from, int to, double weight) {
        this.from = from;
    }

    public int getFrom() {
        return from;
    }


    public int getTo() {
        return to;
    }

    public double getWeight() {
        return weight;
    }
}
