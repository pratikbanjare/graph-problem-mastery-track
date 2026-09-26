package practice.path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import practice.exception.GraphException;
import practice.graph.Graph;

class FloydWarshallTest {

    @Test()
    void testFloydWarshallAlgorithm3() {
        Graph graph  = new Graph(3);
        graph.addEdge(1,2,3);
        graph.addEdge(2,3,-4);
        graph.addEdge(3,1,0);

        FloydWarshall algo   = new FloydWarshall();
        Assertions.assertThrows(GraphException.class, () -> algo.floydWarshallAlgorithm(graph));

    }
}
