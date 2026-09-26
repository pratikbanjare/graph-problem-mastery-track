package practice.graph;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class BipartiteChecker {

    public boolean isBipartite(Graph graph){

        int[] color = new int[graph.getVertexCount()+1];
        Arrays.fill(color, -1);
        for (int i = 1; i<= graph.getVertexCount(); ++i){
            if (color[i] == -1){
                if( !isBipartiteInternal(graph, i, color)){
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
            for (int neighbor : graph.getEdgesOfVertex(v)){

                if( color[neighbor] == -1){
                    color[neighbor] = color[v] == 1 ? 0 : 1;
                    queue.add(neighbor);
                } else if (color[neighbor] == color[v]){
                    return false;
                }
            }
        }
        return true;
    }
}
