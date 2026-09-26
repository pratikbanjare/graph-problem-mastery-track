
package practice.graph;

import practice.exception.GraphException;
import practice.model.Edge;
import practice.model.GraphType;
import practice.model.WeightedEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Graph {

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

    public List<Edge> getEdges() {
        return List.copyOf(this.edges);
    }

    public List<List<WeightedEdge>> getWeightedAdjacencyList() {
        return this.weightedAdjacencyList.stream()
                .map(List::copyOf)
                .toList();
    }

    public List<WeightedEdge> getWeightedEdgesOfVertex(int vertex) {
        validateVertexOrThrow(vertex);
        return List.copyOf(this.weightedAdjacencyList.get(getVertexPos(vertex)));
    }

    public List<Integer> getEdgesOfVertex(int vertex) {
        validateVertexOrThrow(vertex);
        return this.weightedAdjacencyList.get(getVertexPos(vertex)).stream().map(WeightedEdge::getTo).toList();
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
        validateVertexOrThrow(u);
        validateVertexOrThrow(v);

        if (hasEdge(u, v)) {
            throw new GraphException("Edge already exists between " + u + " and " + v);
        }

        this.weightedAdjacencyList.get(getVertexPos(u)).add(new WeightedEdge(v, weight));
        this.edges.add(new Edge(u, v, weight));
        if (graphType == GraphType.UNDIRECTED) {
            this.weightedAdjacencyList.get(getVertexPos(v)).add(new WeightedEdge(u, weight));
        }
    }

    boolean hasEdge(int u, int v) {
        validateVertexOrThrow(u);
        validateVertexOrThrow(v);
        return weightedAdjacencyList.get(getVertexPos(u))
                .stream()
                .map(WeightedEdge::getTo)
                .anyMatch(to -> to == v);
    }

    public void removeEdge(int v1, int v2) {
        validateVertexOrThrow(v1);
        validateVertexOrThrow(v2);
        WeightedEdge forwardEdge = findEdge(v1, v2);
        if (forwardEdge == null) {
            throw new GraphException("Edge does not exist between " + v1 + " and " + v2);
        }
        WeightedEdge reverseEdge = null;
        if (this.graphType == GraphType.UNDIRECTED) {
            reverseEdge = findEdge(v2, v1);
            if (reverseEdge == null) {
                throw new GraphException("Edge does not exist between " + v1 + " and " + v2);
            }
        }

        this.weightedAdjacencyList.get(getVertexPos(v1)).remove(forwardEdge);
        if (this.graphType == GraphType.UNDIRECTED) {
            this.weightedAdjacencyList.get(getVertexPos(v2)).remove(reverseEdge);
        }
        this.edges.removeIf(edge -> isEdge(edge, v1, v2));
    }

    private boolean isEdge(Edge edge, int from, int to) {
        return (edge.getFrom() == from && edge.getTo() == to)
                || (graphType == GraphType.UNDIRECTED
                && edge.getFrom() == to && edge.getTo() == from);
    }

    private WeightedEdge findEdge(int from, int to) {
        return this.weightedAdjacencyList.get(getVertexPos(from))
                .stream()
                .filter(edge -> edge.getTo() == to)
                .findFirst()
                .orElse(null);
    }

    private void validateVertexOrThrow(int vertex) {
        if (!validateVertex(vertex)) {
            throw new GraphException("Vertex must be between 1 and "
                    + this.vertices + ": " + vertex);
        }
    }

}