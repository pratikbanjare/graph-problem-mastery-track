package chapter.topic_11;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import practice.exception.GraphException;
import practice.graph.Graph;
import practice.model.GraphType;
import practice.path.DagShortestPath;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DagShortestPathStepDefinitions {

    private static final Logger LOGGER =
            Logger.getLogger(DagShortestPathStepDefinitions.class.getName());

    private Graph graph;
    private int source;
    private int[] actualDistances;
    private GraphException thrownException;

    @Given("a directed weighted graph with {int} vertices")
    public void a_directed_weighted_graph_with_vertices(int vertexCount) {
        graph = new Graph(vertexCount, GraphType.DIRECTED);
        LOGGER.info(() -> "Created directed weighted graph with " + vertexCount + " vertices.");
    }

    @Given("the following weighted edges:")
    public void the_following_weighted_edges(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : rows) {
            int from = Integer.parseInt(row.get("from"));
            int to = Integer.parseInt(row.get("to"));
            int weight = Integer.parseInt(row.get("weight"));
            graph.addEdge(from, to, weight);
            LOGGER.info(() -> "Added edge " + from + " -> " + to + " (weight " + weight + ").");
        }
    }

    @Given("the source vertex is {int}")
    public void the_source_vertex_is(int sourceVertex) {
        source = sourceVertex;
        LOGGER.info(() -> "Selected source vertex " + source + ".");
    }

    @When("I calculate the DAG shortest paths")
    public void i_calculate_the_dag_shortest_paths() {
        LOGGER.info(() -> "Calculating DAG shortest paths from source " + source + ".");
        try {
            actualDistances = new DagShortestPath().shortestPath(graph, source);
            thrownException = null;
            LOGGER.info(() -> "Calculation completed. Distances: "
                    + formatDistances(actualDistances));
        } catch (GraphException exception) {
            actualDistances = null;
            thrownException = exception;
            LOGGER.warning(() -> "Calculation failed: " + exception.getMessage());
        }
    }

    @Then("the shortest distances should be:")
    public void the_shortest_distances_should_be(DataTable dataTable) {
        Assertions.assertNotNull(actualDistances,
                "Shortest distances were not calculated. Exception: " + exceptionMessage());

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            int vertex = Integer.parseInt(row.get("vertex"));
            int expectedDistance = parseDistance(row.get("distance"));
            int actualDistance = actualDistances[vertex];

            LOGGER.info(() -> "Checking vertex " + vertex + ": expected="
                    + expectedDistance + ", actual=" + actualDistance
                    + ", all distances=" + formatDistances(actualDistances));
            Assertions.assertEquals(expectedDistance, actualDistance,
                    "Distance mismatch for vertex " + vertex
                            + ". Expected=" + expectedDistance
                            + ", actual=" + actualDistance
                            + ", all distances=" + formatDistances(actualDistances));
        }
    }

    @Then("the distance for vertex {int} should be {int}")
    public void the_distance_for_vertex_should_be(int vertex, int expectedDistance) {
        Assertions.assertNotNull(actualDistances,
                "Shortest distances were not calculated. Exception: " + exceptionMessage());
        int actualDistance = actualDistances[vertex];
        LOGGER.info(() -> "Checking vertex " + vertex + ": expected="
                + expectedDistance + ", actual=" + actualDistance
                + ", all distances=" + formatDistances(actualDistances));
        Assertions.assertEquals(expectedDistance, actualDistance,
                "Distance mismatch for vertex " + vertex
                        + ". Expected=" + expectedDistance
                        + ", actual=" + actualDistance
                        + ", all distances=" + formatDistances(actualDistances));
    }

    @Then("a cycle error should be reported")
    public void a_cycle_error_should_be_reported() {
        LOGGER.info(() -> "Checking cycle rejection. Exception: " + exceptionMessage());
        Assertions.assertNotNull(thrownException,
                "Expected a cycle error, but no exception was thrown.");
        Assertions.assertTrue(thrownException.getMessage().contains("Cycle detected"),
                "Expected a cycle error, but received: " + thrownException.getMessage());
    }

    private int parseDistance(String value) {
        if ("unreachable".equalsIgnoreCase(value)
                || "Integer.MAX_VALUE".equals(value)) {
            return Integer.MAX_VALUE;
        }
        return Integer.parseInt(value);
    }

    private String exceptionMessage() {
        return thrownException == null ? "none" : thrownException.getMessage();
    }

    private String formatDistances(int[] distances) {
        return distances == null ? "null" : Arrays.toString(distances);
    }
}
