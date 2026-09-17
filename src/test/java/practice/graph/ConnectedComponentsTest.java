package practice.graph;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConnectedComponentsTest {

    @Test
    void bfsVisitsEachReachableVertexInBreadthFirstOrder() {
        Graph graph = new Graph(4);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);

        ConnectedComponents connectedComponents = new ConnectedComponents();
        assertEquals(List.of(1, 2, 3, 4), connectedComponents.bfs(graph, 1, new boolean[graph.getVerticesCount()+1]));
    }

    @Test
    void connectedComponentTraversesEveryComponent() {
        Graph graph = new Graph(4);
        graph.addEdge(1, 2);
        graph.addEdge(3, 4);

        ConnectedComponents connectedComponents = new ConnectedComponents();
        assertEquals(
                List.of(
                        List.of(1, 2),
                        List.of(3, 4)
                ),
                connectedComponents.connectedComponent(graph)
        );
    }
}
