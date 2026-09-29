package chapter.topic_02;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import practice.graph.ConnectedComponents;
import practice.graph.Graph;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConnectedComponentsSteps {

    private Graph graph;
    private List<List<Integer>> components;

    @Given("a graph with {int} vertices")
    public void aGraphWithVertices(int vertices) {
        graph = new Graph(vertices);
    }

    @Given("the graph has an edge between {int} and {int}")
    public void theGraphHasAnEdgeBetweenAnd(int from, int to) {
        graph.addEdge(from, to);
    }

    @When("I find the connected components")
    public void iFindTheConnectedComponents() {
        components = new ConnectedComponents().connectedComponent(graph);
    }

    @Then("the connected components should be")
    public void theConnectedComponentsShouldBe(io.cucumber.datatable.DataTable dataTable) {
        List<List<Integer>> expected = new ArrayList<>();
        for (List<String> row : dataTable.asLists()) {
            List<Integer> component = new ArrayList<>();
            for (String cell : row) {
                if (cell !=null &&!cell.isBlank()) {
                    component.add(Integer.parseInt(cell));
                }
            }
            expected.add(component);
        }
        String diagnostic = "Connected-components mismatch. Expected: " + expected
                + "; actual: " + components
                + ". Check that every vertex is scanned and marked visited when enqueued.";
        if (!expected.equals(components)) {
            System.err.println(diagnostic);
        }
        assertEquals(expected, components, diagnostic);
    }
}
