package practice.scc;

import org.junit.jupiter.api.Test;
import practice.graph.Graph;
import practice.model.GraphType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TarjanAlgorithmTest {


    @Test
    void testTarjanAlgorithm() {
        TarjanAlgorithm tarjanAlgorithm = new TarjanAlgorithm();

        Graph graph = new Graph(5, GraphType.DIRECTED);
        graph.addEdge(1,2);
        graph.addEdge(2,4);
        graph.addEdge(1,3);
        graph.addEdge(3,4);
        graph.addEdge(4,5);
        graph.addEdge(5,4);

        List<List<Integer>> lists = tarjanAlgorithm.stronglyConnectedComponents(graph);

        assertEquals(List.of(List.of(5, 4), List.of(2), List.of(3), List.of(1)), lists);

        for (List<Integer> list : lists){
            list.forEach(x -> System.out.print("-> " + x));
            System.out.println();
        }
    }
}
