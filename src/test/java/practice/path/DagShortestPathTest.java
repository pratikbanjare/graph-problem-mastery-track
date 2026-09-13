package practice.path;

import org.junit.jupiter.api.Test;
import practice.graph.Graph;

public class DagShortestPathTest {

    @Test
    public void testDagShortestPath() {

        Graph graph = new Graph(4);

        graph.addEdge(1,2,5);
        graph.addEdge(2,4,-3);
        graph.addEdge(1,3,2);
        graph.addEdge(3,4,4);

        DagShortestPath algo = new DagShortestPath();

        int[] distance = algo.shortestPath(graph, 1);

        for (int i = 0; i < distance.length; i++) {
            System.out.print(distance[i] + " -> ");
        }


    }
}
