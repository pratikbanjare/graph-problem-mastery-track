
package practice.graph;

import practice.model.Edge;
import practice.model.GraphType;
import practice.model.WeightedEdge;
import java.util.List;
import java.util.ArrayList;

public class Graph {

    private final int vertices;
    private List<List<WeightedEdge>> adjacencyEdges;
    private List<Edge> edges;

    private final GraphType graphType;

    public Graph(int vertices) {
        this(vertices, GraphType.DIRECTED);
    }

    public Graph(int vertices, GraphType graphType) {
        if (vertices <= 0) throw new IllegalArgumentException("Invalid Vertices");
        this.graphType = graphType;
        this.vertices = vertices;
        adjacencyEdges = new ArrayList<>();
        edges = new ArrayList<>();
        for (int i = 0; i< this.vertices; ++i) {
            this.adjacencyEdges.add(new ArrayList<>());
        }
    }

    public int getNumberOfVertex() {
        return this.vertices;
    }

    public List<List<WeightedEdge>> getAdjacencyEdges() {
        return this.adjacencyEdges;
    }

    public List<Edge> getEdges() {
        return new ArrayList<>(this.edges);
    }

    public GraphType getGraphType() {
        return this.graphType;
    }

    public List<WeightedEdge> getAdjacencyListOfVertex(int vertex) {
        if (!validateVertex(vertex)) throw new IllegalArgumentException("Invalid Vertex");
        return this.adjacencyEdges.get(getVertexPos(vertex));
    }

    private boolean validateVertex(int v){
        return (v >= 1 && v <= this.vertices);
    }

    private int getVertexPos(int v){
        return v-1;
    }

    public void addEdge(int u, int v){
        this.addEdge(u,v,0);
    }
    public void addEdge(int u, int v, int weight) {

        if (hasEdge(u,v)) {
            System.out.println("Edge already exists");
            return;
        }
        if (this.graphType == GraphType.DIRECTED) {
            this.adjacencyEdges.get(getVertexPos(u)).add(new WeightedEdge(v, weight));
            this.edges.add(new Edge(u, v, weight));
        } else {
            this.adjacencyEdges.get(getVertexPos(u)).add(new WeightedEdge(v, weight));
            this.adjacencyEdges.get(getVertexPos(v)).add(new WeightedEdge(u, weight));
            this.edges.add(new Edge(u, v, weight));
        }
    }

    boolean hasEdge(int u, int v) {
        if (!validateVertex(u) || !validateVertex(v)) {
            throw new IllegalArgumentException("Invalid Vertex");
        }
        return adjacencyEdges.get(getVertexPos(u))
                .stream()
                .map(WeightedEdge::getTo)
                .anyMatch(to -> to == v);
    }

    void removeEdge(int u, int v) {
        if (!validateVertex(u) || !validateVertex(v)) {
            throw new IllegalArgumentException("Invalid Vertex");
        }
        List<WeightedEdge> e1 = adjacencyEdges.get(getVertexPos(u));
        List<WeightedEdge> list = new ArrayList<>(e1.stream()
                .filter(edge -> edge.getTo() != v)
                .toList());
        adjacencyEdges.set(getVertexPos(u), list);

        this.edges.removeIf(edge -> edge.getFrom() == u && edge.getTo() == v);

        if (this.graphType != GraphType.DIRECTED) {
            List<WeightedEdge> reverseEdges = adjacencyEdges.get(getVertexPos(v));
            List<WeightedEdge> reverseList = new ArrayList<>(reverseEdges.stream()
                    .filter(edge -> edge.getTo() != u)
                    .toList());
            adjacencyEdges.set(getVertexPos(v), reverseList);
            this.edges.removeIf(edge -> edge.getFrom() == v && edge.getTo() == u);
        }

    }

    void printGraph() {
        for (int i = 0; i< this.vertices; ++i){
            System.out.println();
            System.out.print("For vertex " + i+1 + "neighbors are -[ ");
            List<WeightedEdge> neighbors = adjacencyEdges.get(i);
            neighbors.forEach( j -> System.out.print("("+ j.getTo() + ", "+ j.getWeight() + ")"));
        }
        System.out.print("]");
    }

    public static Graph reverse(Graph graph) {

        Graph reversedgraph = new Graph(graph.getNumberOfVertex(),  graph.getGraphType());

        for (Edge edge : graph.getEdges()){
            reversedgraph.addEdge(edge.getTo(), edge.getFrom(), edge.getWeight());
        }
        return reversedgraph;
    }
}