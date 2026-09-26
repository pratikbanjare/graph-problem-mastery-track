package chapter.topic_06;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import practice.graph.Graph;
import practice.graph.TopologicalSortAlgorithm;
import practice.model.Edge;
import practice.model.GraphType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;


public class TopologicalSortSteps {

    private static final Logger LOGGER = Logger.getLogger(TopologicalSortSteps.class.getName());
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

        this.graph = new Graph(maxVertex, GraphType.DIRECTED);
        this.result = null;
        this.thrownException = null;
        LOGGER.info("Created directed graph with vertices 1 through " + maxVertex);
    }

    @And("the following edges:")
    public void theFollowingEdges(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : rows) {
            int from = Integer.parseInt(row.get("from"));
            int to = Integer.parseInt(row.get("to"));
            graph.addEdge(from, to);
        }
        LOGGER.info("Added graph edges: " + describeGraph());
    }

    @When("the topological sort is calculated")
    public void theTopologicalSortIsCalculated() {
        try {
            TopologicalSortAlgorithm algorithm = new TopologicalSortAlgorithm();
            this.result = algorithm.dfsTopologicalSort(graph);
            LOGGER.info("DFS topological sort result for " + describeGraph() + ": " + result);
        } catch (Exception e) {
            this.thrownException = e;
            LOGGER.info("DFS topological sort failed for " + describeGraph() + ": "
                    + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    @Then("the result should contain each vertex exactly once")
    public void theResultShouldContainEachVertexExactlyOnce() {
        String graphDescription = describeGraph();
        LOGGER.info("Checking vertex membership for graph " + graphDescription + "; result=" + result);
        Assertions.assertNotNull(result,
                "Topological sort result should not be null for graph " + graphDescription + ".");
        Set<Integer> uniqueValues = new HashSet<>(result);
        List<Integer> missingVertices = new ArrayList<>();
        for (int vertex = 1; vertex <= graph.getVertexCount(); vertex++) {
            if (!result.contains(vertex)) {
                missingVertices.add(vertex);
            }
        }
        LOGGER.info("Vertex membership details: expectedCount=" + graph.getVertexCount()
                + ", actualCount=" + result.size() + ", uniqueCount=" + uniqueValues.size()
                + ", missingVertices=" + missingVertices);
        Assertions.assertEquals(graph.getVertexCount(), result.size(),
                "Result size should equal the number of vertices for graph " + graphDescription
                        + "; result=" + result + ".");
        Assertions.assertEquals(graph.getVertexCount(), uniqueValues.size(),
                "Result should contain each vertex exactly once for graph " + graphDescription
                        + "; result=" + result + ".");

        for (int vertex = 1; vertex <= graph.getVertexCount(); vertex++) {
            Assertions.assertTrue(result.contains(vertex),
                    "Missing vertex " + vertex + " in topological order for graph "
                            + graphDescription + "; result=" + result + ".");
        }
    }

    @And("for every edge in the graph, the source vertex appears before the target vertex")
    public void forEveryEdgeTheSourceAppearsBeforeTheTarget() {
        String graphDescription = describeGraph();
        LOGGER.info("Checking edge ordering for graph " + graphDescription + "; result=" + result);
        Assertions.assertNotNull(result,
                "Topological sort result should not be null for graph " + graphDescription + ".");

        for (int source = 1; source <= graph.getVertexCount(); source++) {
            List<Integer> neighbors = graph.getEdgesOfVertex(source);

            for (int neighbor : neighbors) {
                int sourceIndex = result.indexOf(source);
                int targetIndex = result.indexOf(neighbor);
                LOGGER.info("Checking edge " + source + " -> " + neighbor + ": sourceIndex="
                        + sourceIndex + ", targetIndex=" + targetIndex);
                Assertions.assertTrue(sourceIndex < targetIndex,
                        "Edge " + source + " -> " + neighbor + " is violated: " +
                                source + " appears at index " + sourceIndex +
                                " and " + neighbor + " appears at index " + targetIndex
                                + " in result " + result + ".");
            }
        }
    }

    @And("vertex {int} should appear before both {int} and {int}")
    public void vertexShouldAppearBeforeBoth(int vertex, int firstTarget, int secondTarget) {
        LOGGER.info("Checking that vertex " + vertex + " precedes " + firstTarget + " and "
                + secondTarget + "; result=" + result);
        Assertions.assertNotNull(result, "Topological sort result should not be null.");

        Assertions.assertTrue(result.indexOf(vertex) < result.indexOf(firstTarget),
                "Vertex " + vertex + " should appear before " + firstTarget
                        + " in result " + result + ".");
        Assertions.assertTrue(result.indexOf(vertex) < result.indexOf(secondTarget),
                "Vertex " + vertex + " should appear before " + secondTarget
                        + " in result " + result + ".");
    }

    @And("the ordering should still include all disconnected components in one valid overall sequence")
    public void orderingShouldIncludeAllDisconnectedComponents() {
        theResultShouldContainEachVertexExactlyOnce();
        forEveryEdgeTheSourceAppearsBeforeTheTarget();
    }

    @Then("it should fail with an IllegalArgumentException")
    public void itShouldFailWithAnIllegalArgumentException() {
        LOGGER.info("Checking expected IllegalArgumentException; actual exception="
                + describeException());
        Assertions.assertNotNull(thrownException, "Expected an IllegalArgumentException but none was thrown.");
        Assertions.assertTrue(thrownException instanceof IllegalArgumentException,
                "Expected IllegalArgumentException but got: " + describeException());
    }

    @And("the error message should mention a cycle is detected")
    public void theErrorMessageShouldMentionACycleIsDetected() {
        LOGGER.info("Checking that exception message mentions a cycle; actual exception="
                + describeException());
        Assertions.assertNotNull(thrownException, "Expected an exception to be thrown.");
        Assertions.assertTrue(thrownException.getMessage() != null &&
                        thrownException.getMessage().toLowerCase().contains("cycle"),
                "Expected exception message to mention a cycle, but got: " +
                        (thrownException.getMessage() == null ? "null" : thrownException.getMessage())
                        + " from " + describeException());
    }

    private String describeGraph() {
        if (graph == null) {
            return "<graph not initialized>";
        }
        List<String> edges = new ArrayList<>();
        for (Edge edge : graph.getEdges()) {
            edges.add(edge.getFrom() + " -> " + edge.getTo());
        }
        return "vertices=1.." + graph.getVertexCount() + ", edges=" + edges;
    }

    private String describeException() {
        if (thrownException == null) {
            return "<none>";
        }
        return thrownException.getClass().getSimpleName() + ": " + thrownException.getMessage();
    }
}