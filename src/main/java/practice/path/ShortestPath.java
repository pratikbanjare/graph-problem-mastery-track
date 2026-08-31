package practice.path;

import practice.exception.GraphException;
import practice.graph.Graph;
import practice.model.DistanceEntry;
import practice.model.WeightedEdge;

import java.util.*;

public class ShortestPath {

    public int[] djikstra(Graph graph, int source){
        validateGraph(graph);
        int[] parent = new int[graph.getNumberOfVertex()+1];
        return this.djikstra(graph, source, parent);
    }

    public int[] djikstra(Graph graph, int source, int[] parent){
        return djikstra(graph, source, parent, -1);
    }

    public List<Integer> djikstraPath(Graph graph, int source, int target){
        validateGraph(graph);
        int[] parent = new int[graph.getNumberOfVertex()+1];
        int[] distance = this.djikstra(graph, source, parent, target);

        int current = target;
        List<Integer> path =new ArrayList<>();
        if (parent[target] == -1){
            return path;
        }
        while (current !=-1){
            path.add(current);
            current = parent[current];
        }
        Collections.reverse(path);
        return path;
    }

    private int[] djikstra(Graph graph, int source, int[] parent, int target) {


        int[] distance = new int[graph.getNumberOfVertex()+1];
        Arrays.fill(distance, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        distance[source] = 0;

        PriorityQueue<DistanceEntry>  pq = new PriorityQueue<>((a,b) -> Integer.compare(a.getDistance(), b.getDistance()));

        pq.add(new DistanceEntry(source, distance[source]));

        while (!pq.isEmpty()){
            DistanceEntry entry = pq.poll();

            // Stale Entry Check
            if (entry.getDistance() != distance[entry.getVertex()]){
                continue;
            }

            if (target != -1 && entry.getVertex() == target) {
                return distance;
            }

            for (WeightedEdge neighbor : graph.getEdgesOfVertex(entry.getVertex())){
                // Relaxation
                int d = neighbor.getWeight() + distance[entry.getVertex()];
                if (d < distance[neighbor.getTo()]){
                    distance[neighbor.getTo()] = d;
                    pq.add(new DistanceEntry(neighbor.getTo(), distance[neighbor.getTo()]));
                    parent[neighbor.getTo()] = entry.getVertex();
                }
            }
        }
        return distance;
    }

    public void validateGraph(Graph graph)  {
        graph.getEdges().stream().flatMap(Collection::stream).forEach(weightedEdge -> {
            if (weightedEdge.getWeight() < 0){
                try {
                    throw new GraphException("Encountered negative weight when parsing Graph using Djikstra's Algorithm");
                } catch (GraphException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
