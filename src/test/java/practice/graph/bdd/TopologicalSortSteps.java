package practice.graph.bdd;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import practice.graph.Graph;
import practice.graph.TopologicalSortAlgorithm;

import java.util.*;


public class TopologicalSortSteps {

    private Graph graph;
    private List<Integer> result;
    private Throwable thrownException;

    @Given("a directed graph with vertices {int}, {int}, {int}, {int}")
    public void aDirectedGraphWithVertices(int v1, int v2, int v3, int v4) {
        setGraphFromVertices(Arrays.asList(v1, v2, v3, v4));
    }

    @Given("a directed graph with vertices {int}, {int}, {int}, {int}, {int}")
    public void aDirectedGraphWithVertices5(int v1, int v2, int v3, int v4, int v5) {
        setGraphFromVertices(Arrays.asList(v1, v2, v3, v4, v5));
    }

    @Given("a directed graph with vertices {int}, {int}, {int}, {int}, {int}, {int}")
    public void aDirectedGraphWithVertices6(int v1, int v2, int v3, int v4, int v5, int v6) {
        setGraphFromVertices(Arrays.asList(v1, v2, v3, v4, v5, v6));
    }

    @Given("a directed graph with vertices {int}, {int}, {int}")
    public void aDirectedGraphWithVertices3(int v1, int v2, int v3) {
        setGraphFromVertices(Arrays.asList(v1, v2, v3));
    }

    @Given("a directed graph with vertices {int}")
    public void aDirectedGraphWithVertex(int v1) {
        setGraphFromVertices(Collections.singletonList(v1));
    }

    private void setGraphFromVertices(List<Integer> vertices) {
        int maxVertex = vertices.stream().max(Integer::compareTo).orElse(0);

        this.graph = new Graph(maxVertex, practice.model.GraphType.DIRECTED);
        this.result = null;
        this.thrownException = null;
    }

    @And("the following edges:")
    public void theFollowingEdges(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : rows) {
            int from = Integer.parseInt(row.get("from"));
            int to = Integer.parseInt(row.get("to"));
            graph.addEdge(from, to);
        }
    }

    @When("the topological sort is calculated")
    public void theTopologicalSortIsCalculated() {
        try {
            TopologicalSortAlgorithm algorithm = new TopologicalSortAlgorithm();
            this.result = algorithm.dfsTopologicalSort(graph);
        } catch (Exception e) {
            this.thrownException = e;
        }
    }

    @Then("the result should contain each vertex exactly once")
    public void theResultShouldContainEachVertexExactlyOnce() {
        Assertions.assertNotNull(result, "Topological sort result should not be null.");
        Assertions.assertEquals(graph.getVertexCount(), result.size(),
                "Result size should equal the number of vertices.");

        Set<Integer> uniqueValues = new HashSet<>(result);
        Assertions.assertEquals(graph.getVertexCount(), uniqueValues.size(),
                "Result should contain each vertex exactly once.");

        for (int vertex = 1; vertex <= graph.getVertexCount(); vertex++) {
            Assertions.assertTrue(result.contains(vertex),
                    "Missing vertex " + vertex + " in topological order.");
        }
    }

    @And("for every edge in the graph, the source vertex appears before the target vertex")
    public void forEveryEdgeTheSourceAppearsBeforeTheTarget() {
        Assertions.assertNotNull(result, "Topological sort result should not be null.");

        for (int source = 1; source <= graph.getVertexCount(); source++) {
            List<Integer> neighbors = graph.getEdgesOfVertex(source );

            for (int neighbor : neighbors) {
                int target = neighbor;

                int sourceIndex = result.indexOf(source);
                int targetIndex = result.indexOf(target);

                Assertions.assertTrue(sourceIndex < targetIndex,
                        "Edge " + source + " -> " + target + " is violated: " +
                                source + " appears at index " + sourceIndex +
                                " and " + target + " appears at index " + targetIndex);
            }
        }
    }

    @And("vertex {int} should appear before both {int} and {int}")
    public void vertexShouldAppearBeforeBoth(int vertex, int firstTarget, int secondTarget) {
        Assertions.assertNotNull(result, "Topological sort result should not be null.");

        Assertions.assertTrue(result.indexOf(vertex) < result.indexOf(firstTarget),
                "Vertex " + vertex + " should appear before " + firstTarget);
        Assertions.assertTrue(result.indexOf(vertex) < result.indexOf(secondTarget),
                "Vertex " + vertex + " should appear before " + secondTarget);
    }

    @And("the ordering should still include all disconnected components in one valid overall sequence")
    public void orderingShouldIncludeAllDisconnectedComponents() {
        theResultShouldContainEachVertexExactlyOnce();
        forEveryEdgeTheSourceAppearsBeforeTheTarget();
    }

    @Then("it should fail with an IllegalArgumentException")
    public void itShouldFailWithAnIllegalArgumentException() {
        Assertions.assertNotNull(thrownException, "Expected an IllegalArgumentException but none was thrown.");
        Assertions.assertTrue(thrownException instanceof IllegalArgumentException,
                "Expected IllegalArgumentException but got: " + thrownException.getClass().getSimpleName());
    }

    @And("the error message should mention a cycle is detected")
    public void theErrorMessageShouldMentionACycleIsDetected() {
        Assertions.assertNotNull(thrownException, "Expected an exception to be thrown.");
        Assertions.assertTrue(thrownException.getMessage() != null &&
                        thrownException.getMessage().toLowerCase().contains("cycle"),
                "Expected exception message to mention a cycle, but got: " +
                        (thrownException.getMessage() == null ? "null" : thrownException.getMessage()));
    }
}