package practice.mst;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import practice.graph.Graph;
import practice.model.Edge;
import practice.model.GraphType;

import java.util.List;

class KruskalAlgorithmTest {

    @Test
    void testKruskalAlgorithm() {
        Graph graph = new Graph(4, GraphType.UNDIRECTED);
        graph.addEdge(1,2,10);
        graph.addEdge(1,3,6);
        graph.addEdge(1,4,5);
        graph.addEdge(2,4,15);
        graph.addEdge(3,4,4);

        KruskalAlgorithm algo = new KruskalAlgorithm();
        List<Edge> edges = algo.minimumSpanningTree(graph);

        Assertions.assertEquals(3, edges.size());


    }
}
