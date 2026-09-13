package practice.mst;

import practice.graph.Graph;
import practice.model.Edge;
import practice.model.WeightedEdge;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class PrimsAlgorithm {

    public List<Edge> minimumSpammingTree(Graph graph) {

        List<Edge> mstEdges = new ArrayList<>();
        PriorityQueue<Edge> queue = new PriorityQueue<>(Comparator.comparing(Edge::getWeight));
        boolean[] visited = new boolean[graph.getNumberOfVertex() + 1];

        int startingVertex = 1;
        visited[startingVertex] = true;
        populateQueue(queue, startingVertex, graph.getAdjacencyListOfVertex(startingVertex));

        while(!queue.isEmpty()){
            Edge edge = queue.poll();
            if (visited[edge.getTo()]){
                continue;
            }
            mstEdges.add(edge);
            visited[edge.getTo()] = true;
            if (mstEdges.size() == graph.getNumberOfVertex() -1){
                break;
            }
            populateQueue(queue, edge.getTo(), graph.getAdjacencyListOfVertex(edge.getTo()));
        }

        if (mstEdges.size() != graph.getNumberOfVertex() -1){
            throw new IllegalArgumentException("Provided graph is disconnected. MST requires connected graph!!!!");
        }

        return mstEdges;
    }

    private void populateQueue(PriorityQueue<Edge> queue, int vertex, List<WeightedEdge> weightedEdges){
        for(WeightedEdge weightedEdge : weightedEdges) {
            queue.add(new Edge(vertex, weightedEdge.getTo(), weightedEdge.getWeight()));
        }
    }
}
