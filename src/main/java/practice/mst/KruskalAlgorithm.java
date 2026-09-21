package practice.mst;

import practice.graph.Graph;
import practice.model.Edge;
import practice.model.GraphType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KruskalAlgorithm {

    public List<Edge> minimumSpanningTree(Graph graph) {

        if (!GraphType.UNDIRECTED.equals(graph.getGraphType())) {
            throw  new IllegalArgumentException("Undirected graphs are not allowed in Kruskal");
        }

        List<Edge> kruskalEdges = new ArrayList<>(graph.getEdges());
        kruskalEdges.sort(Comparator.comparing(Edge::getWeight));

        int[] rank = new int[graph.getVertexCount()+1];
        int[] parent = new int[graph.getVertexCount()+1];

        for(int i = 1; i<=graph.getVertexCount(); i++){
            parent[i] = i;
            rank[i] = 0;
        }

        int selectedEdges = 0;
        List<Edge> kruskalEdgeList = new ArrayList<>();
        for (Edge edge : kruskalEdges) {

            if (union(edge.getFrom(), edge.getTo(), parent, rank)){
                System.out.println("Edge from " + edge.getFrom() + " to " + edge.getTo() + " is accepted!!!");
                kruskalEdgeList.add(edge);
                if (++selectedEdges == graph.getVertexCount() - 1) {
                    break;
                }
            } else {
                System.out.println("Edge from " + edge.getFrom() + " to " + edge.getTo() + " is Skipped!!!");
            }
        }
        if (selectedEdges < graph.getVertexCount()-1) {
            throw new IllegalArgumentException("Graph is disconnected!!!\nMinimum spanning tree requires connected graph!!!");
        }
        return kruskalEdgeList;
    }

    private boolean union(int from, int to, int[] parent, int[] rank) {

        int rootFrom = find(from, parent);
        int rootTo = find(to, parent);

        if (rootFrom == rootTo){
            return false;
        }
        if (rank[rootFrom] == rank[rootTo]){
            parent[rootTo] = rootFrom;
            rank[rootFrom] +=1;
        } else if (rank[rootFrom] > rank[rootTo]){
            parent[rootTo] = rootFrom;
        } else {
            parent[rootFrom] = rootTo;
        }
        return true;
    }

    private int find(int vertex, int[] parent) {
        if (parent[vertex] == vertex){
            return vertex;
        }
        return parent[vertex] = find(parent[vertex], parent);
    }
}
