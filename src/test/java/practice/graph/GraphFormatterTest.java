package practice.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphFormatterTest {

    @Test
    void returnsDiagnosticStringInsteadOfPrinting() {
        Graph graph = new Graph(2);
        graph.addEdge(1, 2, 5);

        GraphFormatter formatter = new GraphFormatter();

        assertEquals(System.lineSeparator() + "For vertex 1 neighbors are -[ (2, 5)]"
                        + System.lineSeparator() + "For vertex 2 neighbors are -[ (1, 5)]",
                formatter.toDebugString(graph));
    }
}
