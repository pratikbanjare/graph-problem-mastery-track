package practice.path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import practice.exception.GraphException;
import practice.graph.Graph;
import practice.model.GraphType;

public class FlyodWarshallTest {

    @Test()
    void testFlyodWarshallAlgorithm3() {
        Graph graph  = new Graph(3);
        graph.addEdge(1,2,3);
        graph.addEdge(2,3,-4);
        graph.addEdge(3,1,0);

        FlyodWarshall algo   = new FlyodWarshall();
        Assertions.assertThrows(GraphException.class, () -> algo.flyodWarshallAlgorithm(graph));

    }
}
