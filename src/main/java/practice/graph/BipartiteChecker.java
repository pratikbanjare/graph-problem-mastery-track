package practice.graph;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class BipartiteChecker {

    public boolean isBipartite(Graph graph){

        int[] color = new int[graph.getVertices()];
        Arrays.fill(color, -1);
        for (int i = 1; i<= graph.getVertices(); ++i){
            if (color[i-1] == -1){
                System.out.println("Validating Bipartite from vertex " + i);
                if( !isBipartiteInternal(graph, i-1, color)){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isBipartiteInternal(Graph graph, int node, int[] color){

        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(node);
        color[node] = 1;
        while (!queue.isEmpty()){
            int v = queue.poll();
            System.out.println("[INTERNAL] BFS processing node -> " + v);
            for (int neighbor : graph.getEdgesOfVertex(v)){

                if( color[neighbor] == -1){
                    color[neighbor] = color[v] == 1 ? 0 : 1;
                    queue.add(neighbor);
                    System.out.println("[INTERNAL][NODE -> "+ v + "] [Queue->ADD -> " + neighbor + "] [COLORING ->  " + color[neighbor] + "]" );
                } else if (color[neighbor] == color[v]){
                    System.out.println("[INTERNAL][NODE -> " + v + " ] Adajcent vertices have same color < " + v + ", " + neighbor + ">. Color -> " + color[neighbor]);
                    return false;
                }
            }
        }
        return true;
    }
}
