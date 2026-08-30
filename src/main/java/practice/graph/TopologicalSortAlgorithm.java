package practice.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopologicalSortAlgorithm {

    public List<Integer> dfsTopologicalSort(Graph graph) {
        List<Integer> topologicalOrder = new ArrayList<>();
        int[] state  = new int[graph.getVertexCount()];

        for (int i = 1; i<= graph.getVertexCount(); i++){
            if (state[i-1] == 0){
                dfsTopologicalSortInternal(graph, i-1, state, topologicalOrder);
            }
        }
        Collections.reverse(topologicalOrder);

        List<Integer> res = new ArrayList<>();
        topologicalOrder.forEach( x -> res.add(x+1) );
        return res;
    }

    public void dfsTopologicalSortInternal(Graph graph, int vertex, int[] state, List<Integer> topologicalOrder){

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
