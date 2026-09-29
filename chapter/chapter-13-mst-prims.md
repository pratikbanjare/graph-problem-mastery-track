# Topic 5: Minimum Spanning Tree with Prim's Algorithm

## Index

- [Learning path](#learning-path)
- [1. What an MST must achieve](#1-what-an-mst-must-achieve)
- [2. Grow one tree from a starting vertex](#2-grow-one-tree-from-a-starting-vertex)
- [3. Let the frontier choose the next edge](#3-let-the-frontier-choose-the-next-edge)
- [4. Trace the example graph](#4-trace-the-example-graph)
- [5. Connect the ideas to the implementation](#5-connect-the-ideas-to-the-implementation)
- [6. Complexity and important boundaries](#6-complexity-and-important-boundaries)
- [7. Bare-minimum pseudocode](#7-bare-minimum-pseudocode)
- [8. Practice task](#8-practice-task)
- [BDD feature location](#bdd-feature-location)
- [BDD scenario gap](#bdd-scenario-gap)

## Learning path

We will answer one question at a time:

1. What does a minimum spanning tree (MST) need to produce?
2. How can one tree grow without making a cycle?
3. Why is the cheapest edge leaving the tree a safe greedy choice?
4. How does the `PriorityQueue` in `PrimsAlgorithm.java` implement that choice?
5. How can a BDD run confirm the behavior?

## 1. What an MST must achieve

An MST is a set of edges in a connected, undirected, weighted graph that:

- reaches every vertex;
- contains no cycle;
- contains exactly `V - 1` edges for `V` vertices;
- has the smallest possible total weight.

So, for four vertices, we must select exactly three edges. Selecting cheap
edges alone is not enough: the selected edges must also keep extending one
connected tree.

That observation suggests a natural strategy: start with one vertex, then
repeatedly attach one new vertex to the tree.

## 2. Grow one tree from a starting vertex

Prim's algorithm starts with a chosen vertex and maintains two groups:

- **visited**: vertices already inside the growing tree;
- **unvisited**: vertices still waiting to be connected.

At every step, consider only edges crossing from `visited` to `unvisited`.
Choose the lightest such edge, add it to the MST, and move its destination
vertex into `visited`.

Why does this avoid cycles? An accepted edge always contributes a new vertex.
An edge whose destination is already visited is discarded because it would
connect two vertices already in the tree.

The remaining question is how to find the lightest frontier edge efficiently.
That is the job of a min-priority queue.

## 3. Let the frontier choose the next edge

The priority queue stores candidate edges ordered by weight:

- when a vertex joins the tree, add all of its outgoing weighted edges;
- remove the smallest candidate;
- skip it if its destination was already visited;
- otherwise accept it and expand the frontier from the new vertex.

This is a **greedy algorithm**: each choice is the cheapest edge that safely
connects a new vertex. The cut property of MSTs tells us that a lightest edge
crossing the cut between the tree and the remaining vertices can belong to an
MST.

## 4. Trace the example graph

Use the same four-vertex graph as the BDD feature:

```text
       2
  1 -------- 2
  | \       /
1 |  \4   /3
  |   \  /
  4 -------- 3
       1
```

Its undirected weighted edges are:

```text
1 -- 2 (2)     1 -- 4 (1)     1 -- 3 (4)
2 -- 4 (3)     3 -- 4 (1)
```

The implementation starts at vertex `1`, so follow that choice:

1. `visited = {1}`; candidates are `(1, 4, 1)`, `(1, 2, 2)`,
   and `(1, 3, 4)`.
2. Select `(1, 4, 1)`; now `visited = {1, 4}`.
   Add `(4, 3, 1)` and `(4, 2, 3)` to the candidates.
3. Select `(4, 3, 1)`; now `visited = {1, 3, 4}`.
   The old `(1, 3, 4)` candidate is skipped because `3` is visited.
4. Select `(1, 2, 2)`; all four vertices are connected.

The MST is therefore:

```text
(1, 4, 1), (4, 3, 1), (1, 2, 2)
```

Its total weight is `1 + 1 + 2 = 4`, and it has `4 - 1 = 3` edges.

## 5. Connect the ideas to the implementation

Open `../src/main/java/practice/mst/PrimsAlgorithm.java` while reading this
mapping:

- `visited` records the vertices already in the growing tree.
- `queue` is a min-heap ordered by `Edge::getWeight`.
- `populateQueue` converts each `WeightedEdge` from the graph adjacency list
  into a candidate `Edge`.
- `startingVertex = 1` makes the trace above deterministic.
- `queue.poll()` chooses the lightest frontier edge.
- `if (visited[edge.getTo()])` rejects an edge that would revisit a vertex.
- `mstEdges.size() == graph.getVertexCount() - 1` stops once a tree is
  complete.
- The final size check rejects a disconnected graph, because no single MST
  can span it.

The graph uses vertex numbers `1` through `V`, so the boolean array has
`V + 1` positions and leaves index `0` unused.

Implement or review the explained behavior in:

```text
src/main/java/practice/mst/PrimsAlgorithm.java
```

Then run the BDD test:

```text
src/test/java/chapter/topic_05/PrimsBdd.java
```

The selected-edge checks verify the greedy order, the total-weight check
verifies the result, and the disconnected-graph scenario verifies failure
handling. A successful run is evidence that the implementation matches the
concepts in this lesson.

## 6. Complexity and important boundaries

Let `V` be the number of vertices and `E` the number of edges.

- Each candidate can enter the priority queue at most as many times as its
  adjacency-list occurrence.
- Queue operations cost `O(log E)`.
- Overall time is `O(E log E)`.
- The visited array, queue, and result use `O(V + E)` space.

The method expects a connected undirected graph. A disconnected graph has a
minimum spanning forest rather than one MST. Negative weights are still valid:
the queue compares weights, regardless of whether they are positive or
negative.

## 7. Bare-minimum pseudocode

```text
visited[start] = true
add every edge from start to min-heap

while heap is not empty and tree has fewer than V - 1 edges:
    edge = remove smallest edge
    if edge.to is already visited:
        continue
    add edge to tree
    visited[edge.to] = true
    add every edge from edge.to to min-heap

if tree has fewer than V - 1 edges:
    reject disconnected graph

return tree
```

## 8. Practice task

Implement the algorithm described above in:

```text
src/main/java/practice/mst/PrimsAlgorithm.java
```

Use the example graph to predict the selected edge order before running:

```text
mvn -Dtest=chapter.topic_13.PrimsBdd test
```

When a scenario fails, read the logged expected value, actual value, graph,
and selected edges before changing the implementation.

## BDD feature location

The scenarios used by `PrimsBdd` are in:

```text
src/test/resources/features/chapter/topic_13/prims_algorithm.feature
```

The step definitions are in:

```text
src/test/java/chapter/topic_13/PrimsAlgorithmSteps.java
```

## BDD scenario gap

The feature covers the main connected case, skipping a visited destination,
and disconnected-graph rejection. The added one-vertex scenario covers the
boundary where a valid MST is empty. Additional useful future scenarios would
check negative weights and equal-weight edges; neither is required to explain
the current implementation's core control flow.