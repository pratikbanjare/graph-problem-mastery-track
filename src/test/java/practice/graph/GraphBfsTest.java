package practice.graph;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphBfsTest {

    @Test
    void visitsVerticesLevelByLevel() {
        Graph graph = new Graph(6);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 5);
        graph.addEdge(4, 6);

        boolean[] visited = new boolean[6];
        String output = captureOutput(() -> graph.bfs(1, visited));

        assertEquals("1 2 3 4 5 6 ", output);
        assertArrayEquals(new boolean[]{true, true, true, true, true, true}, visited);
    }

    @Test
    void doesNotCrossIntoAnUnconnectedComponent() {
        Graph graph = new Graph(5);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(4, 5);

        boolean[] visited = new boolean[5];
        String output = captureOutput(() -> graph.bfs(1, visited));

        assertEquals("1 2 3 ", output);
        assertArrayEquals(new boolean[]{true, true, true, false, false}, visited);
    }

    private String captureOutput(Runnable operation) {
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(capturedOutput, true, StandardCharsets.UTF_8));
            operation.run();
            return capturedOutput.toString(StandardCharsets.UTF_8);
        } finally {
            System.setOut(originalOutput);
        }
    }
}
