package chapter.topic_08;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import practice.graph.Graph;
import practice.model.GraphType;
import practice.path.ShortestPath;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DijkstraShortestPathSteps {

    private static final Logger LOGGER = Logger.getLogger(DijkstraShortestPathSteps.class.getName());

    private Graph graph;
    private ShortestPath shortestPath = new ShortestPath();
    private int[] distances;
    private List<Integer> shortestPathList;
    private RuntimeException validationException;
    private int source;

    @Given("a weighted graph with vertices {int} to {int}")
    public void aWeightedGraphWithVerticesTo(int startVertex, int endVertex) {
        if (startVertex != 1) {
            throw new IllegalArgumentException("This feature assumes graph vertices start at 1");
        }
        this.graph = new Graph(endVertex, GraphType.DIRECTED);
        LOGGER.info(() -> "Created directed weighted graph with vertices 1 to " + endVertex);
    }

    @Given("edges:")
    public void edges(DataTable dataTable) {
        for (var row : dataTable.asMaps()) {
            int from = Integer.parseInt(row.get("from"));
            int to = Integer.parseInt(row.get("to"));
            int weight = Integer.parseInt(row.get("weight"));
            graph.addEdge(from, to, weight);
            LOGGER.info(() -> "Added edge " + from + " -> " + to + " (weight " + weight + ")");
        }
    }

    @Given("no path exists from vertex {int} to vertex {int}")
    public void noPathExistsFromVertexToVertex(int from, int to) {
        this.source = from;
        this.graph = new Graph(Math.max(from, to));
        LOGGER.info(() -> "Prepared disconnected graph with no route from " + from + " to " + to);
    }

    @When("the shortest distances are computed from source {int}")
    public void theShortestDistancesAreComputedFromSource(int sourceVertex) {
        this.source = sourceVertex;
        LOGGER.info(() -> "Computing shortest distances from source " + sourceVertex);
        this.distances = shortestPath.djikstra(graph, sourceVertex);
        LOGGER.info(() -> "Distances result: " + Arrays.toString(this.distances));
    }

    @When("the shortest path is requested from source {int} to target {int}")
    public void theShortestPathIsRequestedFromSourceToTarget(int sourceVertex, int targetVertex) {
        this.source = sourceVertex;
        LOGGER.info(() -> "Requesting shortest path from " + sourceVertex + " to " + targetVertex);
        this.shortestPathList = shortestPath.djikstraPath(graph, sourceVertex, targetVertex);
        this.distances = shortestPath.djikstra(graph, sourceVertex);
        LOGGER.info(() -> "Path result: " + shortestPathList + ", distances: " + Arrays.toString(this.distances));
    }

    @When("validation is performed for Dijkstra's algorithm")
    public void validationIsPerformedForDijkstrasAlgorithm() {
        this.validationException = null;
        try {
            LOGGER.info(() -> "Validating graph before Dijkstra: " + graph);
            shortestPath.validateGraph(graph);
            LOGGER.info(() -> "Validation succeeded.");
        } catch (RuntimeException exception) {
            this.validationException = exception;
            LOGGER.warning(() -> "Validation failed: " + exception.getMessage());
        }
    }

    @Then("the distance to vertex {int} should be {int}")
    public void theDistanceToVertexShouldBe(int vertex, int expectedDistance) {
        assertNotNull(graph, "Graph should be initialized before checking distances");
        LOGGER.info(() -> "Checking distance to vertex " + vertex + ": expected=" + expectedDistance + ", actual=" + distances[vertex]);
        assertEquals(expectedDistance, distances[vertex],
                "Expected distance to vertex " + vertex + " to be " + expectedDistance + " but was " + distances[vertex]);
    }

    @Then("the distance to vertex {int} should be infinity")
    public void theDistanceToVertexShouldBeInfinity(int vertex) {
        assertNotNull(graph, "Graph should be initialized before checking distances");
        LOGGER.info(() -> "Checking unreachable vertex " + vertex + ": expected=Infinity, actual=" + distances[vertex]);
        assertEquals(Integer.MAX_VALUE, distances[vertex],
                "Expected vertex " + vertex + " to stay unreachable, but the distance was " + distances[vertex]);
    }

    @Then("^the returned path should be \\[(.*)\\]$")
    public void theReturnedPathShouldBe(String expectedPath) {
        List<Integer> expected = parsePath(expectedPath);
        LOGGER.info(() -> "Checking returned path: expected=" + expected + ", actual=" + shortestPathList);
        assertEquals(expected, shortestPathList,
                "Expected returned path " + expected + " but was " + shortestPathList);
    }

    @Then("^the path should be \\[(.*)\\]$")
    public void thePathShouldBeList(String expectedPath) {
        List<Integer> expected = parsePath(expectedPath);
        LOGGER.info(() -> "Checking path: expected=" + expected + ", actual=" + shortestPathList);
        assertEquals(expected, shortestPathList,
                "Expected path " + expected + " but was " + shortestPathList);
    }

    @Then("the path should be empty")
    public void thePathShouldBeEmpty() {
        LOGGER.info(() -> "Checking empty path: actual=" + shortestPathList);
        assertTrue(shortestPathList.isEmpty(), "Expected no path, but got " + shortestPathList);
    }

    @Then("the distance to the source should be {int}")
    public void theDistanceToTheSourceShouldBe(int expectedDistance) {
        LOGGER.info(() -> "Checking source distance: expected=" + expectedDistance + ", actual=" + distances[source]);
        assertEquals(expectedDistance, distances[source],
                "Expected source distance to be " + expectedDistance + " but was " + distances[source]);
    }

    @Then("the graph should be rejected as invalid")
    public void theGraphShouldBeRejectedAsInvalid() {
        LOGGER.info(() -> "Checking validation rejection: exception=" + validationException);
        assertNotNull(validationException, "Validation should have rejected the graph");
    }

    @Then("an error should be raised indicating negative weight is not allowed")
    public void anErrorShouldBeRaisedIndicatingNegativeWeightIsNotAllowed() {
        LOGGER.info(() -> "Checking negative-weight validation message: " + validationException);
        assertNotNull(validationException);
        assertTrue(validationException.getMessage().contains("negative weight"),
                "Expected a negative-weight validation error, but got: " + validationException.getMessage());
    }

    private List<Integer> parsePath(String pathText) {
        String normalized = pathText.replace("[", "").replace("]", "").trim();
        if (normalized.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(normalized.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .map(Integer::parseInt)
                .toList();
    }
}
