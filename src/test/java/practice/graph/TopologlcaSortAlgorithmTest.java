package practice.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import practice.Exception.GraphException;

import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TopologlcaSortAlgorithmTest {


    private TopologicalSortAlgorithm algo;

    @BeforeAll
    public void init() {
        algo =  new TopologicalSortAlgorithm();
    }

    @Test
    public void testTopologicalOrder() {
        Graph graph = new Graph(4);
        graph.addEdge(1, 2);
        graph.addEdge(1,3);
        graph.addEdge(2,4);
        graph.addEdge(3,4);

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
        Assertions.assertThrows(IllegalArgumentException.class, ()->algo.dfsTopologicalSort(graph));

    }

    @Test
    public void kahnsAlgorithmTest() {
        Graph graph = new Graph(4);

        graph.addEdge(1,3);
        graph.addEdge(2,3);
        graph.addEdge(3,4);

        try {
            List<Integer> actual = algo.kahnsAlgorithm(graph);
            List<Integer> expected = Arrays.asList(1, 2, 3, 4);
            Assertions.assertEquals(expected, actual);

        } catch (GraphException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void kahnsAlgorithmTest2() {
        Graph graph = new Graph(4);
        graph.addEdge(1,3);
        graph.addEdge(2,3);
        graph.addEdge(3,4);
        graph.addEdge(4,1);

        Assertions.assertThrows(GraphException.class, () -> algo.kahnsAlgorithm(graph));

    }
}
