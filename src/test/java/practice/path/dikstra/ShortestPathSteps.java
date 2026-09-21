package practice.path.dikstra;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import practice.graph.Graph;
import practice.model.GraphType;
import practice.path.ShortestPath;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShortestPathSteps {

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
    }

    @Given("edges:")
    public void edges(DataTable dataTable) {
        for (var row : dataTable.asMaps()) {
            int from = Integer.parseInt(row.get("from"));
            int to = Integer.parseInt(row.get("to"));
            int weight = Integer.parseInt(row.get("weight"));
            graph.addEdge(from, to, weight);
        }
    }

    @Given("no path exists from vertex {int} to vertex {int}")
    public void noPathExistsFromVertexToVertex(int from, int to) {
        this.source = from;
        this.graph = new Graph(Math.max(from, to));
    }

    @When("the shortest distances are computed from source {int}")
    public void theShortestDistancesAreComputedFromSource(int sourceVertex) {
        this.source = sourceVertex;
        this.distances = shortestPath.djikstra(graph, sourceVertex);
    }

    @When("the shortest path is requested from source {int} to target {int}")
    public void theShortestPathIsRequestedFromSourceToTarget(int sourceVertex, int targetVertex) {
        this.source = sourceVertex;
        this.shortestPathList = shortestPath.djikstraPath(graph, sourceVertex, targetVertex);
        this.distances = shortestPath.djikstra(graph, sourceVertex);
    }

    @When("validation is performed for Dijkstra's algorithm")
    public void validationIsPerformedForDijkstrasAlgorithm() {
        this.validationException = null;
        try {
            shortestPath.validateGraph(graph);
        } catch (RuntimeException exception) {
            this.validationException = exception;
        }
    }

    @Then("the distance to vertex {int} should be {int}")
    public void theDistanceToVertexShouldBe(int vertex, int expectedDistance) {
        assertNotNull(graph, "Graph should be initialized before checking distances");
        assertEquals(expectedDistance, distances[vertex]);
    }

    @Then("the distance to vertex {int} should be infinity")
    public void theDistanceToVertexShouldBeInfinity(int vertex) {
        assertNotNull(graph, "Graph should be initialized before checking distances");
        assertEquals(Integer.MAX_VALUE, distances[vertex]);
    }

    @Then("^the returned path should be \\[(.*)\\]$")
    public void theReturnedPathShouldBe(String expectedPath) {
        assertEquals(parsePath(expectedPath), shortestPathList);
    }

    @Then("^the path should be \\[(.*)\\]$")
    public void thePathShouldBeList(String expectedPath) {
        assertEquals(parsePath(expectedPath), shortestPathList);
    }

    @Then("the path should be empty")
    public void thePathShouldBeEmpty() {
        assertTrue(shortestPathList.isEmpty());
    }

    @Then("the distance to the source should be {int}")
    public void theDistanceToTheSourceShouldBe(int expectedDistance) {
        assertEquals(expectedDistance, distances[source]);
    }

    @Then("the graph should be rejected as invalid")
    public void theGraphShouldBeRejectedAsInvalid() {
        assertNotNull(validationException, "Validation should have rejected the graph");
    }

    @Then("an error should be raised indicating negative weight is not allowed")
    public void anErrorShouldBeRaisedIndicatingNegativeWeightIsNotAllowed() {
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