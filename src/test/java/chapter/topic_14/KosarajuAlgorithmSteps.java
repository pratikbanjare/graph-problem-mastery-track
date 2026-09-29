package chapter.topic_14;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import practice.graph.Graph;
import practice.model.GraphType;
import practice.scc.KosarajuAlgorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class KosarajuAlgorithmSteps {

    private static final Logger LOGGER = Logger.getLogger(KosarajuAlgorithmSteps.class.getName());
    private Graph graph;
    private List<List<Integer>> components;
    private Exception exception;

    @Given("a directed graph with the following edges:")
    public void aDirectedGraphWithTheFollowingEdges(DataTable dataTable) {
        graph = createGraphFromEdges(dataTable, GraphType.DIRECTED);
        resetResult();
        LOGGER.info("Created directed graph: " + describeGraph());
    }

    @Given("a directed graph with {int} vertices and the following edges:")
    public void aDirectedGraphWithVerticesAndTheFollowingEdges(int vertices, DataTable dataTable) {
        graph = new Graph(vertices, GraphType.DIRECTED);
        for (List<String> row : dataTable.asLists().subList(1, dataTable.height())) {
            graph.addEdge(Integer.parseInt(row.get(0)), Integer.parseInt(row.get(1)));
        }
        resetResult();
        LOGGER.info("Created directed graph with explicit vertex count: " + describeGraph());
    }

    @Given("a directed graph with {int} vertices and no edges")
    public void aDirectedGraphWithVerticesAndNoEdges(int vertices) {
        graph = new Graph(vertices, GraphType.DIRECTED);
        resetResult();
        LOGGER.info("Created directed graph with " + vertices + " vertices and no edges");
    }

    @Given("an undirected graph with the following edges:")
    public void anUndirectedGraphWithTheFollowingEdges(DataTable dataTable) {
        graph = createGraphFromEdges(dataTable, GraphType.UNDIRECTED);
        resetResult();
        LOGGER.info("Created undirected graph: " + describeGraph());
    }

    @When("I find the strongly connected components")
    public void iFindTheStronglyConnectedComponents() {
        exception = null;
        components = new KosarajuAlgorithm().stronglyConnectedAlgorithm(graph);
        LOGGER.info("Kosaraju returned components " + components + " for graph " + describeGraph());
    }

    @When("I attempt to find the strongly connected components")
    public void iAttemptToFindTheStronglyConnectedComponents() {
        try {
            components = new KosarajuAlgorithm().stronglyConnectedAlgorithm(graph);
            exception = null;
            LOGGER.info("Kosaraju returned components " + components + " for graph " + describeGraph());
        } catch (Exception caughtException) {
            exception = caughtException;
            components = null;
            LOGGER.warning("Kosaraju rejected graph " + describeGraph() + " with "
                    + caughtException.getClass().getSimpleName() + ": " + caughtException.getMessage());
        }
    }

    @Then("the strongly connected components should be:")
    public void theStronglyConnectedComponentsShouldBe(DataTable dataTable) {
        Set<Set<Integer>> expected = normalize(readExpectedComponents(dataTable));
        Set<Set<Integer>> actual = components == null ? null : normalize(components);
        LOGGER.info("Checking SCC partition: expected=" + expected + ", actual=" + actual
                + ", graph=" + describeGraph());
        assertNotNull(components, "Kosaraju returned null for graph " + describeGraph());
        assertEquals(graph.getVertexCount(), components.stream()
                .flatMap(List::stream)
                .count(), "Every graph vertex must appear exactly once across SCCs for graph "
                + describeGraph() + "; actual=" + components);
        assertEquals(graph.getVertexCount(), components.stream()
                .flatMap(List::stream)
                .distinct()
                .count(), "SCC output contains a missing or repeated vertex for graph "
                + describeGraph() + "; actual=" + components);
        assertEquals(expected, actual, "Unexpected strongly connected components for graph "
                + describeGraph() + "; expected=" + expected + ", actual=" + actual);
    }

    @Then("an IllegalArgumentException should be thrown")
    public void anIllegalArgumentExceptionShouldBeThrown() {
        LOGGER.info("Checking expected IllegalArgumentException: "
                + (exception == null ? "none" : exception.getClass().getSimpleName()
                + ": " + exception.getMessage()));
        assertInstanceOf(IllegalArgumentException.class, exception,
                "Expected Kosaraju to reject graph " + describeGraph()
                        + ", but got " + (exception == null ? "no exception"
                        : exception.getClass().getSimpleName() + ": " + exception.getMessage()));
    }

    @Then("the exception message should be:")
    public void theExceptionMessageShouldBe(String expectedMessage) {
        String actualMessage = exception == null ? null : exception.getMessage();
        LOGGER.info("Checking exception message: expected=\"" + expectedMessage
                + "\", actual=\"" + actualMessage + "\"");
        assertEquals(expectedMessage, actualMessage,
                "Unexpected exception message for graph " + describeGraph());
    }

    private Graph createGraphFromEdges(DataTable dataTable, GraphType graphType) {
        List<List<String>> rows = dataTable.asLists();
        int vertices = rows.stream()
                .skip(1)
                .mapToInt(row -> Math.max(Integer.parseInt(row.get(0)), Integer.parseInt(row.get(1))))
                .max()
                .orElseThrow(() -> new IllegalArgumentException("At least one edge is required"));

        Graph createdGraph = new Graph(vertices, graphType);
        for (List<String> row : rows.subList(1, rows.size())) {
            createdGraph.addEdge(Integer.parseInt(row.get(0)), Integer.parseInt(row.get(1)));
        }
        return createdGraph;
    }

    private void resetResult() {
        components = null;
        exception = null;
    }

    private String describeGraph() {
        return graph.getGraphType() + " graph with " + graph.getVertexCount()
                + " vertices and edges " + graph.getEdges().stream()
                .map(edge -> edge.getFrom() + " -> " + edge.getTo()
                        + " (weight " + edge.getWeight() + ")")
                .toList();
    }

    private List<List<Integer>> readExpectedComponents(DataTable dataTable) {
        List<List<Integer>> expected = new ArrayList<>();
        for (List<String> row : dataTable.asLists().subList(1, dataTable.height())) {
            List<Integer> component = new ArrayList<>();
            for (String vertex : row.get(0).split(",")) {
                component.add(Integer.parseInt(vertex.trim()));
            }
            expected.add(component);
        }
        return expected;
    }

    private Set<Set<Integer>> normalize(List<List<Integer>> componentsToNormalize) {
        Set<Set<Integer>> normalized = new HashSet<>();
        for (List<Integer> component : componentsToNormalize) {
            normalized.add(new HashSet<>(component));
        }
        return normalized;
    }
}
