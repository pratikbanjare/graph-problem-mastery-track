package chapter.topic_04;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import practice.graph.BipartiteChecker;
import practice.graph.Graph;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BipartiteCheckerSteps {

    private static final Logger LOGGER = Logger.getLogger(BipartiteCheckerSteps.class.getName());

    private Graph graph;
    private boolean result;

    @Given("a graph whose vertex count is {int}")
    public void aGraphWithVertices(int vertices) {
        graph = new Graph(vertices);
        LOGGER.info(() -> "Created an undirected graph with " + vertices + " vertices");
    }

    @Given("no edges")
    public void noEdges() {
        // no-op
    }

    @Given("edges")
    public void edges(DataTable dataTable) {
        LOGGER.info(() -> "Adding graph edges from table: " + dataTable.asLists());
        for (java.util.List<String> row : dataTable.asLists()) {
            if (row.size() < 2) {
                continue;
            }
            String fromCell = row.get(0);
            String toCell = row.get(1);
            if ("from".equalsIgnoreCase(fromCell) || "to".equalsIgnoreCase(fromCell)) {
                continue;
            }
            graph.addEdge(Integer.parseInt(fromCell), Integer.parseInt(toCell));
        }
    }

    @When("I check whether the graph is bipartite")
    public void iCheckWhetherTheGraphIsBipartite() {
        result = new BipartiteChecker().isBipartite(graph);
        LOGGER.info(() -> "Bipartite check completed with result=" + result);
    }

    @Then("the result should be true")
    public void theResultShouldBeTrue() {
        logAssertion(true);
        assertEquals(true, result, "Expected the graph to be bipartite, but result was " + result);
    }

    @Then("the result should be false")
    public void theResultShouldBeFalse() {
        logAssertion(false);
        assertEquals(false, result, "Expected the graph not to be bipartite, but result was " + result);
    }

    private void logAssertion(boolean expected) {
        LOGGER.info(() -> "Checking bipartite result: expected=" + expected + ", actual=" + result
                + ", vertices=" + graph.getVertexCount() + ", edges=" + graph.getEdges().size());
    }
}