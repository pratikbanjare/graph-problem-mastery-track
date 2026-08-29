package practice.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BipartiteCheckerTest {

    @Test
    public void testBipartiteChecker(){
        BipartiteChecker bipartiteChecker = new BipartiteChecker();
        Graph graph = new Graph(4);
        graph.addEdge(1,2);
        graph.addEdge(1,3);
        graph.addEdge(2,4);
        graph.addEdge(3,4);

        Assertions.assertTrue(bipartiteChecker.isBipartite(graph));
    }

    @Test
    public void testBipartiteChecker2(){
        BipartiteChecker bipartiteChecker = new BipartiteChecker();
        Graph graph = new Graph(3);
        graph.addEdge(1,2);
        graph.addEdge(1,3);
        graph.addEdge(2,3);

        Assertions.assertFalse(bipartiteChecker.isBipartite(graph));
    }
}
