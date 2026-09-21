package practice.path;

import practice.graph.Graph;

import java.util.*;

public class BFS {

    public List<Integer> shortestDistancePath(Graph graph, int source, int target){

        int[] parent =  shortestDistancePathInternal(graph,source, target);

        return tracePath(graph, source, target, parent);

    }

    public int[] shortestDistancePathInternal(Graph graph, int source, int target) {
        boolean[] visited = new boolean[graph.getVertexCount() + 1];
        int[] parent = new int[graph.getVertexCount() + 1];
        Arrays.fill(parent, -1);
        Queue<Integer> queue = new ArrayDeque<>();

        if (source == target) {
            return parent;
        }

        queue.add(source);
        visited[source] = true;

        while(!queue.isEmpty()){
            int current = queue.poll();
            for (int neighbor : graph.getEdgesOfVertex(current)){
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
            return List.of(source);
        }
        if (parent[target] == -1) {
            return Collections.emptyList();
        }
        int current = target;
        List<Integer> res = new ArrayList<>();
        while (current != -1)  {
            res.add(current);
            current = parent[current];
        }
        Collections.reverse(res);
        return res;
    }

    public int shortestDistance(Graph graph, int source, int target) {
        return shortestDistanceInternal(graph, source, target);

    }

    private int shortestDistanceInternal (Graph graph, int source, int target) {
        boolean[] visited = new boolean[graph.getVertexCount()+1];
        int[] distance = new int[graph.getVertexCount()+1];
        Arrays.fill(distance, -1);

        Queue<Integer> queue = new ArrayDeque<>();
        visited[source] = true;
        queue.add(source);
        distance[source] = 0;
        while(!queue.isEmpty()){
            int current = queue.poll();
            for (int neighbor : graph.getEdgesOfVertex(current)){
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
