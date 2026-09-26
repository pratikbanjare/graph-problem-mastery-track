package practice.path.floydwarshall;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import practice.exception.GraphException;
import practice.graph.Graph;
import practice.model.GraphType;
import practice.path.FloydWarshall;

import java.util.List;
import java.util.Map;

public class FloydWarshallSteps {

    private Graph graph;
    private int[][] distances;
    private GraphException thrownException;

    @Given("a directed graph with {int} vertices")
    public void aDirectedGraphWithVertices(int vertexCount) {
        graph = new Graph(vertexCount, GraphType.DIRECTED);
    }

    @Given("a graph with {int} vertices")
    public void aGraphWithVertices(int vertexCount) {
        graph = new Graph(vertexCount);
    }

    @Given("the edges:")
    public void theEdges(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            graph.addEdge(
                    Integer.parseInt(row.get("from")),
                    Integer.parseInt(row.get("to")),
                    Integer.parseInt(row.get("weight"))
            );
        }
    }

    @When("I run Floyd-Warshall on the graph")
    public void iRunFloydWarshallOnTheGraph() {
        try {
            distances = new FloydWarshall().floydWarshallAlgorithm(graph);
            thrownException = null;
        } catch (GraphException e) {
            thrownException = e;
            distances = null;
        }
    }

    @Then("the shortest distance from {int} to {int} should be {int}")
    public void theShortestDistanceFromToShouldBe(int from, int to, int expected) {
        Assertions.assertNotNull(distances, "Expected distances but Floyd-Warshall threw an exception.");
        Assertions.assertEquals(expected, distances[from][to]);
    }

    @Then("a GraphException should be thrown with message containing {string}")
    public void aGraphExceptionShouldBeThrownWithMessageContaining(String expectedMessage) {
        Assertions.assertNotNull(thrownException, "Expected GraphException but none was thrown.");
        Assertions.assertTrue(thrownException.getMessage().contains(expectedMessage));
    }
}
