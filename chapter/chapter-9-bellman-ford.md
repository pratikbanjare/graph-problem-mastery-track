# Bellman-Ford Shortest Path

## Index

- [1. Why Bellman-Ford?](#1-why-bellman-ford)
- [2. Running example](#2-running-example)
- [3. The distance table](#3-the-distance-table)
- [4. Relaxation](#4-relaxation)
- [5. Why `V - 1` passes are enough](#5-why-v---1-passes-are-enough)
- [6. Detecting a reachable negative cycle](#6-detecting-a-reachable-negative-cycle)
- [7. Unreachable vertices and safe updates](#7-unreachable-vertices-and-safe-updates)
- [8. Complexity and implementation mapping](#8-complexity-and-implementation-mapping)
- [9. Bare-minimum pseudocode](#9-bare-minimum-pseudocode)
- [10. Your implementation task](#10-your-implementation-task)
- [11. BDD test location and execution](#11-bdd-test-location-and-execution)

## 1. Why Bellman-Ford?

Suppose a directed, weighted graph asks for the cheapest cost from one source vertex to every other vertex.

- Dijkstra's algorithm is fast, but it cannot safely process negative edge weights.
- Bellman-Ford accepts negative edge weights.
- A negative edge is not automatically a problem; a **reachable negative cycle** is.
- A negative cycle lets a path become cheaper every time the cycle is traversed, so no finite shortest distance exists.

This leads to the central question:

> How can we repeatedly improve tentative distances, and then prove that no further improvement is possible?

## 2. Running example

Use source vertex `1` in this graph:

```text
1 --4--> 2 --(-2)--> 3 --3--> 4
 \--5-----------------> 3
```

The edges are:

| Edge | Weight |
|---|---:|
| `1 -> 2` | `4` |
| `1 -> 3` | `5` |
| `2 -> 3` | `-2` |
| `3 -> 4` | `3` |

Before exploring the edges, only the source has a known cost:

- `distance[1] = 0`
- every other distance is infinity, represented in `BellmanFord.java` by `Integer.MAX_VALUE`

How can the algorithm discover that reaching `3` through `2` is cheaper than the direct edge? The answer is **relaxation**.

## 3. The distance table

For this example, the best known distances evolve toward the final answer:

| Vertex | Initial | Best final distance | Reason |
|---:|---:|---:|---|
| `1` | `0` | `0` | It is the source |
| `2` | `∞` | `4` | `1 -> 2` |
| `3` | `∞` | `2` | `1 -> 2 -> 3`, costing `4 + (-2)` |
| `4` | `∞` | `5` | `1 -> 2 -> 3 -> 4`, costing `2 + 3` |

The algorithm does not need to guess the complete path. It only keeps the cheapest cost known for each vertex and uses that information to improve neighboring vertices.

## 4. Relaxation

For an edge `u -> v` with weight `w`, compare:

```text
candidate = distance[u] + w
```

- If `u` is unreachable, do not calculate a candidate from infinity.
- If `candidate < distance[v]`, replace `distance[v]`.
- Otherwise, keep the existing distance.

For the example:

- `1 -> 2`: `0 + 4 = 4`, so `distance[2]` becomes `4`.
- `1 -> 3`: `0 + 5 = 5`, so `distance[3]` becomes `5`.
- `2 -> 3`: `4 + (-2) = 2`, so `distance[3]` improves from `5` to `2`.
- `3 -> 4`: `2 + 3 = 5`, so `distance[4]` becomes `5`.

One pass may improve several edges. The next question is how many passes are required.

## 5. Why `V - 1` passes are enough

Any simple path in a graph with `V` vertices uses at most `V - 1` edges. If a path uses `V` or more edges, some vertex repeats and the path contains a cycle.

- When there is no reachable negative cycle, a shortest path can be chosen to be simple.
- Therefore, every finite shortest distance is represented by a path of at most `V - 1` edges.
- Repeating edge relaxation for `V - 1` passes gives every such path a chance to propagate its cost.

In the implementation, `graph.getVertexCount()` supplies `V`, and the outer loop performs these passes. If a complete pass makes no update, the distances have already converged and the remaining passes can be skipped.

## 6. Detecting a reachable negative cycle

After the `V - 1` passes, scan every edge one more time:

- If an edge can still be relaxed, some reachable path is still becoming cheaper.
- That can only happen because a reachable negative cycle is contributing another round of savings.
- `BellmanFord.java` reports this with `GraphException("Negative Cycle detected!!!!")`.

The word **reachable** matters. A negative cycle in a disconnected component cannot affect distances from the selected source, so it must not cause an exception.

## 7. Unreachable vertices and safe updates

An unreachable vertex keeps `Integer.MAX_VALUE`:

- It is the Java representation of infinity used by the implementation and the BDD tables.
- Never compute `Integer.MAX_VALUE + weight` as a real candidate.
- The implementation checks `distance[vertex] != Integer.MAX_VALUE` before relaxing an outgoing edge.

This guard explains why a negative cycle that cannot be reached from the source is ignored.

## 8. Complexity and implementation mapping

- Time complexity: `O(VE)`, because up to `V - 1` passes inspect all `E` edges.
- Space complexity: `O(V)` for the distance array, excluding the graph's own storage.

Read `src/main/java/practice/path/BellmanFord.java` while implementing:

- `shortestPath(...)` is the public entry point.
- `bellmanFord(...)` initializes distances, runs passes, and starts the final cycle check.
- `edgeRelaxation(...)` contains the reusable relaxation logic.
- `throwException` distinguishes an ordinary pass from negative-cycle detection.

## 9. Bare-minimum pseudocode

```text
distance[all vertices] = infinity
distance[source] = 0

repeat V - 1 times:
    changed = false
    for each edge (u, v, weight):
        if distance[u] is not infinity
           and distance[u] + weight < distance[v]:
            distance[v] = distance[u] + weight
            changed = true
    if changed is false:
        stop early

for each edge (u, v, weight):
    if distance[u] is not infinity
       and distance[u] + weight < distance[v]:
        report a reachable negative cycle

return distance
```

## 10. Your implementation task

Implement the missing Bellman-Ford logic in `src/main/java/practice/path/BellmanFord.java`.

- Initialize the distance array with infinity and set the source to `0`.
- Relax every edge for at most `V - 1` passes.
- Skip edges whose source is unreachable.
- Stop early when a pass makes no change.
- Perform the extra scan needed to detect a reachable negative cycle.
- Preserve the distance-array convention: index `0` is unused and vertices are indexed from `1`.

## 11. BDD test location and execution

The BDD feature associated with this lesson is:

`src/test/resources/features/chapter/topic_09/bellman_fort_shortest_path.feature`

Its step definitions and suite are in:

- `src/test/java/chapter/topic_09/BellmanFordStepDefinitions.java`
- `src/test/java/chapter/topic_09/BellmanFordBddTest.java`

Run `BellmanFordBddTest` after implementing the algorithm. Passing it confirms the behavior covered by the feature, including negative edges, unreachable vertices, and reachable negative-cycle detection.
