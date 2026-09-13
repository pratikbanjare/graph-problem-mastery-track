package practice.path;

import practice.exception.GraphException;
import practice.graph.Graph;
import practice.model.WeightedEdge;

import java.util.Arrays;

public class FlyodWarshall {

    public int[][] flyodWarshallAlgorithm(Graph graph) throws GraphException {
        int[][] distance = initializeDistance(graph);
        int vertexCount = graph.getNumberOfVertex();
        for (int k = 1; k <= vertexCount; ++k){
            for (int i = 1; i <= vertexCount; ++i){
                for (int j = 1; j <=vertexCount; ++j) {
                    if (distance[i][k] != Integer.MAX_VALUE && distance[k][j] != Integer.MAX_VALUE ) {
                        distance[i][j] = Math.min(
                                distance[i][j],
                                distance[i][k] + distance[k][j]
                        );
                    }
                }
            }
        }

        for (int vertex = 1; vertex<=vertexCount; ++vertex){
            if (distance[vertex][vertex] < 0){
                throw new GraphException("Negative Cycle exist for vertex " + vertex);
            }
        }

        return distance;
    }

    private int[][] initializeDistance(Graph graph) {
        int[][] distance = new int[1 + graph.getNumberOfVertex()][1 + graph.getNumberOfVertex()];

        for (int i = 0; i<= graph.getNumberOfVertex(); ++i){
            Arrays.fill(distance[i], Integer.MAX_VALUE);
            distance[i][i] = 0;
        }

        for (int vertex = 1; vertex<= graph.getNumberOfVertex(); ++vertex){
            for (WeightedEdge weightedEdge : graph.getEdgesOfVertex(vertex)){
                distance[vertex][weightedEdge.getTo()] = Math.min(
                        distance[vertex][weightedEdge.getTo()],
                        weightedEdge.getWeight()
                );
            }
        }
        return distance;
    }
}
