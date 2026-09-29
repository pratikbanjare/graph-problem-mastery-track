# Topic 11: Shortest Paths in a Directed Acyclic Graph

## Index

- [Learning path](#learning-path)
- [1. Start with the graph](#1-start-with-the-graph)
- [2. Why a DAG makes shortest paths easier](#2-why-a-dag-makes-shortest-paths-easier)
- [3. Build the topological order](#3-build-the-topological-order)
- [4. Relax edges in that order](#4-relax-edges-in-that-order)
- [5. Trace the example](#5-trace-the-example)
- [6. Connect the idea to the implementation](#6-connect-the-idea-to-the-implementation)
- [7. Complexity and boundaries](#7-complexity-and-boundaries)
- [8. Bare-minimum pseudocode](#8-bare-minimum-pseudocode)
- [9. Practice task](#9-practice-task)
- [BDD feature location](#bdd-feature-location)

## Learning path

The lesson moves from the graph structure to the algorithm:

1. Recognize what makes a graph a directed acyclic graph (DAG).
2. Use that structure to order vertices.
3. Use the order to calculate shortest distances by relaxation.
4. Compare each step with `DagShortestPath`.
5. Run the BDD scenarios to confirm the behavior.

## 1. Start with the graph

A **directed weighted graph** gives every edge a direction and a cost. A **DAG** is a directed graph with no directed cycle.

Consider this example, with source vertex `1`:

```text
1 --4--> 2 --1--> 4
|        ^
2        |
v        1
3 --5--> 4
```

The edges are:

```text
1 -> 2 (4)
1 -> 3 (2)
3 -> 2 (1)
3 -> 4 (5)
2 -> 4 (1)
```

There are multiple routes to vertices `2` and `4`. Therefore, the task is not to choose the first route we see; it is to retain the cheapest route discovered so far.

## 2. Why a DAG makes shortest paths easier

In a general graph, a vertex might later receive a better distance from a predecessor that has not been processed yet. A DAG avoids that problem when its vertices are processed in **topological order**:

- every edge points from an earlier vertex to a later vertex;
- all predecessors of a vertex are handled before that vertex;
- once a vertex is reached in this order, its shortest distance is final.

For the example, one valid topological order is:

```text
1, 3, 2, 4
```

This also explains an important boundary: if a directed cycle is supplied, no topological order exists, so the input is not a valid DAG for this algorithm. The implementation relies on `TopologicalSort`, which raises a `GraphException` for such input.

## 3. Build the topological order

The first algorithmic step is to ask `TopologicalSort` for an order:

```java
TopologicalSort topologicalSort = new TopologicalSort();
List<Integer> sortedVertex = topologicalSort.sort(graph);
```

Reading this line naturally leads to the next question: once the vertices are ordered, where do the tentative distances live?

## 4. Relax edges in that order

Create one distance entry per vertex:

- set the source distance to `0`;
- set every other distance to `Integer.MAX_VALUE`, which represents unreachable;
- scan each vertex in topological order;
- for every outgoing edge `u -> v` with weight `w`, try the route through `u`.

The relaxation rule is:

```text
distance[v] = min(distance[v], distance[u] + w)
```

The implementation must first confirm that `u` is reachable. Otherwise, adding a weight to `Integer.MAX_VALUE` would not represent a real path.

## 5. Trace the example

Start with source `1`:

```text
distance[1] = 0
distance[2] = infinity
distance[3] = infinity
distance[4] = infinity
```

Process vertices in the order `1, 3, 2, 4`:

1. From `1`:
   - `1 -> 2`: `0 + 4 = 4`, so `distance[2] = 4`.
   - `1 -> 3`: `0 + 2 = 2`, so `distance[3] = 2`.
2. From `3`:
   - `3 -> 2`: `2 + 1 = 3`, which improves `distance[2]` from `4` to `3`.
   - `3 -> 4`: `2 + 5 = 7`, so `distance[4] = 7`.
3. From `2`:
   - `2 -> 4`: `3 + 1 = 4`, which improves `distance[4]` from `7` to `4`.
4. From `4`:
   - there are no outgoing edges to relax.

The final distances are:

```text
1: 0
2: 3   via 1 -> 3 -> 2
3: 2   via 1 -> 3
4: 4   via 1 -> 3 -> 2 -> 4
```

Negative edge weights are also safe in a DAG because the algorithm does not depend on non-negative weights. The BDD feature includes a negative-edge example without a cycle.

## 6. Connect the idea to the implementation

`DagShortestPath.shortestPath` delegates to the private `dagShortestPath` method. The method follows the lesson in the same order:

1. call `TopologicalSort.sort(graph)`;
2. allocate and initialize `distance`;
3. set `distance[source] = 0`;
4. relax every outgoing `WeightedEdge` while scanning `sortedVertex`;
5. return the complete distance array.

The array has `graph.getVertexCount() + 1` entries because this project addresses vertices from `1` through the vertex count. Index `0` is unused.

## 7. Complexity and boundaries

- Topological sorting takes `O(V + E)`.
- Each edge is relaxed once, also `O(E)`.
- Total time is `O(V + E)`.
- The distance array uses `O(V)` additional space.

Keep these constraints in mind:

- the graph must be directed and acyclic;
- unreachable vertices remain `Integer.MAX_VALUE`;
- negative edges are allowed, but negative cycles cannot occur in a valid DAG;
- the source must be a valid vertex in the graph.

## 8. Bare-minimum pseudocode

```text
order = topologicalSort(graph)
distance[every vertex] = infinity
distance[source] = 0

for u in order:
    for each edge (u, v, weight):
        if distance[u] is not infinity:
            distance[v] = min(distance[v], distance[u] + weight)

return distance
```

## 9. Practice task

Implement or complete the explained behavior in:

```text
src/main/java/practice/path/DagShortestPath.java
```

Then run the BDD test:

```text
src/test/java/chapter/topic_11/DagShortestPathBdd.java
```

The scenarios check multiple routes, negative edge weights, the source distance, unreachable vertices, and rejection of a cyclic graph. A successful run is evidence that the missing implementation section behaves as described.

## BDD feature location

The scenarios used by the BDD test are in:

```text
src/test/resources/features/chapter/topic_11/dag_shortest_path.feature
```
