package practice.graph;

import java.util.*;

public class TopologicalSortAlgorithm {

    public List<Integer> dfsTopologicalSort(Graph graph) {
        List<Integer> topologicalOrder = new ArrayList<>();
        int[] state  = new int[graph.getVertexCount()+1];

        for (int i = 1; i<= graph.getVertexCount(); i++){
            if (state[i] == 0){
                dfsTopologicalSortInternal(graph, i, state, topologicalOrder);
            }
        }
        Collections.reverse(topologicalOrder);

        List<Integer> res = new ArrayList<>();
        topologicalOrder.forEach( x -> res.add(x) );
        return res;
    }

    private void dfsTopologicalSortInternal(Graph graph, int vertex, int[] state, List<Integer> topologicalOrder){

        state[vertex] = 1;

        for(int neighbor : graph.getEdgesOfVertex(vertex)){
            if (state[neighbor] == 1){
                throw new IllegalArgumentException("Cycle detected in Graph. Topological Ordering not possible.");
            }
            if (state[neighbor] == 0){
                dfsTopologicalSortInternal(graph, neighbor, state, topologicalOrder);
            }
        }
        topologicalOrder.add(vertex);
        state[vertex] = 2;
    }

}
