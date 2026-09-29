package chapter.topic_10;

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
        Assertions.assertNotNull(distances,
                "Expected a distance matrix, but Floyd-Warshall threw: " + exceptionMessage());
        int actual = distances[from][to];
        System.out.printf("Checking distance %d -> %d: expected=%d, actual=%d, matrix=%s%n",
                from, to, expected, actual, formatDistances());
        Assertions.assertEquals(expected, actual,
                "Distance mismatch for " + from + " -> " + to + ". Matrix=" + formatDistances());
    }

    @Then("the shortest distance from {int} to {int} should be Integer.MAX_VALUE")
    public void theShortestDistanceFromToShouldBeInfinite(int from, int to) {
        Assertions.assertNotNull(distances,
                "Expected a distance matrix, but Floyd-Warshall threw: " + exceptionMessage());
        int actual = distances[from][to];
        System.out.printf("Checking unreachable distance %d -> %d: expected=Integer.MAX_VALUE, actual=%d, matrix=%s%n",
                from, to, actual, formatDistances());
        Assertions.assertEquals(Integer.MAX_VALUE, actual,
                "Expected " + from + " -> " + to + " to remain unreachable. Matrix=" + formatDistances());
    }

    @Then("a GraphException should be thrown with message containing {string}")
    public void aGraphExceptionShouldBeThrownWithMessageContaining(String expectedMessage) {
        System.out.printf("Checking exception message: expected to contain=\"%s\", actual=\"%s\"%n",
                expectedMessage, exceptionMessage());
        Assertions.assertNotNull(thrownException, "Expected GraphException but none was thrown.");
        Assertions.assertTrue(thrownException.getMessage().contains(expectedMessage),
                "Exception message did not contain expected text. Actual=" + thrownException.getMessage());
    }

    private String exceptionMessage() {
        return thrownException == null ? "none" : thrownException.getMessage();
    }

    private String formatDistances() {
        if (distances == null) {
            return "null";
        }
        StringBuilder formatted = new StringBuilder("[");
        for (int from = 1; from < distances.length; from++) {
            if (from > 1) {
                formatted.append("; ");
            }
            formatted.append(from).append("=");
            for (int to = 1; to < distances[from].length; to++) {
                if (to > 1) {
                    formatted.append(",");
                }
                formatted.append(to).append(":").append(distances[from][to]);
            }
        }
        return formatted.append("]").toString();
    }
}
