# Topic 5: Minimum Spanning Tree with Kruskal's Algorithm

## Index

- [Learning path](#learning-path)
- [1. Start with the problem](#1-start-with-the-problem)
- [2. Let the graph lead us to a greedy choice](#2-let-the-graph-lead-us-to-a-greedy-choice)
- [3. Reject cycles with disjoint sets](#3-reject-cycles-with-disjoint-sets)
- [4. Trace the example](#4-trace-the-example)
- [5. Connect the idea to the implementation](#5-connect-the-idea-to-the-implementation)
- [6. Complexity and boundaries](#6-complexity-and-boundaries)
- [7. Bare-minimum pseudocode](#7-bare-minimum-pseudocode)
- [8. Practice task](#8-practice-task)
- [BDD feature location](#bdd-feature-location)

## Learning path

The lesson follows the decisions Kruskal's algorithm makes:

1. Define what a minimum spanning tree (MST) must produce.
2. Sort edges so the cheapest useful choice comes first.
3. Detect and reject an edge that would create a cycle.
4. Stop after exactly `V - 1` edges.
5. Compare each idea with `KruskalAlgorithm.java` and confirm it with the BDD test.

## 1. Start with the problem

An MST is a set of edges that:

- connects every vertex in a connected, undirected, weighted graph;
- contains no cycle;
- has exactly `V - 1` edges for `V` vertices;
- has the smallest possible total weight among all spanning trees.

Consider this graph:

```text
        10
   1 -------- 2
   |\        /
 6 | \5    /15
   |  \    /
   3 -------- 4
        4
```

Its weighted edges are:

```text
1 -- 2 (10)     1 -- 3 (6)      1 -- 4 (5)
2 -- 4 (15)     3 -- 4 (4)
```

There are four vertices, so a spanning tree needs three edges. The question is now: how can we choose three edges cheaply without accidentally making a cycle?

## 2. Let the graph lead us to a greedy choice

Kruskal's algorithm answers that question by considering edges in non-decreasing weight order:

```text
(3, 4, 4), (1, 4, 5), (1, 3, 6), (1, 2, 10), (2, 4, 15)
```

For each edge:

- select it if it joins two currently separate components;
- skip it if both endpoints are already connected.

This is a **greedy algorithm**: it commits to the cheapest safe edge available. The next question is how to determine “currently separate” efficiently while components grow.

## 3. Reject cycles with disjoint sets

Kruskal uses a **disjoint-set union (DSU)**, also called **union-find**:

- `find(v)` returns the representative (root) of the component containing `v`;
- `union(a, b)` merges two different components;
- if `find(a) == find(b)`, adding `(a, b)` would create a cycle.

The implementation improves DSU performance with:

- **path compression** in `find`, which makes future lookups point closer to the root;
- **union by rank** in `union`, which attaches the shorter tree below the taller one.

This gives a precise rule for the graph above: after selecting `(3, 4)` and `(1, 4)`, vertices `1`, `3`, and `4` are one component. Therefore `(1, 3)` is skipped because it would close a cycle. The next safe edge is `(1, 2)`, which connects the remaining vertex.

## 4. Trace the example

Start with four one-vertex components:

```text
{1}  {2}  {3}  {4}
```

Process the sorted edges:

1. `(3, 4, 4)` joins `{3}` and `{4}`.
   - Selected edges: `(3, 4)`
   - Components: `{1}`, `{2}`, `{3, 4}`
2. `(1, 4, 5)` joins `{1}` and `{3, 4}`.
   - Selected edges: `(3, 4)`, `(1, 4)`
   - Components: `{1, 3, 4}`, `{2}`
3. `(1, 3, 6)` is skipped.
   - Both endpoints are already in `{1, 3, 4}`.
   - Selecting it would create a cycle.
4. `(1, 2, 10)` joins the two remaining components.
   - Selected edges: `(3, 4)`, `(1, 4)`, `(1, 2)`
   - Three edges means the tree is complete.

The MST has total weight `4 + 5 + 10 = 19`. Notice that edge `(2, 4, 15)` never needs to be considered after `V - 1` edges have been selected.

## 5. Connect the idea to the implementation

`KruskalAlgorithm.minimumSpanningTree` follows the trace directly:

1. It rejects a directed graph because an MST is defined here for undirected graphs.
2. It copies and sorts `graph.getEdges()` by `Edge::getWeight`.
3. It initializes `parent` and `rank` so every vertex starts in its own component.
4. It calls `union` for each sorted edge.
5. It adds an edge only when `union` returns `true`.
6. It stops at `graph.getVertexCount() - 1` selected edges.
7. It rejects a disconnected graph if that count cannot be reached.

The arrays have `V + 1` entries because this project numbers vertices from `1` through `V`; index `0` is unused.

Run the BDD test after implementing or changing the missing section of:

```text
src/main/java/practice/mst/KruskalAlgorithm.java
```

The selected-edge assertions check the actual greedy order, while the total-weight assertion checks the MST result as a whole. A successful BDD run is evidence that the implementation satisfies the behavior described here.

## 6. Complexity and boundaries

- Sorting the `E` edges costs `O(E log E)`.
- DSU operations are almost constant amortized time, `O(alpha(V))`, with path compression and union by rank.
- Total time is `O(E log E)`.
- The parent, rank, and result structures use `O(V + E)` space.

The method expects:

- an undirected graph;
- a connected graph, because a disconnected graph has a minimum spanning **forest**, not one spanning tree;
- valid vertex identifiers accepted by `Graph`.

Negative edge weights are valid. Kruskal still compares weights from smallest to largest; only the relative order matters.

## 7. Bare-minimum pseudocode

```text
if graph is directed:
    reject

sort all edges by weight
make-set(each vertex)
tree = empty

for each edge (u, v) in sorted edges:
    if find(u) != find(v):
        union(u, v)
        add edge to tree
        if tree has V - 1 edges:
            break

if tree has fewer than V - 1 edges:
    reject disconnected graph

return tree
```

## 8. Practice task

Complete the explained behavior in:

```text
src/main/java/practice/mst/KruskalAlgorithm.java
```

Then run:

```text
src/test/java/chapter/topic_05/KruskalAlgorithmBddTest.java
```

Read any logged assertion failure before changing the implementation: the log identifies the graph, expected value, and actual value (or exception) that disagree.

## BDD feature location

The scenarios used by the BDD test are in:

```text
src/test/resources/features/chapter/topic_05/kruskal_algo.feature
```
