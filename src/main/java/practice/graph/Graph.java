
package practice.graph;

import practice.model.Edge;
import practice.model.WeightedEdge;
import practice.model.GraphType;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Graph {

    private int vertices;
    private GraphType graphType;
    private List<List<WeightedEdge>> weightedAdjacencyList;
    private List<Edge> edges;

    public Graph(int vertices) {
        this(vertices, GraphType.UNDIRECTED);
    }

    public Graph(int vertices, GraphType graphType) {
        if (vertices <= 0) throw new IllegalArgumentException("Invalid Vertices");
        this.graphType = graphType;
        this.vertices = vertices;
        weightedAdjacencyList = new ArrayList<>();
        edges = new ArrayList<>();
        for (int i = 0; i < this.vertices; ++i) {
            this.weightedAdjacencyList.add(new ArrayList<>());
        }
    }

    public int getVertexCount() {
        return this.vertices;
    }

    public int getNumberOfVertex() {
        return this.vertices;
    }

    public List<Edge> getEdges() {
        return this.edges;
    }

    public List<List<WeightedEdge>> getWeightedAdjacencyList() {
        return this.weightedAdjacencyList;
    }

    public List<WeightedEdge> getWeightedEdgesOfVertex(int vertex) {
        if (!validateVertex(vertex)) throw new IllegalArgumentException("Invalid Vertex");
        return this.weightedAdjacencyList.get(getVertexPos(vertex));
    }

    public List<Integer> getEdgesOfVertex(int vertex) {
        if (!validateVertex(vertex)) throw new IllegalArgumentException("Invalid Vertex");
        return this.weightedAdjacencyList.get(getVertexPos(vertex)).stream().map(WeightedEdge::getTo).toList();
    }

    public GraphType getGraphType() {
        return this.graphType;
    }

    public int getVertices() {
        return this.vertices;
    }

    public boolean validateVertex(int v) {
        return (v >= 1 && v <= this.vertices);
    }

    private int getVertexPos(int v) {
        return v - 1;
    }

    public void addEdge(int u, int v) {
        this.addEdge(u, v, 0);
    }

    public void addEdge(int u, int v, int weight) {
        if (!validateVertex(u) || !validateVertex(v)) {
            throw new IllegalArgumentException("Invalid Vertex");
        }

        if (hasEdge(u, v)) {
            System.out.println("Edge already exists");
            return;
        }

        this.weightedAdjacencyList.get(getVertexPos(u)).add(new WeightedEdge(v, weight));
        this.edges.add(new Edge(u, v, weight));
        if (graphType == GraphType.UNDIRECTED) {
            this.weightedAdjacencyList.get(getVertexPos(v)).add(new WeightedEdge(u, weight));
        }
    }

    boolean hasEdge(int u, int v) {
        if (!validateVertex(u) || !validateVertex(v)) {
            throw new IllegalArgumentException("Invalid Vertex");
        }
        return weightedAdjacencyList.get(getVertexPos(u))
                .stream()
                .map(WeightedEdge::getTo)
                .anyMatch(to -> to == v);
    }

    public void removeEdge(int u, int v) {
        if (!validateVertex(u) || !validateVertex(v)) {
            throw new IllegalArgumentException("Invalid Vertex");
        }

        this.removeFromAdjacencyList(u, v);
        if(this.graphType == GraphType.UNDIRECTED) {
            this.removeFromAdjacencyList(v,u);
        }
        this.edges = this.edges.stream()
                .filter(edge -> edge.getFrom() != u && edge.getTo() != v)
                .collect(Collectors.toList());
    }

    private void removeFromAdjacencyList(int u, int v) {
        List<WeightedEdge> e1 = this.weightedAdjacencyList.get(getVertexPos(u));
        List<WeightedEdge> list = e1.stream()
                .filter(weightedEdge -> weightedEdge.getTo() != v)
                .toList();
        weightedAdjacencyList.set(getVertexPos(u), list);
    }

    void printGraph() {
        for (int i = 0; i < this.vertices; ++i) {
            System.out.println();
            System.out.print("For vertex " + i + 1 + "neighbors are -[ ");
            List<WeightedEdge> neighbors = weightedAdjacencyList.get(i);
            neighbors.forEach(j -> System.out.print("(" + j.getTo() + ", " + j.getWeight() + ")"));
        }
        System.out.print("]");
    }

    public int getVerticesCount() {
        return this.vertices;
    }

    public List<Integer> getNeighbors(int v) {
        return this.weightedAdjacencyList.get(getVertexPos(v)).stream().map(WeightedEdge::getTo).toList();
    }

    public int getPublicVertex(int v) {
        return v + 1;
    }
}