package practice.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import practice.model.GraphType;

import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TopologicalSortAlgorithmTest {


    private TopologicalSortAlgorithm algo;

    @BeforeAll
    void init() {
        algo =  new TopologicalSortAlgorithm();
    }

    @Test
    void testTopologicalOrder() {
        Graph graph = new Graph(4, GraphType.DIRECTED);
        graph.addEdge(1, 2);
        graph.addEdge(1,3);
        graph.addEdge(2,4);
        graph.addEdge(3,4);

        List<Integer> order = algo.dfsTopologicalSort(graph);

        List<Integer> expected = Arrays.asList(1, 3, 2, 4);

        Assertions.assertEquals(expected, order);
    }

    @Test
    void testTopologicalOrder2() {
        Graph graph = new Graph(4, GraphType.DIRECTED);
        graph.addEdge(1, 2);
        graph.addEdge(1,3);
        graph.addEdge(2,4);
        graph.addEdge(4,1);
        Assertions.assertThrows(IllegalArgumentException.class, ()->algo.dfsTopologicalSort(graph));

    }
}
