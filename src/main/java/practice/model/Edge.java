package practice.model;


public class Edge {
    private int from;
    private int to;
    private int weight;

    public Edge(int from, int to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Edge(int from, int to){
        this(from, to, 0);
    }

    public int getFrom() {
        return from;
    }


    public int getTo() {
        return to;
    }

    public int getWeight() {
        return weight;
    }
}
