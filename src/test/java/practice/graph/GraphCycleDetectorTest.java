package practice.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GraphCycleDetectorTest {

    @Test
    public void dfsCycleTest(){
        Graph graph = new Graph(4);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(3,1);

        GraphCycleDetector detector = new GraphCycleDetector();
        Assertions.assertTrue(detector.dfsCycle(graph));
    }

    @Test
    public void dfsCycleExistTest2(){
        Graph graph = new Graph(4);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(1, 3);


        GraphCycleDetector detector = new GraphCycleDetector();
        Assertions.assertFalse(detector.dfsCycle(graph));
    }

}
