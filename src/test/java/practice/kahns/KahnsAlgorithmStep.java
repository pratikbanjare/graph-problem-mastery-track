package practice.kahns;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import practice.exception.GraphException;
import practice.graph.Graph;
import practice.graph.TopologicalSortAlgorithm;
import practice.model.GraphType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KahnsAlgorithmStep {

    private Graph graph;
    private TopologicalSortAlgorithm topologicalSortAlgorithm;
    private List<Integer> actualOrder;
    private GraphException thrownException;

    @Given("a directed graph with {int} number of vertices")
    public void givenADirectedGraphWithNumberOfVertices(int vertices) {
        graph = new Graph(vertices, GraphType.DIRECTED);
        topologicalSortAlgorithm = new TopologicalSortAlgorithm();
        actualOrder = null;
        thrownException = null;
    }

    @And("edges")
    public void andEdges(DataTable dataTable) {
        for (Map<String, String> row : dataTable.asMaps()) {
            int from = Integer.parseInt(row.get("from"));
            int to = Integer.parseInt(row.get("to"));
            graph.addEdge(from, to);
        }
    }

    @When("kahns algorithm is called")
    public void whenKahnsAlgorithmIsCalled() {
        try {
            actualOrder = topologicalSortAlgorithm.kahnsAlgorithm(graph);
        } catch (GraphException e) {
            thrownException = e;
        }
    }

    @Then("the topological ordering is")
    public void thenTheTopologicalOrderingIs(DataTable dataTable) {
        List<Integer> expectedOrder = dataTable.asMaps()
                .stream()
                .map(row -> Integer.parseInt(row.get("vertex")))
                .collect(Collectors.toList());

        assertNotNull(actualOrder, "Kahn's algorithm did not return an ordering.");
        assertEquals(expectedOrder, actualOrder,
                "Expected topological ordering: " + expectedOrder + " but got: " + actualOrder);
    }

    @Then("a GraphException is thrown")
    public void thenAGraphExceptionIsThrown() {
        assertNotNull(thrownException, "Expected GraphException to be thrown, but no exception was thrown.");
    }
}