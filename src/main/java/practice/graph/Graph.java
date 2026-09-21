
package practice.graph;

import practice.exception.GraphException;
import practice.model.Edge;
import practice.model.GraphType;
import practice.model.WeightedEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class Graph {

    private static final Logger LOGGER = Logger.getLogger(Graph.class.getName());

    private final int vertices;
    private final GraphType graphType;
    private final List<List<WeightedEdge>> weightedAdjacencyList;
    private final List<Edge> edges;

    public Graph(int vertices) {
        this(vertices, GraphType.UNDIRECTED);
    }

    public Graph(int vertices, GraphType graphType) {
        if (vertices <= 0) {
            throw new GraphException("Invalid Vertices");
        }
        this.graphType = Objects.requireNonNull(graphType, "Graph type must not be null");
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
        if (!validateVertex(vertex)) {
            throw new GraphException("Invalid Vertex");
        }
        return this.weightedAdjacencyList.get(getVertexPos(vertex));
    }

    public List<Integer> getEdgesOfVertex(int vertex) {
        if (!validateVertex(vertex)) {
            throw new GraphException("Invalid Vertex");
        }
        return this.weightedAdjacencyList.get(getVertexPos(vertex)).stream().map(WeightedEdge::getTo).toList();
    }

    public int getVertices() {
        return this.vertices;
    }

    public GraphType getGraphType() {
        return this.graphType;
    }

    public boolean validateVertex(int vertex) {
        return vertex >= 1 && vertex <= this.vertices;
    }

    private int getVertexPos(int v) {
        return v - 1;
    }

    public void addEdge(int u, int v) {
        this.addEdge(u, v, 0);
    }

    public void addEdge(int u, int v, int weight) {
        if (!validateVertex(u) || !validateVertex(v)) {
            throw new GraphException("Invalid Vertex");
        }

        if (hasEdge(u, v)) {
            LOGGER.warning("Edge already exists");
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
            throw new GraphException("Invalid Vertex");
        }
        return weightedAdjacencyList.get(getVertexPos(u))
                .stream()
                .map(WeightedEdge::getTo)
                .anyMatch(to -> to == v);
    }

    public void removeEdge(int u, int v) {
        if (!validateVertex(u) || !validateVertex(v)) {
            throw new GraphException("Invalid Vertex");
        }

        this.removeFromAdjacencyList(u, v);
        if (this.graphType == GraphType.UNDIRECTED) {
            this.removeFromAdjacencyList(v, u);
        }
        this.edges.removeIf(edge -> isEdge(edge, u, v));
    }

    private boolean isEdge(Edge edge, int from, int to) {
        return (edge.getFrom() == from && edge.getTo() == to)
                || (graphType == GraphType.UNDIRECTED
                && edge.getFrom() == to && edge.getTo() == from);
    }

    private void removeFromAdjacencyList(int u, int v) {
        List<WeightedEdge> e1 = this.weightedAdjacencyList.get(getVertexPos(u));
        List<WeightedEdge> list = e1.stream()
                .filter(weightedEdge -> weightedEdge.getTo() != v)
                .toList();
        weightedAdjacencyList.set(getVertexPos(u), list);
    }

    void printGraph() {
        StringBuilder graphDescription = new StringBuilder();
        for (int i = 0; i < this.vertices; ++i) {
            graphDescription.append(System.lineSeparator())
                    .append("For vertex ")
                    .append(i + 1)
                    .append(" neighbors are -[ ");
            List<WeightedEdge> neighbors = weightedAdjacencyList.get(i);
            neighbors.forEach(edge -> graphDescription.append("(")
                    .append(edge.getTo())
                    .append(", ")
                    .append(edge.getWeight())
                    .append(")"));
        }
        graphDescription.append("]");
        LOGGER.info(graphDescription::toString);
    }

    public int getVerticesCount() {
        return this.vertices;
    }

    public List<Integer> getNeighbors(int vertex) {
        if (!validateVertex(vertex)) {
            throw new GraphException("Invalid Vertex");
        }
        return this.weightedAdjacencyList.get(getVertexPos(vertex)).stream().map(WeightedEdge::getTo).toList();
    }

    public int getPublicVertex(int vertex) {
        return vertex + 1;
    }

    public static Graph reverse(Graph graph) {

        Graph reversedGraph = new Graph(graph.getNumberOfVertex(), graph.getGraphType());

        for (Edge edge : graph.getEdges()) {
            reversedGraph.addEdge(edge.getTo(), edge.getFrom(), edge.getWeight());
        }
        return reversedGraph;
    }
}