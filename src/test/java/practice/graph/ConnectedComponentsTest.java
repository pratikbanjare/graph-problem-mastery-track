package practice.graph;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConnectedComponentsTest {

    @Test
    void bfsVisitsEachReachableVertexInBreadthFirstOrder() {
        Graph graph = new Graph(4);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);

        ConnectedComponents connectedComponents = new ConnectedComponents(4);
        assertEquals("1 2 3 4 ", captureOutput(() -> connectedComponents.bfs(graph, 1, new boolean[4])));
    }

    @Test
    void connectedComponentTraversesEveryComponent() {
        Graph graph = new Graph(4);
        graph.addEdge(1, 2);
        graph.addEdge(3, 4);

        ConnectedComponents connectedComponents = new ConnectedComponents(4);
        String output = captureOutput(() -> connectedComponents.connectedComponent(graph));

        assertTrue(output.contains("\n1 2 "));
        assertTrue(output.contains("\n3 4 "));
    }

    private String captureOutput(Runnable action) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            action.run();
            return output.toString();
        } finally {
            System.setOut(originalOut);
        }
    }
}
