package practice.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import practice.exception.GraphException;
import practice.model.GraphType;

import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KahnsAlgorithmTest {

    private KahnsAlgorithm algo;

    @BeforeAll
    void init() {
        algo =  new KahnsAlgorithm();
    }

    @Test
    void kahnsAlgorithmTest() {
        Graph graph = new Graph(4, GraphType.DIRECTED);

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
    void kahnsAlgorithmTest2() {
        Graph graph = new Graph(4, GraphType.DIRECTED);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 1);

        Assertions.assertThrows(GraphException.class, () -> algo.kahnsAlgorithm(graph));
    }
}
