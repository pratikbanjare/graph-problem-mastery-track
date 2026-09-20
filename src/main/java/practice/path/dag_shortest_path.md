# DAG Shortest Path Algorithm

A DAG shortest path algorithm finds the shortest distance from a source node to every other node in a directed acyclic graph (DAG).

It works efficiently because a DAG has a topological ordering. In that order, every edge goes from an earlier node to a later node, so once we process a vertex, all of its predecessors have already been processed.

## Key Idea

- First, compute a topological order of the graph.
- Initialize the distance of the source to `0` and all other vertices to `Infinity`.
- Traverse vertices in topological order.
- For each outgoing edge `(u -> v)` with weight `w`:
  - if `distance[u]` is reachable and `distance[u] + w < distance[v]`, then update `distance[v]`.

This is called relaxation.

Because the graph is acyclic, each vertex is processed exactly once in a valid order, and the shortest distance to a vertex is finalized when we reach it.

## Why Topological Order Matters

If the vertices are ordered like this:

```text
A -> B -> C
```

Then when we process `A`, we can relax edges from `A` to its neighbors. By the time we reach `B`, all distances that can influence `B` have already been computed.

This avoids repeated reprocessing and gives us an efficient solution.

## Java Implementation

```java
public class DagShortestPath implements IShortestPath {
    @Override
    public int[] shortestPath(Graph graph, int source) {
        return this.dagShortestPath(graph, source);
    }

    private int[] dagShortestPath(Graph graph, int source) {
        TopologicalSort topologicalSort = new TopologicalSort();
        List<Integer> sortedVertex = topologicalSort.sort(graph);

        int[] distance = new int[graph.getNumberOfVertex() + 1];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[source] = 0;

        for (int vertex : sortedVertex) {
            for (WeightedEdge weightedEdge : graph.getWeightedEdgesOfVertex(vertex)) {
                int neighbor = weightedEdge.getTo();
                int weight = weightedEdge.getWeight();

                if (distance[vertex] != Integer.MAX_VALUE &&
                        distance[vertex] + weight < distance[neighbor]) {
                    distance[neighbor] = distance[vertex] + weight;
                }
            }
        }
        return distance;
    }
}
```

## Relaxation Rule

For each edge `(u, v)` with weight `w`:

```java
if (distance[u] != Integer.MAX_VALUE && distance[u] + w < distance[v]) {
    distance[v] = distance[u] + w;
}
```

This means:

- if `u` is reachable,
- and the path through `u` gives a better route to `v`,
- then update the best known distance to `v`.

## Example

Consider the DAG below:

```text
1 -> 2 (4)
1 -> 3 (2)
3 -> 2 (1)
3 -> 4 (5)
2 -> 4 (1)
```

If the source is `1`:

- `distance[1] = 0`
- `distance[3] = 2` via `1 -> 3`
- `distance[2] = min(4, 2 + 1) = 3`
- `distance[4] = min(5, 3 + 1) = 4`

Final result:

```text
1: 0
2: 3
3: 2
4: 4
```

## Complexity

- Topological sort: `O(V + E)`
- Edge relaxation: `O(E)`
- Total: `O(V + E)`

This is much faster than algorithms like Bellman-Ford for DAGs.

## Important Notes

- The algorithm only works for directed acyclic graphs.
- If the graph contains a cycle, the topological order does not exist.
- For unreachable vertices, the distance remains `Integer.MAX_VALUE`.

## Summary

The DAG shortest path algorithm is elegant because it converts a graph problem into a simple linear scan in topological order. This guarantees that every shortest path is discovered exactly when the necessary predecessor information is available.
