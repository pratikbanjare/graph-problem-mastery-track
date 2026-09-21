package practice.scc;

import practice.graph.Graph;
import practice.graph.GraphOperation;
import practice.model.GraphType;
import practice.model.WeightedEdge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KosarajuAlgorithm {

    public List<List<Integer>> stronglyConnectedAlgorithm(Graph graph){

        if (graph.getGraphType() == GraphType.UNDIRECTED){
            throw new IllegalArgumentException("Kosaraju's algorithm requires Directed Graph!!!");
        }

        boolean[] visited = new boolean[graph.getVertexCount() + 1];
        List<Integer> finishingOrder = new ArrayList<>();
        for (int vertex = 1; vertex <=graph.getVertexCount(); vertex++) {
            if (!visited[vertex]){
                dfs(graph, vertex, visited, finishingOrder);
            }
        }

        Collections.reverse(finishingOrder);

        Graph reversedGraph = GraphOperation.reverse(graph);

        List<List<Integer>> sccLists = new ArrayList<>();
        visited = new boolean[reversedGraph.getVertexCount() + 1];
        for (Integer vertex : finishingOrder){
            if (!visited[vertex]){
                List<Integer> scc = new ArrayList<>();
                dfs(reversedGraph, vertex, visited, scc);
                sccLists.add(scc);
            }
        }

        return sccLists;
    }

    private void dfs(Graph graph, int vertex, boolean[] visited, List<Integer> finishingOrder) {
        visited[vertex] = true;

        for (WeightedEdge edge : graph.getWeightedEdgesOfVertex(vertex)){
            if (visited[edge.getTo()]){
                continue;
            }
            dfs(graph, edge.getTo(), visited, finishingOrder);
        }
        finishingOrder.add(vertex);
    }
}
