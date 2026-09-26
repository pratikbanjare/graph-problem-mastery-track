package practice.graph;

import practice.exception.GraphException;
import practice.model.Edge;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class KahnsAlgorithm {

    public List<Integer> kahnsAlgorithm(Graph graph) throws GraphException {

        List<Integer> topologicalOrder = new ArrayList<>();;
        Queue<Integer> queue = new ArrayDeque<>();

        // Generate In-Degree for each vertex
        int [] inDegree = generateIndegreeFrom(graph);

        // Add vertex to queue with in degree == 0
        for (int i = 1; i<= graph.getVertexCount(); ++i) {
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
        res.addAll(topologicalOrder);

        return res;
    }

    private int[] generateIndegreeFrom(Graph graph){
        int[] inDegree = new int[graph.getVertexCount()+1];

        for (Edge edge : graph.getEdges()){
            inDegree[edge.getTo()]++;
        }
        return inDegree;
    }
}
