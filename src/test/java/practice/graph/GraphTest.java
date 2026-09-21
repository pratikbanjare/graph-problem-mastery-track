package practice.graph;

import org.junit.jupiter.api.Test;

import practice.model.GraphType;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

        assertTrue(graph.hasEdge(1, 2));
        assertTrue(graph.hasEdge(2, 1));
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
    void canAddEdgeAfterRemovingOne() {
        Graph graph = new Graph(3);

        graph.addEdge(1, 2);
        graph.removeEdge(1, 2);

        assertDoesNotThrow(() -> graph.addEdge(1, 3));
        assertTrue(graph.hasEdge(1, 3));
    }

    @Test
    void doesNotExposeMutableAdjacencyLists() {
        Graph graph = new Graph(2);
        graph.addEdge(1, 2);

        assertThrows(UnsupportedOperationException.class,
                () -> graph.getWeightedAdjacencyList().get(0).clear());
        assertThrows(UnsupportedOperationException.class,
                () -> graph.getWeightedEdgesOfVertex(1).clear());

        assertTrue(graph.hasEdge(1, 2));
    }

    @Test
    void preservesDirectionWhenReversingDirectedGraph() {
        Graph graph = new Graph(3, GraphType.DIRECTED);
        graph.addEdge(1, 2, 7);

        Graph reversed = GraphOperation.reverse(graph);

        assertFalse(reversed.hasEdge(1, 2));
        assertTrue(reversed.hasEdge(2, 1));
        assertEquals(7, reversed.getWeightedEdgesOfVertex(2).get(0).getWeight());
    }

    @Test
    void rejectsNullGraphWhenReversing() {
        assertThrows(NullPointerException.class, () -> GraphOperation.reverse(null));
    }

}
