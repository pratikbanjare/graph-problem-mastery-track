
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

    private int vertices;
    private GraphType graphType;
    private List<List<WeightedEdge>> weightedAdjacencyList;
    private List<Edge> edges;

    public Graph(int vertices) {
        this(vertices, GraphType.UNDIRECTED);
    }

    public Graph(int vertices, GraphType graphType) {
        // write your code here
    }

    public int getVertexCount() {
        return this.vertices;
    }

    public List<Edge> getEdges() {
        return List.copyOf(this.edges);
    }

    public List<List<WeightedEdge>> getWeightedAdjacencyList() {
        // write your code here
        return null;
    }

    public List<WeightedEdge> getWeightedEdgesOfVertex(int vertex) {
        // write your code here
        return null;
    }

    public List<Integer> getEdgesOfVertex(int vertex) {
        // write your code here
        return null;
    }


    public GraphType getGraphType() {
        return this.graphType;
    }

    public void addEdge(int u, int v) {
        this.addEdge(u, v, 0);
    }

    public void addEdge(int u, int v, int weight) {
        // write your code here
    }

    public boolean hasEdge(int u, int v) {
        // write your code here
        return false;
    }

    public void removeEdge(int v1, int v2) {
        // write your code here
    }

}