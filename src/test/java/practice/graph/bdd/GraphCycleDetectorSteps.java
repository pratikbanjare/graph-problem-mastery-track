package practice.graph.bdd;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import practice.CommonGraphHelper;
import practice.graph.Graph;
import practice.graph.GraphCycleDetector;
import practice.model.GraphType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphCycleDetectorSteps {

    private final CommonGraphHelper helper = new CommonGraphHelper();
    private Graph graph;
    private boolean result;

    @Given("a graph whose vertex count is {int}")
    public void aGraphWhoseVertexCountIs(int vertices) {
        graph = helper.createGraph(vertices, GraphType.DIRECTED);
    }

    @Given("edges")
    public void edges(DataTable dataTable) {
        helper.addEdges(graph, dataTable);
    }

    @When("I check whether the graph has a cycle")
    public void iCheckWhetherTheGraphHasACycle() {
        result = new GraphCycleDetector().cycleDetector(graph);
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