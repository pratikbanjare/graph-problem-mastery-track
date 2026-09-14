package practice.path.bdd;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import practice.graph.Graph;
import practice.path.BFS;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BfsSteps {

    private Graph graph;
    private List<Integer> shortestPath;
    private int shortestDistance;

    @Given("a path graph with {int} vertices")
    public void aPathGraphWithVertices(int vertices) {
        graph = new Graph(vertices);
    }

    @Given("the path graph has an edge between {int} and {int}")
    public void thePathGraphHasAnEdgeBetweenAnd(int from, int to) {
        graph.addEdge(from, to);
    }

    @When("I find the shortest BFS path from {int} to {int}")
    public void iFindTheShortestBfsPathFromTo(int source, int target) {
        shortestPath = new BFS().shortestDistancePath(graph, source, target);
    }

    @When("I measure the BFS shortest distance from {int} to {int}")
    public void iMeasureTheBfsShortestDistanceFromTo(int source, int target) {
        shortestDistance = new BFS().shortestDistance(graph, source, target);
    }

    @Then("the BFS path should be")
    public void theBfsPathShouldBe(DataTable dataTable) {
        List<Integer> expectedPath = dataTable.asLists().get(0).stream()
                .filter(cell -> cell != null && !cell.isBlank())
                .map(Integer::parseInt)
                .toList();
        assertEquals(expectedPath, shortestPath);
    }

    @Then("the BFS path should form a valid traversal from {int} to {int}")
    public void theBfsPathShouldFormAValidTraversalFromTo(int source, int target) {
        if (shortestPath.isEmpty()) {
            return;
        }

        assertEquals(source, shortestPath.get(0));
        assertEquals(target, shortestPath.get(shortestPath.size() - 1));
        for (Integer vertex : shortestPath) {
            assertFalse(vertex == null);
        }
    }

    @Then("the BFS path should use {int} edges")
    public void theBfsPathShouldUseEdges(int expectedEdges) {
        int actualEdges = shortestPath.isEmpty() ? 0 : shortestPath.size() - 1;
        assertEquals(expectedEdges, actualEdges);
    }

    @Then("the BFS path should be empty")
    public void theBfsPathShouldBeEmpty() {
        assertEquals(List.of(), shortestPath);
    }

    @Then("the BFS distance should be {int}")
    public void theBfsDistanceShouldBe(int expectedDistance) {
        assertEquals(expectedDistance, shortestDistance);
    }
}
