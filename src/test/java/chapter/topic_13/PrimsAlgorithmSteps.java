package chapter.topic_13;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import practice.graph.Graph;
import practice.model.Edge;
import practice.model.GraphType;
import practice.mst.PrimsAlgorithm;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrimsAlgorithmSteps {

    private static final Logger LOGGER = Logger.getLogger(PrimsAlgorithmSteps.class.getName());
    private Graph graph;
    private List<Edge> minimumSpanningTree;
    private IllegalArgumentException thrownException;

    @Given("an undirected graph with {int} vertices")
    public void anUndirectedGraphWithVertices(int vertices) {
        graph = new Graph(vertices, GraphType.UNDIRECTED);
        resetResult();
        LOGGER.info("Created undirected graph with " + vertices + " vertices");
    }

    @And("the graph has weighted edges")
    public void theGraphHasWeightedEdges(DataTable dataTable) {
        for (Map<String, String> row : dataTable.asMaps(String.class, String.class)) {
            graph.addEdge(
                    Integer.parseInt(row.get("from")),
                    Integer.parseInt(row.get("to")),
                    Integer.parseInt(row.get("weight")));
        }
        LOGGER.info("Added weighted edges: " + describeGraph());
    }

    @When("Prim's algorithm is called")
    public void primsAlgorithmIsCalled() {
        try {
            minimumSpanningTree = new PrimsAlgorithm().minimumSpammingTree(graph);
            thrownException = null;
            LOGGER.info("Prim returned " + describeEdges(minimumSpanningTree));
        } catch (IllegalArgumentException exception) {
            thrownException = exception;
            minimumSpanningTree = null;
            LOGGER.warning("Prim rejected graph " + describeGraph()
                    + " with message: " + exception.getMessage());
        }
    }

    @Then("the minimum spanning tree contains {int} edges")
    public void theMinimumSpanningTreeContainsEdges(int expectedEdgeCount) {
        String actual = minimumSpanningTree == null ? "null" : String.valueOf(minimumSpanningTree.size());
        LOGGER.info("Checking MST edge count: expected=" + expectedEdgeCount
                + ", actual=" + actual + ", selected=" + describeEdges(minimumSpanningTree));
        assertNotNull(minimumSpanningTree, "Prim's algorithm did not return an MST for "
                + describeGraph());
        assertEquals(expectedEdgeCount, minimumSpanningTree.size(),
                "Unexpected MST edge count for graph " + describeGraph());
    }

    @Then("the selected edges are")
    public void theSelectedEdgesAre(DataTable dataTable) {
        assertNotNull(minimumSpanningTree, "Prim's algorithm did not return an MST.");

        List<EdgeValue> expectedEdges = dataTable.asMaps(String.class, String.class).stream()
                .map(this::toEdgeValue)
                .toList();
        List<EdgeValue> actualEdges = minimumSpanningTree.stream()
                .map(edge -> new EdgeValue(edge.getFrom(), edge.getTo(), edge.getWeight()))
                .toList();

        LOGGER.info("Checking selected edges: expected=" + expectedEdges + ", actual=" + actualEdges
                + ", graph=" + describeGraph());
        assertEquals(expectedEdges, actualEdges,
                "Selected MST edges differ for graph " + describeGraph());
    }

    @Then("the total weight of the minimum spanning tree is {int}")
    public void theTotalWeightOfTheMinimumSpanningTreeIs(int expectedWeight) {
        assertNotNull(minimumSpanningTree, "Prim's algorithm did not return an MST.");

        int actualWeight = minimumSpanningTree.stream()
                .mapToInt(Edge::getWeight)
                .sum();
        LOGGER.info("Checking MST total weight: expected=" + expectedWeight + ", actual=" + actualWeight
                + ", selected=" + describeEdges(minimumSpanningTree));
        assertEquals(expectedWeight, actualWeight,
                "Unexpected MST total weight for graph " + describeGraph());
    }

    @Then("the edge from {int} to {int} is not selected")
    public void theEdgeFromToIsNotSelected(int from, int to) {
        assertNotNull(minimumSpanningTree, "Prim's algorithm did not return an MST.");
        boolean selected = minimumSpanningTree.stream()
                .anyMatch(edge -> edge.getFrom() == from && edge.getTo() == to);
        LOGGER.info("Checking rejected edge " + from + " -> " + to + ": selected=" + selected
                + ", actual MST=" + describeEdges(minimumSpanningTree));
        assertTrue(!selected, "The edge from " + from + " to " + to
                + " was selected. MST=" + describeEdges(minimumSpanningTree));
    }

    @Then("an IllegalArgumentException is thrown")
    public void anIllegalArgumentExceptionIsThrown() {
        LOGGER.info("Checking expected IllegalArgumentException: "
                + (thrownException == null ? "none" : thrownException.getMessage()));
        assertNotNull(thrownException, "Expected IllegalArgumentException to be thrown for graph "
                + describeGraph());
    }

    @And("the exception message is {string}")
    public void theExceptionMessageIs(String expectedMessage) {
        assertNotNull(thrownException, "No exception was thrown to verify.");
        LOGGER.info("Checking exception message: expected=\"" + expectedMessage + "\", actual=\""
                + thrownException.getMessage() + "\"");
        assertEquals(expectedMessage, thrownException.getMessage());
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

    private String describeGraph() {
        return graph.getEdges().stream().map(this::describeEdge).toList().toString();
    }

    private String describeEdges(List<Edge> edges) {
        return edges == null ? "null" : edges.stream().map(this::describeEdge).toList().toString();
    }

    private String describeEdge(Edge edge) {
        return edge.getFrom() + " -> " + edge.getTo() + " (" + edge.getWeight() + ")";
    }

    private record EdgeValue(int from, int to, int weight) {
    }
}
