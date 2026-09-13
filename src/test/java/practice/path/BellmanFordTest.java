package practice.path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import practice.exception.GraphException;
import practice.graph.Graph;

public class BellmanFordTest {

    @Test
    public void testBellmanFord(){
        IShortestPath shortestPath = new BellmanFord();

        Graph graph = new Graph(4);
        graph.addEdge(1,2,4);
        graph.addEdge(1,3,5);
        graph.addEdge(2,3,-2);
        graph.addEdge(3,4,3);

        try {
            int[] actual = shortestPath.shortestPath(graph, 1);
            int[] expected = new int[] {Integer.MAX_VALUE, 0,4,2,5};
            Assertions.assertEquals(expected.length, actual.length);

            for (int i = 1; i< actual.length; ++i){
                Assertions.assertEquals(expected[i], actual[i]);
            }

        } catch (GraphException e) {
            throw new RuntimeException(e);
        }

    }

}
