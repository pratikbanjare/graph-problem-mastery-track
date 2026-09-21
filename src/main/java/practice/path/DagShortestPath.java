package practice.path;

import practice.graph.Graph;
import practice.model.WeightedEdge;
import practice.sort.TopologicalSort;

import java.util.Arrays;
import java.util.List;

public class DagShortestPath implements IShortestPath {
    @Override
    public int[] shortestPath(Graph graph, int source){
        return this.dagShortestPath(graph,source);
    }

    private int[] dagShortestPath(Graph graph, int source){

        TopologicalSort topologicalSort = new TopologicalSort();
        List<Integer> sortedVertex = topologicalSort.sort(graph);

        int[] distance = new int[graph.getVertexCount()+1];
        Arrays.fill(distance,Integer.MAX_VALUE);
        distance[source] = 0;

        for (int vertex : sortedVertex){
            for (WeightedEdge weightedEdge : graph.getWeightedEdgesOfVertex(vertex)){
                int neighbor =  weightedEdge.getTo();
                int weight = weightedEdge.getWeight();

                if (distance[vertex] != Integer.MAX_VALUE &&
                        distance[vertex] + weight < distance[neighbor]){
                    distance[neighbor] = distance[vertex] + weight;
                }
            }
        }
        return distance;

    }
}
