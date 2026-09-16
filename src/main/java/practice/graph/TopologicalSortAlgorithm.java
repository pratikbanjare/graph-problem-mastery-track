package practice.graph;

import practice.Exception.GraphException;
import java.util.*;

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

    public List<Integer> kahnsAlgorithm(Graph graph) throws GraphException {

        List<Integer> topologicalOrder = new ArrayList<>();;
        Queue<Integer> queue = new ArrayDeque<>();

        // Generate In-Degree for each vertex
        int [] inDegree = generateIndegreeFrom(graph);

        // Add vertex to queue with in degree == 0
        for (int i = 0; i< graph.getVertexCount(); ++i) {
            if (inDegree[i] == 0){
                queue.add(i);
            }
        }

        // Topological Sort of Graph using BFS and In-Degree
        while(!queue.isEmpty()){
            int vertex = queue.poll();
            for (Integer neighbor : graph.getEdgesOfVertex(vertex)){
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0){
                    queue.add(neighbor);
                }
            }
            topologicalOrder.add(vertex);
        }

        if (topologicalOrder.size() != graph.getVertexCount()){
            throw new GraphException("Provided graph not DAG");
        }

        List<Integer> res = new ArrayList<>();
        topologicalOrder.forEach( x -> res.add(x+1) );

        return res;
    }

    private int[] generateIndegreeFrom(Graph graph){
        int[] inDegree = new int[graph.getVertexCount()];

        for (List<Integer> edge : graph.getEdges()){
            for (Integer vertex : edge){
                inDegree[vertex]++;
            }
        }
        return inDegree;
    }
}
