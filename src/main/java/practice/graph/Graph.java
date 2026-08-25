
package practice.graph;

import java.util.ArrayDeque;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;

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

    public void connectedComponent() {

        boolean[] visited = new boolean[this.vertices];
        for (int i = 1; i<=this.vertices; ++i){
            if (visited[i-1]){
                continue;;
            }
            System.out.println();
            bfs(i, visited);
        }
    }

    public void bfs(int node, boolean[] visited){
        bfsInternal(node-1, visited);
    }

    public void bfsInternal(int node, boolean[] visited){
        Queue<Integer> queue = new ArrayDeque<Integer>();

        visited[node] = true;
        queue.add(node);
        while(!queue.isEmpty()){
            int current = queue.poll();
            System.out.print(getPublicVertex(current) + " ");
            for (int neighbor : this.edges.get(current)){
                if (!visited[neighbor]){
                    visited[neighbor] = true;
                    queue.add(neighbor);
                }
            }
        }
    }
}