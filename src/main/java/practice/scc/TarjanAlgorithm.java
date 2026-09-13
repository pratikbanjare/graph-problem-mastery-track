package practice.scc;

import practice.graph.Graph;
import practice.model.GraphType;
import practice.model.WeightedEdge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class TarjanAlgorithm {

    public List<List<Integer>> stronglyConnectedComponents(Graph graph){

        if (graph.getGraphType() == GraphType.UNDIRECTED){
            throw new IllegalArgumentException("Tarjan algorithm works on undirected graph only!!!");
        }
        int[] discoveryTIme = new int[graph.getNumberOfVertex() + 1];
        int[] lowLink = new int[graph.getNumberOfVertex() + 1];
        boolean[] onStack = new boolean[graph.getNumberOfVertex() + 1];
        int[] time = {0};
        Stack<Integer> stack = new Stack<>();

        List<List<Integer>> scc = new ArrayList<>();
        Arrays.fill(discoveryTIme, -1);

        for (int vertex = 1; vertex <= graph.getNumberOfVertex(); vertex++) {
            if (discoveryTIme[vertex] == -1){
                dfs(vertex,graph, discoveryTIme, lowLink, onStack, stack, time, scc);
            }
        }

        return scc;

    }

    private void dfs(int vertex, Graph graph, int[] discoveryTIme, int[] lowLink, boolean[] onStack, Stack<Integer> stack, int[] time, List<List<Integer>> scc){
        discoveryTIme[vertex] = time[0]++;
        lowLink[vertex] = discoveryTIme[vertex];
        onStack[vertex] = true;
        stack.push(vertex);

        for (WeightedEdge weightedEdge : graph.getAdjacencyListOfVertex(vertex)) {
            if (discoveryTIme[weightedEdge.getTo()] == -1){
                dfs(weightedEdge.getTo(), graph, discoveryTIme, lowLink, onStack, stack, time, scc);
                lowLink[vertex] = Math.min(lowLink[weightedEdge.getTo()], lowLink[vertex]);
            } else if (onStack[weightedEdge.getTo()]){
                lowLink[vertex] = Math.min(lowLink[vertex], discoveryTIme[weightedEdge.getTo()]);
            }
        }

        if (lowLink[vertex] == discoveryTIme[vertex]){
            List<Integer> list = new ArrayList<>();
            while(onStack[vertex]){
                Integer release = stack.pop();
                onStack[release] = false;
                list.add(release);
            }
            scc.add(list);
        }

    }
}
