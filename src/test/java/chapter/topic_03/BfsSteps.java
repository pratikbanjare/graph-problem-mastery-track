package chapter.topic_03;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import practice.graph.Graph;
import practice.path.BFS;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BfsSteps {

    private static final Logger LOGGER = Logger.getLogger(BfsSteps.class.getName());

    private Graph graph;
    private List<Integer> shortestPath;
    private int shortestDistance;

    @Given("a path graph with {int} vertices")
    public void aPathGraphWithVertices(int vertices) {
        graph = new Graph(vertices);
    }

    @Given("the path graph has an edge between {int} and {int}")
    public void thePathGraphHasAnEdgeBetweenAnd(int from, int to) {
        graph.addEdge(from, to);
    }

    @When("I find the shortest BFS path from {int} to {int}")
    public void iFindTheShortestBfsPathFromTo(int source, int target) {
        LOGGER.info(() -> "Finding shortest BFS path from " + source + " to " + target);
        shortestPath = new BFS().shortestDistancePath(graph, source, target);
    }

    @When("I measure the BFS shortest distance from {int} to {int}")
    public void iMeasureTheBfsShortestDistanceFromTo(int source, int target) {
        LOGGER.info(() -> "Measuring BFS shortest distance from " + source + " to " + target);
        shortestDistance = new BFS().shortestDistance(graph, source, target);
    }

    @Then("the BFS path should be")
    public void theBfsPathShouldBe(DataTable dataTable) {
        List<Integer> expectedPath = dataTable.asLists().get(0).stream()
                .filter(cell -> cell != null && !cell.isBlank())
                .map(Integer::parseInt)
                .toList();
        logAssertion("shortest path", expectedPath, shortestPath);
        assertEquals(expectedPath, shortestPath,
                "Expected shortest path " + expectedPath + " but was " + shortestPath);
    }

    @Then("the BFS path should form a valid traversal from {int} to {int}")
    public void theBfsPathShouldFormAValidTraversalFromTo(int source, int target) {
        if (shortestPath.isEmpty()) {
            return;
        }

        logAssertion("path source", source, shortestPath.get(0));
        assertEquals(source, shortestPath.get(0),
                "Expected path to start at " + source + " but was " + shortestPath);
        logAssertion("path target", target, shortestPath.get(shortestPath.size() - 1));
        assertEquals(target, shortestPath.get(shortestPath.size() - 1),
                "Expected path to end at " + target + " but was " + shortestPath);
        for (Integer vertex : shortestPath) {
            logAssertion("path vertex is non-null", false, vertex == null);
            assertFalse(vertex == null, "Path contained a null vertex: " + shortestPath);
        }
    }

    @Then("the BFS path should use {int} edges")
    public void theBfsPathShouldUseEdges(int expectedEdges) {
        int actualEdges = shortestPath.isEmpty() ? 0 : shortestPath.size() - 1;
        logAssertion("number of path edges", expectedEdges, actualEdges);
        assertEquals(expectedEdges, actualEdges,
                "Expected " + expectedEdges + " path edge(s) but was " + actualEdges
                        + " for path " + shortestPath);
    }

    @Then("the BFS path should be empty")
    public void theBfsPathShouldBeEmpty() {
        logAssertion("unreachable path", List.of(), shortestPath);
        assertEquals(List.of(), shortestPath,
                "Expected no path, but found " + shortestPath);
    }

    @Then("the BFS distance should be {int}")
    public void theBfsDistanceShouldBe(int expectedDistance) {
        logAssertion("shortest distance", expectedDistance, shortestDistance);
        assertEquals(expectedDistance, shortestDistance,
                "Expected shortest distance " + expectedDistance
                        + " but was " + shortestDistance);
    }

    private void logAssertion(String subject, Object expected, Object actual) {
        LOGGER.info(() -> "Checking " + subject + ": expected=" + expected + ", actual=" + actual);
    }
}
