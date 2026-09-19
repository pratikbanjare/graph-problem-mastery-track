package practice.path.bellmanford;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import practice.exception.GraphException;
import practice.graph.Graph;
import practice.model.GraphType;
import practice.path.BellmanFord;
import practice.path.IShortestPath;

import java.util.List;
import java.util.Map;

public class BellmanFordStepDefinitions {

    private Graph graph;
    private int source;
    private int[] actualDistances;
    private GraphException thrownException;

    @Given("a directed graph with {int} vertices")
    public void a_directed_graph_with_vertices(int vertexCount) {
        graph = new Graph(vertexCount, GraphType.DIRECTED);
        this.source = 1;
    }

    @Given("a directed graph with {int} vertices and source vertex {int}")
    public void a_directed_graph_with_vertices_and_source(int vertexCount, int sourceVertex) {
        graph = new Graph(vertexCount, GraphType.DIRECTED);
        this.source = sourceVertex;
    }

    @Given("a directed graph with {int} vertex")
    public void a_directed_graph_with_single_vertex(int vertexCount) {
        graph = new Graph(vertexCount, GraphType.DIRECTED);
        this.source = 1;
    }

    @Given("the following weighted edges:")
    public void the_following_weighted_edges(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : rows) {
            int from = Integer.parseInt(row.get("from"));
            int to = Integer.parseInt(row.get("to"));
            int weight = Integer.parseInt(row.get("weight"));
            graph.addEdge(from, to, weight);
        }
    }

    @Given("there are no weighted edges in the graph")
    public void no_weighted_edges_in_the_graph() {
        // Intentionally left empty because the graph is already created with zero edges.
    }

    @When("I compute the shortest path from source vertex {int}")
    public void i_compute_the_shortest_path_from_source_vertex(int sourceVertex) {
        this.source = sourceVertex;
        IShortestPath shortestPath = new BellmanFord();

        try {
            actualDistances = shortestPath.shortestPath(graph, source);
            thrownException = null;
        } catch (GraphException e) {
            thrownException = e;
            actualDistances = null;
        }
    }

    @Then("the distances should be:")
    public void the_distances_should_be(DataTable dataTable) {
        Assertions.assertNotNull(actualDistances, "Shortest path calculation threw an exception unexpectedly.");

        List<Map<String, String>> expectedRows = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : expectedRows) {
            int vertex = Integer.parseInt(row.get("vertex"));
            String expectedValue = row.get("distance");

            int expectedDistance = parseDistanceValue(expectedValue);

            Assertions.assertEquals(expectedDistance, actualDistances[vertex],
                    "Distance mismatch for vertex " + vertex);
        }
    }

    @Then("no exception should be thrown")
    public void no_exception_should_be_thrown() {
        Assertions.assertNull(thrownException, "An exception was thrown unexpectedly.");
    }

    @Then("a GraphException should be thrown")
    public void a_graph_exception_should_be_thrown() {
        Assertions.assertNotNull(thrownException, "Expected GraphException but none was thrown.");
    }

    @And("the exception message should be {string}")
    public void the_exception_message_should_be(String expectedMessage) {
        Assertions.assertNotNull(thrownException, "No exception was thrown to verify.");
        Assertions.assertEquals(expectedMessage, thrownException.getMessage());
    }

    private int parseDistanceValue(String value) {
        if ("Integer.MAX_VALUE".equals(value)) {
            return Integer.MAX_VALUE;
        }
        return Integer.parseInt(value);
    }
}