package practice.path;

import practice.exception.GraphException;
import practice.graph.Graph;
import practice.model.DistanceEntry;
import practice.model.WeightedEdge;
import java.util.Arrays;
import java.util.Collection;
import java.util.PriorityQueue;

public class ShortestPath {

    public int[] dijstra(Graph graph, int source) throws GraphException {

        int[] distance = new int[graph.getNumberOfVertex()+1];
        Arrays.fill(distance, Integer.MAX_VALUE);

        distance[source] = 0;

        PriorityQueue<DistanceEntry>  pq = new PriorityQueue<>((a,b) -> Integer.compare(a.getDistance(), b.getDistance()));

        pq.add(new DistanceEntry(source, distance[source]));

        while (!pq.isEmpty()){
            DistanceEntry entry = pq.poll();

            // Stale Entry Check
            if (entry.getDistance() != distance[entry.getVertex()]){
                continue;
            }
            for (WeightedEdge neighbor : graph.getEdgesOfVertex(entry.getVertex())){
                // Relaxation
                int d = neighbor.getWeight() + distance[entry.getVertex()];
                if (d < distance[neighbor.getTo()]){
                    distance[neighbor.getTo()] = d;
                    pq.add(new DistanceEntry(neighbor.getTo(), distance[neighbor.getTo()]));
                }
            }
        }
        return distance;
    }

    public void validateGraph(Graph graph)  {
        graph.getEdges().stream().flatMap(Collection::stream).forEach(weightedEdge -> {
            if (weightedEdge.getWeight() < 0){
                try {
                    throw new GraphException("Encountered negative weight when parsing Graph using Djistra's Algorithm");
                } catch (GraphException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
