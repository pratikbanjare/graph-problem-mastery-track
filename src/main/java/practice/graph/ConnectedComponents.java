package practice.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ConnectedComponents {

    List<List<Integer>> connectedComponent(Graph graph) {
        List<List<Integer>> components = new ArrayList<>();
        boolean[] visited = new boolean[graph.getVerticesCount()];
        for (int i = 1; i <= graph.getVerticesCount(); ++i) {
            if (visited[i - 1]) {
                continue;
            }
            components.add(bfsInternal(graph, i - 1, visited));
        }
        return components;
    }

    List<Integer> bfs(Graph graph, int node, boolean[] visited) {
        return bfsInternal(graph, node - 1, visited);
    }

    List<Integer> bfsInternal(Graph graph, int node, boolean[] visited) {
        Queue<Integer> queue = new ArrayDeque<>();
        List<Integer> component = new ArrayList<>();

        visited[node] = true;
        queue.add(node);
        while (!queue.isEmpty()) {
            int current = queue.poll();
            component.add(graph.getPublicVertex(current));
            for (int neighbor : graph.getNeighbors(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                }
            }
        }
        return component;
    }
}
