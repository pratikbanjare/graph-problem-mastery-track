package practice.sort;

import practice.exception.GraphException;
import practice.graph.Graph;
import practice.model.WeightedEdge;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TopologicalSort {

    public List<Integer> sort(Graph graph){

        List<Integer> sortedData = new LinkedList<>();
        int[] state = new int[graph.getNumberOfVertex() + 1];

        for (int vertex = 1; vertex <= graph.getNumberOfVertex(); ++vertex){
            if (state[vertex] == 0){
                sortInterval(graph, vertex, state, sortedData);
            }
        }

        Collections.reverse(sortedData);
        return sortedData;

    }

    private void sortInterval(Graph graph, int vertex, int[] state, List<Integer> sortedData) {
        state[vertex] = 1;

        for (WeightedEdge weightedEdge : graph.getWeightedEdgesOfVertex(vertex)) {

            if (state[weightedEdge.getTo()] == 1) {
                throw new GraphException("Cycle detected when performing topological sort");
            }
            if (state[weightedEdge.getTo()] == 0) {
                sortInterval(graph, weightedEdge.getTo(), state, sortedData);
            }
        }
        sortedData.add(vertex);
        state[vertex] = 2;
    }
}
