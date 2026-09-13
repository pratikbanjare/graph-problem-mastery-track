package practice.model;

public class WeightedEdge {
    private  int  to;
    private int weight;

    public WeightedEdge(int to, int weight){
        this.to = to;
        this.weight = weight;
    }

    public int getTo() {
        return to;
    }

    public int getWeight() {
        return weight;
    }
}
