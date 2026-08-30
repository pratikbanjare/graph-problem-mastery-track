
package practice.graph;

import practice.model.WeightedEdge;
import java.util.List;
import java.util.ArrayList;

public class Graph {

    private final int vertices;
    private List<List<WeightedEdge>> edges;

    Graph(int vertices) {
        if (vertices <= 0) throw new IllegalArgumentException("Invalid Vertices");
        this.vertices = vertices;
        edges = new ArrayList<>();
        for (int i = 0; i< this.vertices; ++i) {
            this.edges.add(new ArrayList<>());
        }
    }

    private boolean validateVertex(int v){
        return (v >= 1 && v <= this.vertices);
    }

    private int getVertexPos(int v){
        return v-1;
    }

    void addEdge(int u, int v, int weight) {
        if (!validateVertex(u) || !validateVertex(v)){
            throw new IllegalArgumentException("Invalid Vertex");
        }

        if (hasEdge(u,v)) {
            System.out.println("Edge already exists");
            return;
        }
        this.edges.get(getVertexPos(u)).add(new WeightedEdge(v, weight));

    }

    boolean hasEdge(int u, int v) {
        if (!validateVertex(u) || !validateVertex(v)) {
            throw new IllegalArgumentException("Invalid Vertex");
        }
        return edges.get(getVertexPos(u))
                .stream()
                .map(WeightedEdge::getTo)
                .anyMatch(to -> to == v);
    }

    void removeEdge(int u, int v) {
        if (validateVertex(u) || validateVertex(v)) {
            throw new IllegalArgumentException("Invalid Vertex");
        }
        List<WeightedEdge> e1 = edges.get(getVertexPos(u));
        List<WeightedEdge> list = e1.stream().filter(edge -> edge.getTo() != v)
                .toList();
        edges.set(getVertexPos(u), list);

    }

    void printGraph() {
        for (int i = 0; i< this.vertices; ++i){
            System.out.println();
            System.out.print("For vertex " + i+1 + "neighbors are -[ ");
            List<WeightedEdge> neighbors = edges.get(i);
            neighbors.forEach( j -> System.out.print("("+ j.getTo() + ", "+ j.getWeight() + ")"));
        }
        System.out.print("]");
    }
}