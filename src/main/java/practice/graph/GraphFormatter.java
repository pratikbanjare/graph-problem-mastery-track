package practice.graph;

import practice.model.WeightedEdge;

import java.util.List;
import java.util.Objects;

public final class GraphFormatter {

    public String toDebugString(Graph graph) {
        Objects.requireNonNull(graph, "Graph must not be null");

        StringBuilder graphDescription = new StringBuilder();
        for (int i = 0; i < graph.getVertexCount(); ++i) {
            graphDescription.append(System.lineSeparator())
                    .append("For vertex ")
                    .append(i + 1)
                    .append(" neighbors are -[ ");
            List<WeightedEdge> neighbors = graph.getWeightedEdgesOfVertex(i + 1);
            neighbors.forEach(edge -> graphDescription.append("(")
                    .append(edge.getTo())
                    .append(", ")
                    .append(edge.getWeight())
                    .append(")"));
            graphDescription.append("]");
        }
        return graphDescription.toString();
    }
}
