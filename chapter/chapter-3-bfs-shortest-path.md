# Breadth-First Search: Shortest Paths in an Unweighted Graph

## Index

1. [Start with the graph](#1-start-with-the-graph)
2. [Why breadth-first search finds the shortest path](#2-why-breadth-first-search-finds-the-shortest-path)
3. [The queue preserves the layer order](#3-the-queue-preserves-the-layer-order)
4. [Reconstructing the actual path](#4-reconstructing-the-actual-path)
5. [Bare-minimum pseudocode](#5-bare-minimum-pseudocode)
6. [Practice and feedback loop](#6-practice-and-feedback-loop)

## 1. Start with the graph

Suppose the vertices of an undirected graph are rooms and every edge is a
hallway. Walking through one hallway always costs exactly one step:

```text
        2 ---- 4 ---- 6
       /
      1
       \
        3 ---- 5
```

There are two ways to reach vertex `6` from vertex `1`:

  - `1 -> 2 -> 4 -> 6` uses 3 edges.
  - A longer route, if one exists, would use more edges.

The important question is not merely whether `6` is reachable. It is:

> How can we discover vertices in an order that guarantees the fewest
> hallways from the source?

## 2. Why breadth-first search finds the shortest path

Breadth-first search (BFS) answers that question by exploring in layers:

1. Layer 0 contains the source: `{1}`.
2. Layer 1 contains its unvisited neighbors: `{2, 3}`.
3. Layer 2 contains the next unvisited neighbors: `{4, 5}`.
4. Layer 3 contains `{6}`.

Because every edge has the same cost, every vertex in a later layer needs at
least one more edge than every vertex in the previous layer. Therefore, the
first time BFS discovers a vertex, it has found a shortest route to that
vertex.

This guarantee depends on the graph being **unweighted** (or every edge
having equal weight). For weighted edges, use an algorithm such as Dijkstra's
algorithm instead.

## 3. The queue preserves the layer order

BFS uses a first-in, first-out queue:

```text
queue: [1]
remove 1, add 2 and 3       queue: [2, 3]
remove 2, add 4             queue: [3, 4]
remove 3, add 5             queue: [4, 5]
remove 4, add 6             queue: [5, 6]
```

The `visited` array is essential. It prevents a vertex from being queued
again when another neighbor points to it, and it ensures that the first
discovery is the shortest one.

## 4. Reconstructing the actual path

Knowing the distance to `6` is useful, but often we need the route itself.
When BFS discovers a neighbor, store the vertex from which it was discovered:

```text
parent[2] = 1
parent[4] = 2
parent[6] = 4
```

Starting at the target and repeatedly following `parent` pointers gives
`6 -> 4 -> 2 -> 1`. Reverse that list to obtain the forward path
`1 -> 2 -> 4 -> 6`.

If the target is never discovered, its parent remains `-1`, so there is no
path. If source and target are the same, the shortest path is the source
alone and its distance is `0`.

The reference implementation is
`src/main/java/practice/path/BFS.java`. Its
`shortestDistancePathInternal` method records parents, while
`shortestDistance` records the number of edges in each layer.

## 5. Bare-minimum pseudocode

### Shortest distance

```text
distance[source] = 0
put source in queue

while queue is not empty:
    current = remove from queue
    for each unvisited neighbor of current:
        mark neighbor visited
        distance[neighbor] = distance[current] + 1
        put neighbor in queue

return distance[target] or -1 if it was never reached
```

### Shortest path

```text
run BFS while storing parent[neighbor] = current
start at target and follow parent until source
reverse the collected vertices
return the path, or an empty path if target was not reached
```

## 6. Practice and feedback loop

Implement or complete the explained BFS behavior in:

  `src/main/java/practice/path/BFS.java`

Then run the BDD test:

  ```bash
  mvn -Dtest=RunBfsCucumberTest test
  ```

The associated feature file is:

  `../src/test/resources/features/chapter/topic_03/bfs.feature`

The test scenarios check the shortest branching path, edge count,
unreachable targets, and the source-equals-target path. When the BDD test
passes, the behavior exercised by those scenarios is implemented correctly.
If an assertion fails, the step-definition logs show the expected and actual
path or distance.
