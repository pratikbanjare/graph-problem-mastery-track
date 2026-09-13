package practice.graph;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GraphTest {

    @Test
    void rejectsNonPositiveVertexCount() {
        assertThrows(IllegalArgumentException.class, () -> new Graph(0));
        assertThrows(IllegalArgumentException.class, () -> new Graph(-1));
    }

    @Test
    void addsAnUndirectedEdge() {
        Graph graph = new Graph(3);

        graph.addEdge(1, 2);

        assertTrue(graph.hasEdge(1, 2));
        assertTrue(graph.hasEdge(2, 1));
        assertFalse(graph.hasEdge(1, 3));
    }

    @Test
    void doesNotAddDuplicateEdges() {
        Graph graph = new Graph(2);

        graph.addEdge(1, 2);
        graph.addEdge(1, 2);

        assertEquals("1 2 ", captureOutput(() -> graph.bfs(1, new boolean[2])));
    }

    @Test
    void removesAnUndirectedEdge() {
        Graph graph = new Graph(3);
        graph.addEdge(1, 2);

        graph.removeEdge(1, 2);

        assertFalse(graph.hasEdge(1, 2));
        assertFalse(graph.hasEdge(2, 1));
    }

    @Test
    void rejectsInvalidVertices() {
        Graph graph = new Graph(3);

        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(0, 1));
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(1, 4));
        assertThrows(IllegalArgumentException.class, () -> graph.hasEdge(0, 1));
        assertThrows(IllegalArgumentException.class, () -> graph.removeEdge(1, 4));
    }

    @Test
    void bfsVisitsEachReachableVertexInBreadthFirstOrder() {
        Graph graph = new Graph(4);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);

        assertEquals("1 2 3 4 ", captureOutput(() -> graph.bfs(1, new boolean[4])));
    }

    @Test
    void connectedComponentTraversesEveryComponent() {
        Graph graph = new Graph(4);
        graph.addEdge(1, 2);
        graph.addEdge(3, 4);

        String output = captureOutput(graph::connectedComponent);

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
