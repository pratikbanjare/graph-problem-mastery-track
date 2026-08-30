package practice.path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import practice.exception.GraphException;
import practice.graph.Graph;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShortestPathTest {

    ShortestPath  shortestPath;

    @BeforeAll
    public void init() {
        this.shortestPath = new ShortestPath();
    }

    @Test
    public void testShortestPath(){
        Graph graph = new Graph(3);
        graph.addEdge(1,2,10);
        graph.addEdge(1,3,1);
        graph.addEdge(3,2,1);

        try {
            int[] distance = shortestPath.dijstra(graph, 1);
            Assertions.assertEquals(2, distance[2]);

        } catch (GraphException e) {
            throw new RuntimeException(e);
        }


    }
}
