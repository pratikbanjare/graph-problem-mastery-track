package practice.mst.kruskal;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import practice.graph.Graph;
import practice.model.Edge;
import practice.model.GraphType;
import practice.mst.KruskalAlgorithm;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KruskalAlgorithmSteps {

    private Graph graph;
    private List<Edge> minimumSpanningTree;
    private IllegalArgumentException thrownException;

    @Given("an undirected graph with {int} vertices")
    public void anUndirectedGraphWithVertices(int vertices) {
        graph = new Graph(vertices, GraphType.UNDIRECTED);
        resetResult();
    }

    @Given("a directed graph with {int} vertices")
    public void aDirectedGraphWithVertices(int vertices) {
        graph = new Graph(vertices, GraphType.DIRECTED);
        resetResult();
    }

    @And("the graph has weighted edges")
    public void theGraphHasWeightedEdges(DataTable dataTable) {
        for (Map<String, String> row : dataTable.asMaps(String.class, String.class)) {
            graph.addEdge(
                    Integer.parseInt(row.get("from")),
                    Integer.parseInt(row.get("to")),
                    Integer.parseInt(row.get("weight")));
        }
    }

    @When("Kruskal's algorithm is called")
    public void kruskalsAlgorithmIsCalled() {
        try {
            minimumSpanningTree = new KruskalAlgorithm().minimumSpanningTree(graph);
        } catch (IllegalArgumentException exception) {
            thrownException = exception;
            minimumSpanningTree = null;
        }
    }

    @Then("the minimum spanning tree contains {int} edges")
    public void theMinimumSpanningTreeContainsEdges(int expectedEdgeCount) {
        assertNotNull(minimumSpanningTree, "Kruskal's algorithm did not return an MST.");
        assertEquals(expectedEdgeCount, minimumSpanningTree.size());
    }

    @Then("the selected edges are")
    public void theSelectedEdgesAre(DataTable dataTable) {
        assertNotNull(minimumSpanningTree, "Kruskal's algorithm did not return an MST.");

        List<EdgeValue> expectedEdges = dataTable.asMaps(String.class, String.class).stream()
                .map(this::toEdgeValue)
                .toList();
        List<EdgeValue> actualEdges = minimumSpanningTree.stream()
                .map(edge -> new EdgeValue(edge.getFrom(), edge.getTo(), edge.getWeight()))
                .toList();

        assertEquals(expectedEdges, actualEdges);
    }

    @Then("the total weight of the minimum spanning tree is {int}")
    public void theTotalWeightOfTheMinimumSpanningTreeIs(int expectedWeight) {
        assertNotNull(minimumSpanningTree, "Kruskal's algorithm did not return an MST.");

        int actualWeight = minimumSpanningTree.stream()
                .mapToInt(Edge::getWeight)
                .sum();
        assertEquals(expectedWeight, actualWeight);
    }

    @Then("the edge from {int} to {int} is not selected")
    public void theEdgeFromToIsNotSelected(int from, int to) {
        assertNotNull(minimumSpanningTree, "Kruskal's algorithm did not return an MST.");
        assertTrue(minimumSpanningTree.stream()
                        .noneMatch(edge -> edge.getFrom() == from && edge.getTo() == to),
                "The edge from " + from + " to " + to + " was selected.");
    }

    @Then("an IllegalArgumentException is thrown")
    public void anIllegalArgumentExceptionIsThrown() {
        assertNotNull(thrownException, "Expected IllegalArgumentException to be thrown.");
    }

    @And("the exception message is {string}")
    public void theExceptionMessageIs(String expectedMessage) {
        assertNotNull(thrownException, "No exception was thrown to verify.");
        assertEquals(expectedMessage, thrownException.getMessage());
    }

    @And("the exception message contains {string}")
    public void theExceptionMessageContains(String expectedMessagePart) {
        assertNotNull(thrownException, "No exception was thrown to verify.");
        assertTrue(thrownException.getMessage().contains(expectedMessagePart),
                "Expected exception message to contain: " + expectedMessagePart);
    }

    private void resetResult() {
        minimumSpanningTree = null;
        thrownException = null;
    }

    private EdgeValue toEdgeValue(Map<String, String> row) {
        return new EdgeValue(
                Integer.parseInt(row.get("from")),
                Integer.parseInt(row.get("to")),
                Integer.parseInt(row.get("weight")));
    }

    private record EdgeValue(int from, int to, int weight) {
    }
}
