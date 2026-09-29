package chapter.topic_05;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import practice.graph.Graph;
import practice.graph.GraphCycleDetector;
import practice.model.Edge;
import practice.model.GraphType;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphCycleDetectorSteps {

    private static final Logger LOGGER = Logger.getLogger(GraphCycleDetectorSteps.class.getName());
    private Graph graph;
    private boolean result;

    @Given("a graph whose vertex count is {int}")
    public void aGraphWhoseVertexCountIs(int vertices) {
        graph = new Graph(vertices, GraphType.DIRECTED);
        LOGGER.info("Created directed graph with " + vertices + " vertices");
    }

    @Given("edges")
    public void edges(DataTable dataTable) {
        for (java.util.List<String> row : dataTable.asLists()) {
            if (row.size() < 2 || "from".equalsIgnoreCase(row.get(0))
                    || "to".equalsIgnoreCase(row.get(0))) {
                continue;
            }
            graph.addEdge(Integer.parseInt(row.get(0)), Integer.parseInt(row.get(1)));
        }
        LOGGER.info("Added graph edges: " + describeGraph());
    }

    @When("I check whether the graph has a cycle")
    public void iCheckWhetherTheGraphHasACycle() {
        result = new GraphCycleDetector().cycleDetector(graph);
        LOGGER.info("Cycle detection result for graph " + describeGraph() + ": " + result);
    }

    @Then("the result should be true")
    public void theResultShouldBeTrue() {
        assertEquals(true, result,
                "Expected a cycle, but detector returned false for graph " + describeGraph());
    }

    @Then("the result should be false")
    public void theResultShouldBeFalse() {
        assertEquals(false, result,
                "Expected no cycle, but detector returned true for graph " + describeGraph());
    }

    private String describeGraph() {
        return graph.getEdges().stream()
                .map(this::describeEdge)
                .toList()
                .toString();
    }

    private String describeEdge(Edge edge) {
        return edge.getFrom() + " -> " + edge.getTo();
    }
}