package practice.graph;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConnectedComponentsTest {

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
