package practice.bipartite.bdd;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import practice.graph.BipartiteChecker;
import practice.graph.Graph;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BipartiteCheckerSteps {

    private Graph graph;
    private boolean result;

    @Given("a graph whose vertex count is {int}")
    public void aGraphWithVertices(int vertices) {
        graph = new Graph(vertices);
    }

    @Given("no edges")
    public void noEdges() {
        // no-op
    }

    @Given("edges")
    public void edges(DataTable dataTable) {
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
    }

    @Then("the result should be true")
    public void theResultShouldBeTrue() {
        assertEquals(true, result);
    }

    @Then("the result should be false")
    public void theResultShouldBeFalse() {
        assertEquals(false, result);
    }
}