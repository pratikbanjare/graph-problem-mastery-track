package practice.scc.kosaraju;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import practice.graph.Graph;
import practice.model.GraphType;
import practice.scc.KosarajuAlgorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class KosarajuAlgorithmSteps {

    private Graph graph;
    private List<List<Integer>> components;
    private Exception exception;

    @Given("a directed graph with the following edges:")
    public void aDirectedGraphWithTheFollowingEdges(DataTable dataTable) {
        graph = createGraphFromEdges(dataTable, GraphType.DIRECTED);
    }

    @Given("a directed graph with {int} vertices and no edges")
    public void aDirectedGraphWithVerticesAndNoEdges(int vertices) {
        graph = new Graph(vertices, GraphType.DIRECTED);
    }

    @Given("an undirected graph with the following edges:")
    public void anUndirectedGraphWithTheFollowingEdges(DataTable dataTable) {
        graph = createGraphFromEdges(dataTable, GraphType.UNDIRECTED);
    }

    @When("I find the strongly connected components")
    public void iFindTheStronglyConnectedComponents() {
        exception = null;
        components = new KosarajuAlgorithm().stronglyConnectedAlgorithm(graph);
    }

    @When("I attempt to find the strongly connected components")
    public void iAttemptToFindTheStronglyConnectedComponents() {
        try {
            components = new KosarajuAlgorithm().stronglyConnectedAlgorithm(graph);
        } catch (Exception caughtException) {
            exception = caughtException;
        }
    }

    @Then("the strongly connected components should be:")
    public void theStronglyConnectedComponentsShouldBe(DataTable dataTable) {
        assertEquals(normalize(readExpectedComponents(dataTable)), normalize(components));
    }

    @Then("an IllegalArgumentException should be thrown")
    public void anIllegalArgumentExceptionShouldBeThrown() {
        assertInstanceOf(IllegalArgumentException.class, exception);
    }

    @Then("the exception message should be:")
    public void theExceptionMessageShouldBe(String expectedMessage) {
        assertEquals(expectedMessage, exception.getMessage());
    }

    private Graph createGraphFromEdges(DataTable dataTable, GraphType graphType) {
        List<List<String>> rows = dataTable.asLists();
        int vertices = rows.stream()
                .skip(1)
                .mapToInt(row -> Math.max(Integer.parseInt(row.get(0)), Integer.parseInt(row.get(1))))
                .max()
                .orElseThrow(() -> new IllegalArgumentException("At least one edge is required"));

        Graph createdGraph = new Graph(vertices, graphType);
        for (List<String> row : rows.subList(1, rows.size())) {
            createdGraph.addEdge(Integer.parseInt(row.get(0)), Integer.parseInt(row.get(1)));
        }
        return createdGraph;
    }

    private List<List<Integer>> readExpectedComponents(DataTable dataTable) {
        List<List<Integer>> expected = new ArrayList<>();
        for (List<String> row : dataTable.asLists().subList(1, dataTable.height())) {
            List<Integer> component = new ArrayList<>();
            for (String vertex : row.get(0).split(",")) {
                component.add(Integer.parseInt(vertex.trim()));
            }
            expected.add(component);
        }
        return expected;
    }

    private Set<Set<Integer>> normalize(List<List<Integer>> componentsToNormalize) {
        Set<Set<Integer>> normalized = new HashSet<>();
        for (List<Integer> component : componentsToNormalize) {
            normalized.add(new HashSet<>(component));
        }
        return normalized;
    }
}
