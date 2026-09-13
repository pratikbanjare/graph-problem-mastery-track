package practice.graph;

import java.util.ArrayDeque;
import java.util.Queue;

public class ConnectedComponents {

    private final int vertices;

    ConnectedComponents(int vertices) {
        this.vertices = vertices;
    }

    void connectedComponent(Graph graph) {
        boolean[] visited = new boolean[this.vertices];
        for (int i = 1; i <= this.vertices; ++i) {
            if (visited[i - 1]) {
                continue;
            }
            System.out.println();
            bfsInternal(graph, i - 1, visited);
        }
    }

    void bfs(Graph graph, int node, boolean[] visited) {
        bfsInternal(graph, node - 1, visited);
    }

    void bfsInternal(Graph graph, int node, boolean[] visited) {
        Queue<Integer> queue = new ArrayDeque<>();

        visited[node] = true;
        queue.add(node);
        while (!queue.isEmpty()) {
            int current = queue.poll();
            System.out.print(graph.getPublicVertex(current) + " ");
            for (int neighbor : graph.getNeighbors(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                }
            }
        }
    }
}
