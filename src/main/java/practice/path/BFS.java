package practice.path;

import practice.graph.Graph;

import java.util.*;

public class BFS {

    public List<Integer> shortestDistancePath(Graph graph, int source, int target){

        int[] parent =  shortestDistancePathInternal(graph,source-1, target-1);

        return tracePath(graph, source-1, target-1, parent);

    }

    public int[] shortestDistancePathInternal(Graph graph, int source, int target) {
        boolean[] visited = new boolean[graph.getVerticesCount()];
        int[] parent = new int[graph.getVerticesCount()];
        Arrays.fill(parent, -1);
        Queue<Integer> queue = new ArrayDeque<>();

        if (source == target) {
            return parent;
        }

        queue.add(source);
        visited[source] = true;

        while(!queue.isEmpty()){
            int current = queue.poll();
            for (int neighbor : graph.getNeighbors(current)){
                if( !visited[neighbor]){
                    parent[neighbor] = current;
                    visited[neighbor] = true;
                    if (neighbor == target){
                        return parent;
                    }

                    queue.add(neighbor);
                }
            }

        }
        return parent;

    }

    private List<Integer> tracePath (Graph graph, int source, int target, int[] parent){

        if (source == target){
            return List.of(graph.getPublicVertex(source));
        }
        if (parent[target] == -1) {
            return Collections.emptyList();
        }
        int current = target;
        List<Integer> res = new ArrayList<>();
        while (current != -1)  {
            res.add(graph.getPublicVertex(current));
            current = parent[current];
        }
        Collections.reverse(res);
        return res;
    }

    public int shortestDistance(Graph graph, int source, int target) {
        return shortestDistanceInternal(graph, source-1, target-1);

    }

    private int shortestDistanceInternal (Graph graph, int source, int target) {
        boolean[] visited = new boolean[graph.getVerticesCount()];
        int[] distance = new int[graph.getVerticesCount()];
        Arrays.fill(distance, -1);

        Queue<Integer> queue = new ArrayDeque<>();
        visited[source] = true;
        queue.add(source);
        distance[source] = 0;
        while(!queue.isEmpty()){
            int current = queue.poll();
            for (int neighbor : graph.getNeighbors(current)){
                if (!visited[neighbor]){
                    queue.add(neighbor);
                    visited[neighbor] = true;
                    distance[neighbor] = distance[current] + 1;
                    if (neighbor == target){
                        return distance[target];
                    }
                }
            }
        }
        return -1;
    }

}
