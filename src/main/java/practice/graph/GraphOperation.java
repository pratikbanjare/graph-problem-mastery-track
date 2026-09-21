package practice.graph;

import practice.model.Edge;

import java.util.Objects;

public final class GraphOperation {

    private GraphOperation() {
    }

    public static Graph reverse(Graph graph) {
        Objects.requireNonNull(graph, "Graph must not be null");

        Graph reversedGraph = new Graph(graph.getVertexCount(), graph.getGraphType());
        for (Edge edge : graph.getEdges()) {
            reversedGraph.addEdge(edge.getTo(), edge.getFrom(), edge.getWeight());
        }
        return reversedGraph;
    }
}
