package practice.path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import practice.exception.GraphException;
import practice.graph.Graph;

public class FlyodWarshallTest {

    @Test
    public void testFlyodWarshallAlgorithm() {
        Graph graph  = new Graph(4);
        graph.addEdge(1,2,4);
        graph.addEdge(2,3,3);
        graph.addEdge(1,3,10);
        graph.addEdge(3,4,2);

        FlyodWarshall algo = new FlyodWarshall();

        int[][] ints = null;
        try {
            ints = algo.flyodWarshallAlgorithm(graph);
        } catch (GraphException e) {
            throw new RuntimeException(e);
        }

        printArray(ints);
    }

    @Test
    public void testFlyodWarshallAlgorithm2() {
        Graph graph  = new Graph(3);
        graph.addEdge(1,2,4);
        graph.addEdge(2,3,-6);
        graph.addEdge(1,3,3);

        FlyodWarshall algo   = new FlyodWarshall();
        int[][] ints = null;
        try {
            ints = algo.flyodWarshallAlgorithm(graph);
        } catch (GraphException e) {
            throw new RuntimeException(e);
        }
        printArray(ints);
    }

    @Test()
    public void testFlyodWarshallAlgorithm3() throws GraphException {
        Graph graph  = new Graph(3);
        graph.addEdge(1,2,3);
        graph.addEdge(2,3,-4);
        graph.addEdge(3,1,0);

        FlyodWarshall algo   = new FlyodWarshall();
        Assertions.assertThrows(GraphException.class, () -> algo.flyodWarshallAlgorithm(graph));

    }
    private void printArray(int[][] ints) {
        for (int i = 1; i < ints.length; ++i) {
            for (int j = 1; j < ints[i].length; ++j) {
                System.out.printf("%15d", ints[i][j]);
            }
            System.out.println();
        }
    }
}
