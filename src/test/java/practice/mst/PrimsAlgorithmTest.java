package practice.mst;

import org.junit.jupiter.api.Test;
import practice.graph.Graph;
import practice.model.Edge;
import practice.model.GraphType;

import java.util.List;

public class PrimsAlgorithmTest {

    @Test
    public void test() {
        Graph graph = new Graph(4, GraphType.UNDIRECTED);
        graph.addEdge(1,2,2);
        graph.addEdge(1,4,1);
        graph.addEdge(1,3,4);
        graph.addEdge(2,4,3);
        graph.addEdge(3,4,1);

        PrimsAlgorithm algo = new PrimsAlgorithm();
        List<Edge> edges = algo.minimumSpammingTree(graph);
        edges.forEach(edge ->
                System.out.println(edge.getFrom() + " -> " + edge.getTo() + " : " + edge.getWeight()));

    }
}
