
package practice.graph;

import java.util.*;

public class Graph {

    private final int vertices;
    private List<List<Integer>> edges;

    Graph(int vertices) {
        if (vertices <= 0) throw new IllegalArgumentException("Invalid Vertices");
        this.vertices = vertices;
        edges = new ArrayList<>();
        for (int i = 0; i< this.vertices; ++i) {
            this.edges.add(new ArrayList<>());
        }
    }


    private boolean validateVertex(int v){
        return (v >= 1 && v <= this.vertices);

    }

    void addEdge(int u, int v) {
        if (validateVertex(u) || validateVertex(v)){
            throw new IllegalArgumentException("Invalid Vertex");
        }
        if (edges.get(u-1).contains(v-1)){
            System.out.println("Edge already exists");
            return;
        }
        this.edges.get(u-1).add(v-1);
        this.edges.get(v-1).add(u-1);
    }

    boolean hasEdge(int u, int v) {
        if (validateVertex(u) || validateVertex(v)) {
            throw new IllegalArgumentException("Invalid Vertex");
        }
        return this.edges.get(u-1).contains(v-1);
    }

    void removeEdge(int u, int v) {
        if (validateVertex(u) || validateVertex(v)) {
            throw new IllegalArgumentException("Invalid Vertex");
        }
        List<Integer> e1 = edges.get(u-1);
        int pos = e1.indexOf(v-1);
        if (pos != -1) {
            e1.remove(pos);
        } else {
            System.out.println("Edge not found");
            return;
        }

        List<Integer> e2 = edges.get(v-1);
        int pos2 = e2.indexOf(u-1);
        e2.remove(pos2);


    }

    void printGraph() {
        for (int i = 0; i< this.vertices; ++i){
            System.out.println();
            System.out.print("For vertex " + i+1 + "neighbors are -[ ");
            List<Integer> neighbors = edges.get(i);
            neighbors.forEach( j -> System.out.print(j+1 + " "));
        }
        System.out.print("]");
    }

    public int getPublicVertex(int v){
        return v+1;
    }


    public List<Integer> shortestDistancePath(int source, int target){

        int[] parent =  shortestDistancePathInternal(source-1, target-1);

        return tracePath(source-1, target-1, parent);

    }

    public int[] shortestDistancePathInternal(int source, int target) {
        boolean[] visited = new boolean[this.vertices];
        int[] parent = new int[this.vertices];
        Arrays.fill(parent, -1);
        Queue<Integer> queue = new ArrayDeque<>();

        if (source == target) {
            return parent;
        }

        queue.add(source);
        visited[source] = true;

        while(!queue.isEmpty()){
            int current = queue.poll();
            for (int neighbor : this.edges.get(current)){
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

    private List<Integer> tracePath (int source, int target, int[] parent){

        if (source == target){
            return List.of(getPublicVertex(source));
        }
        if (parent[target] == -1) {
            return Collections.emptyList();
        }
        int current = target;
        List<Integer> res = new ArrayList<>();
        while (current != -1)  {
            res.add(getPublicVertex(current));
            current = parent[current];
        }
        Collections.reverse(res);
        return res;
    }

    public int shortestDistance(int source, int target) {
        return shortestDistanceInternal(source-1, target-1);

    }

    private int shortestDistanceInternal (int source, int target) {
        boolean[] visited = new boolean[this.vertices];
        int[] distance = new int[this.vertices];
        Arrays.fill(distance, -1);

        Queue<Integer> queue = new ArrayDeque<>();
        visited[source] = true;
        queue.add(source);
        distance[source] = 0;
        while(!queue.isEmpty()){
            int current = queue.poll();
            for (int neighbor : this.edges.get(current)){
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