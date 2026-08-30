package practice.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class TopologlcaSortAlgorithmTest {


    @Test
    public void testTopologicalOrder() {
        Graph graph = new Graph(4);
        graph.addEdge(1, 2);
        graph.addEdge(1,3);
        graph.addEdge(2,4);
        graph.addEdge(3,4);

        TopologicalSortAlgorithm algo = new TopologicalSortAlgorithm();
        List<Integer> order = algo.dfsTopologicalSort(graph);

        List<Integer> expected = Arrays.asList(1, 3, 2, 4);

        Assertions.assertEquals(expected, order);
    }

    @Test
    public void testTopologicalOrder2() {
        Graph graph = new Graph(4);
        graph.addEdge(1, 2);
        graph.addEdge(1,3);
        graph.addEdge(2,4);
        graph.addEdge(4,1);

        TopologicalSortAlgorithm algo = new TopologicalSortAlgorithm();

        Assertions.assertThrows(IllegalArgumentException.class, ()->algo.dfsTopologicalSort(graph));

    }
}
