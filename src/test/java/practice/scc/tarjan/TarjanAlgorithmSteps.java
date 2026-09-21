package practice.scc.tarjan;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import practice.graph.Graph;
import practice.model.GraphType;
import practice.scc.TarjanAlgorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TarjanAlgorithmSteps {

    private Graph graph;
    private List<List<Integer>> components;

    @Given("a directed graph with the following edges:")
    public void aDirectedGraphWithTheFollowingEdges(DataTable dataTable) {
        graph = createGraphFromEdges(dataTable);
    }

    @When("I find the strongly connected components")
    public void iFindTheStronglyConnectedComponents() {
        components = new TarjanAlgorithm().stronglyConnectedComponents(graph);
    }

    @Then("the strongly connected components should be:")
    public void theStronglyConnectedComponentsShouldBe(DataTable dataTable) {
        assertEquals(normalize(readExpectedComponents(dataTable)), normalize(components));
    }

    private Graph createGraphFromEdges(DataTable dataTable) {
        List<List<String>> rows = dataTable.asLists();
        List<List<String>> edgeRows = rows.subList(1, rows.size());
        int vertices = edgeRows.stream()
                .mapToInt(row -> Math.max(Integer.parseInt(row.get(0)), Integer.parseInt(row.get(1))))
                .max()
                .orElseThrow(() -> new IllegalArgumentException("At least one edge is required"));

        Graph createdGraph = new Graph(vertices, GraphType.DIRECTED);
        for (List<String> row : edgeRows) {
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
