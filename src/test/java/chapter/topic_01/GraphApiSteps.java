package chapter.topic_01;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import practice.exception.GraphException;
import practice.graph.Graph;
import practice.model.GraphType;
import practice.model.WeightedEdge;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphApiSteps {

    private static final Logger LOGGER = Logger.getLogger(GraphApiSteps.class.getName());

    private Graph graph;
    private Throwable exception;

    @Given("an undirected graph with {int} vertices")
    public void anUndirectedGraphWithVertices(int vertices) {
        graph = new Graph(vertices);
    }

    @Given("a directed graph with {int} vertices")
    public void aDirectedGraphWithVertices(int vertices) {
        graph = new Graph(vertices, GraphType.DIRECTED);
    }

    @When("I create a graph with {int} vertices")
    public void iCreateAGraphWithVertices(int vertices) {
        captureException(() -> graph = new Graph(vertices));
    }

    @When("I add an edge from {int} to {int}")
    public void iAddAnEdgeFromTo(int from, int to) {
        captureException(() -> graph.addEdge(from, to));
    }

    @When("I try to add an edge from {int} to {int}")
    public void iTryToAddAnEdgeFromTo(int from, int to) {
        captureException(() -> graph.addEdge(from, to));
    }

    @When("I add an edge from {int} to {int} with weight {int}")
    public void iAddAnEdgeFromToWithWeight(int from, int to, int weight) {
        captureException(() -> graph.addEdge(from, to, weight));
    }

    @When("I remove the edge from {int} to {int}")
    public void iRemoveTheEdgeFromTo(int from, int to) {
        captureException(() -> graph.removeEdge(from, to));
    }

    @Then("the graph should have {int} vertices")
    public void theGraphShouldHaveVertices(int expectedVertices) {
        int actualVertices = graph.getVertexCount();
        logAssertion("vertex count", expectedVertices, actualVertices);
        assertEquals(expectedVertices, actualVertices,
                "Expected vertex count " + expectedVertices + " but was " + actualVertices);
    }

    @Then("the graph type should be {string}")
    public void theGraphTypeShouldBe(String expectedType) {
        GraphType actualType = graph.getGraphType();
        logAssertion("graph type", expectedType, actualType);
        assertEquals(GraphType.valueOf(expectedType), actualType,
                "Expected graph type " + expectedType + " but was " + actualType);
    }

    @Then("every vertex should have no neighbors")
    public void everyVertexShouldHaveNoNeighbors() {
        List<List<WeightedEdge>> adjacencyList = graph.getWeightedAdjacencyList();
        boolean hasNoNeighbors = adjacencyList.stream().allMatch(List::isEmpty);
        LOGGER.info(() -> "Checking empty adjacency lists: actual=" + adjacencyList);
        assertTrue(hasNoNeighbors, "Expected every vertex to have no neighbors but was " + adjacencyList);
    }

    @Then("the edge from {int} to {int} should exist")
    public void theEdgeFromToShouldExist(int from, int to) {
        boolean actual = graph.hasEdge(from, to);
        logAssertion("edge " + from + " -> " + to, true, actual);
        assertTrue(actual, "Expected edge " + from + " -> " + to + " to exist");
    }

    @Then("the edge from {int} to {int} should not exist")
    public void theEdgeFromToShouldNotExist(int from, int to) {
        boolean actual = graph.hasEdge(from, to);
        logAssertion("edge " + from + " -> " + to, false, actual);
        assertFalse(actual, "Expected edge " + from + " -> " + to + " not to exist");
    }

    @Then("the neighbors of vertex {int} should be {string}")
    public void theNeighborsOfVertexShouldBe(int vertex, String expectedNeighbors) {
        List<Integer> expected = parseIntegerList(expectedNeighbors);
        List<Integer> actual = graph.getEdgesOfVertex(vertex);
        logAssertion("neighbors of vertex " + vertex, expected, actual);
        assertEquals(expected, actual,
                "Unexpected neighbors for vertex " + vertex);
    }

    @Then("the weighted neighbors of vertex {int} should be:")
    public void theWeightedNeighborsOfVertexShouldBe(int vertex, DataTable dataTable) {
        List<WeightedEdge> actual = graph.getWeightedEdgesOfVertex(vertex);
        List<Map<String, String>> expected = dataTable.asMaps();

        logAssertion("weighted neighbors of vertex " + vertex, expected, actual);
        assertEquals(expected.size(), actual.size(),
                "Unexpected number of weighted neighbors for vertex " + vertex
                        + ": expected " + expected.size() + " but was " + actual.size());
        for (int index = 0; index < actual.size(); index++) {
            int expectedVertex = Integer.parseInt(expected.get(index).get("vertex"));
            int expectedWeight = Integer.parseInt(expected.get(index).get("weight"));
            assertEquals(expectedVertex, actual.get(index).getTo(),
                    "Unexpected destination at weighted neighbor index " + index);
            assertEquals(expectedWeight, actual.get(index).getWeight(),
                    "Unexpected weight for weighted neighbor at index " + index);
        }
    }


    @Then("the graph should contain {int} edges")
    public void theGraphShouldContainEdges(int expectedEdges) {
        int actualEdges = graph.getEdges().size();
        logAssertion("edge count", expectedEdges, actualEdges);
        assertEquals(expectedEdges, actualEdges,
                "Expected " + expectedEdges + " edge(s) but found " + actualEdges);
    }

    @Then("the edge from {int} to {int} should have weight {int}")
    public void theEdgeFromToShouldHaveWeight(int from, int to, int expectedWeight) {
        var edge = graph.getEdges().stream()
                .filter(candidate -> candidate.getFrom() == from && candidate.getTo() == to)
                .findFirst()
                .orElse(null);
        assertNotNull(edge, "Expected edge " + from + " -> " + to + " to be present");
        var weightedEdge = graph.getWeightedEdgesOfVertex(from).stream()
                .filter(candidate -> candidate.getTo() == to)
                .findFirst()
                .orElse(null);
        assertNotNull(weightedEdge, "Expected weighted edge " + from + " -> " + to + " to be present");
        logAssertion("weight of edge " + from + " -> " + to, expectedWeight, weightedEdge.getWeight());
        assertEquals(expectedWeight, weightedEdge.getWeight(),
                "Expected weight " + expectedWeight + " for edge " + from + " -> " + to
                        + " but was " + weightedEdge.getWeight());
    }

    @Then("a graph exception should be raised")
    public void aGraphExceptionShouldBeRaised() {
        LOGGER.info(() -> "Captured exception: " + exception);
        assertTrue(exception instanceof GraphException,
                "Expected GraphException but captured " + exception);
    }

    @Then("the exception message should be {string}")
    public void theExceptionMessageShouldBe(String expectedMessage) {
        String actualMessage = exception == null ? null : exception.getMessage();
        logAssertion("exception message", expectedMessage, actualMessage);
        assertEquals(expectedMessage, actualMessage,
                "Unexpected exception message");
    }

    @Then("the exception message should contain {string}")
    public void theExceptionMessageShouldContain(String expectedMessage) {
        String actualMessage = exception == null ? null : exception.getMessage();
        logAssertion("exception message containing text", expectedMessage, actualMessage);
        assertTrue(actualMessage != null && actualMessage.contains(expectedMessage),
                "Expected exception message to contain \"" + expectedMessage
                        + "\" but was \"" + actualMessage + "\"");
    }

    private void captureException(Runnable operation) {
        exception = null;
        try {
            operation.run();
        } catch (Throwable thrown) {
            exception = thrown;
            LOGGER.info(() -> "Graph operation failed: " + thrown);
        }
    }

    private void logAssertion(String subject, Object expected, Object actual) {
        LOGGER.info(() -> "Checking " + subject + ": expected=" + expected + ", actual=" + actual);
    }

    private List<Integer> parseIntegerList(String value) {
        String contents = value.substring(1, value.length() - 1).trim();
        if (contents.isEmpty()) {
            return List.of();
        }
        return java.util.Arrays.stream(contents.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();
    }
}
