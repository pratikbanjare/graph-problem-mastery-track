package practice.path.dag;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import practice.graph.Graph;
import practice.model.GraphType;
import practice.path.DagShortestPath;

import java.util.List;
import java.util.Map;

public class DagShortestPathStepDefinitions {

    private Graph graph;
    private int source;
    private int[] actualDistances;

    @Given("a directed weighted graph with {int} vertices")
    public void a_directed_weighted_graph_with_vertices(int vertexCount) {
        graph = new Graph(vertexCount, GraphType.DIRECTED);
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

    @Given("the source vertex is {int}")
    public void the_source_vertex_is(int sourceVertex) {
        source = sourceVertex;
    }

    @When("I calculate the DAG shortest paths")
    public void i_calculate_the_dag_shortest_paths() {
        actualDistances = new DagShortestPath().shortestPath(graph, source);
    }

    @Then("the shortest distances should be:")
    public void the_shortest_distances_should_be(DataTable dataTable) {
        Assertions.assertNotNull(actualDistances, "Shortest distances were not calculated.");

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            int vertex = Integer.parseInt(row.get("vertex"));
            int expectedDistance = parseDistance(row.get("distance"));

            Assertions.assertEquals(expectedDistance, actualDistances[vertex],
                    "Distance mismatch for vertex " + vertex);
        }
    }

    @Then("the distance for vertex {int} should be {int}")
    public void the_distance_for_vertex_should_be(int vertex, int expectedDistance) {
        Assertions.assertNotNull(actualDistances, "Shortest distances were not calculated.");
        Assertions.assertEquals(expectedDistance, actualDistances[vertex],
                "Distance mismatch for vertex " + vertex);
    }

    private int parseDistance(String value) {
        if ("unreachable".equalsIgnoreCase(value)
                || "Integer.MAX_VALUE".equals(value)) {
            return Integer.MAX_VALUE;
        }
        return Integer.parseInt(value);
    }
}
