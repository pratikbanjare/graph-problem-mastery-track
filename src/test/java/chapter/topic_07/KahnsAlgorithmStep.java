package chapter.topic_07;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import practice.exception.GraphException;
import practice.graph.Graph;
import practice.graph.KahnsAlgorithm;
import practice.model.Edge;
import practice.model.GraphType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.logging.Logger;

public class KahnsAlgorithmStep {

    private static final Logger LOGGER = Logger.getLogger(KahnsAlgorithmStep.class.getName());
    private Graph graph;
    private KahnsAlgorithm kahnsAlgorithm;
    private List<Integer> actualOrder;
    private GraphException thrownException;

    @Given("a directed graph with {int} number of vertices")
    public void givenADirectedGraphWithNumberOfVertices(int vertices) {
        graph = new Graph(vertices, GraphType.DIRECTED);
        kahnsAlgorithm = new KahnsAlgorithm();
        actualOrder = null;
        thrownException = null;
        LOGGER.info("Created directed graph with vertices 1 through " + vertices);
    }

    @And("edges")
    public void andEdges(DataTable dataTable) {
        for (Map<String, String> row : dataTable.asMaps()) {
            int from = Integer.parseInt(row.get("from"));
            int to = Integer.parseInt(row.get("to"));
            graph.addEdge(from, to);
        }
        LOGGER.info("Added edges to " + describeGraph());
    }

    @When("kahns algorithm is called")
    public void whenKahnsAlgorithmIsCalled() {
        try {
            actualOrder = kahnsAlgorithm.kahnsAlgorithm(graph);
            LOGGER.info("Kahn's algorithm returned " + actualOrder + " for " + describeGraph());
        } catch (GraphException e) {
            thrownException = e;
            LOGGER.info("Kahn's algorithm failed for " + describeGraph() + ": "
                    + describeException());
        }
    }

    @Then("the topological ordering is")
    public void thenTheTopologicalOrderingIs(DataTable dataTable) {
        List<Integer> expectedOrder = dataTable.asMaps()
                .stream()
                .map(row -> Integer.parseInt(row.get("vertex")))
                .collect(Collectors.toList());

        LOGGER.info("Checking topological order for " + describeGraph()
                + "; expected=" + expectedOrder + ", actual=" + actualOrder);
        assertNotNull(actualOrder, "Kahn's algorithm returned no ordering for "
                + describeGraph() + "; exception=" + describeException());
        assertEquals(expectedOrder, actualOrder,
                "Topological ordering mismatch for " + describeGraph()
                        + ". Expected " + expectedOrder + " but got " + actualOrder + ".");
    }

    @Then("a GraphException is thrown")
    public void thenAGraphExceptionIsThrown() {
        LOGGER.info("Checking expected GraphException for " + describeGraph()
                + "; actual exception=" + describeException());
        assertNotNull(thrownException, "Expected GraphException for " + describeGraph()
                + ", but no exception was thrown. Returned order=" + actualOrder);
    }

    private String describeGraph() {
        List<String> edges = new ArrayList<>();
        for (Edge edge : graph.getEdges()) {
            edges.add(edge.getFrom() + " -> " + edge.getTo());
        }
        return "vertices=1.." + graph.getVertexCount() + ", edges=" + edges;
    }

    private String describeException() {
        if (thrownException == null) {
            return "<none>";
        }
        return thrownException.getClass().getSimpleName() + ": " + thrownException.getMessage();
    }
}