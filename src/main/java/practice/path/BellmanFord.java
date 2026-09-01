package practice.path;

import practice.exception.GraphException;
import practice.graph.Graph;
import practice.model.WeightedEdge;

import java.util.Arrays;

public class BellmanFord implements IShortestPath{


    @Override
    public int[] shortestPath(Graph graph, int source) throws GraphException {
        return bellmanFord(graph, source);
    }

    private int[] bellmanFord(Graph graph, int source) throws GraphException {
        int[] distance = new int[graph.getNumberOfVertex() + 1];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[source] = 0;
        int pass = 1;
        for (; pass < graph.getNumberOfVertex(); ++pass){
            if(!edgeRelaxation(graph, distance, false)){
                break;
            }
        }
        if (pass == graph.getNumberOfVertex()){
            edgeRelaxation(graph, distance, true);
        }
        return distance;
    }


    private boolean edgeRelaxation(Graph graph, int[] distance, boolean throwException) throws GraphException {
        boolean change = false;
        for (int  vertex = 1; vertex <= graph.getNumberOfVertex(); ++vertex){
            for (WeightedEdge weightedEdge : graph.getEdgesOfVertex(vertex)){
                int weight = weightedEdge.getWeight();
                int neighbor = weightedEdge.getTo();

                if (distance[vertex] != Integer.MAX_VALUE &&
                        distance[vertex] + weight < distance[neighbor]){
                    if (throwException){
                        throw new GraphException("Negative Cycle detected!!!!");
                    }
                    change = true;
                    distance[neighbor] = distance[vertex] + weight;
                }
            }
        }
        return change;
    }

}
