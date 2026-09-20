# Minimum Spanning Tree

## MST vs. shortest path

- A **shortest-path** algorithm finds the cheapest path between one source and
  another vertex (or all vertices).
- A **minimum spanning tree (MST)** connects every vertex in a connected,
  undirected, weighted graph with the minimum possible total edge weight. It
  contains exactly `V - 1` edges and cannot contain a cycle.

The tree minimizes the total cost of the selected edges; it does not
necessarily provide the shortest path between any pair of vertices.

Common MST algorithms:

- **Kruskal's algorithm** processes edges globally in increasing weight order
  and uses a **Disjoint Set Union (DSU)**.
- **Prim's algorithm** grows one tree from a starting vertex and uses a
  **priority queue** to choose the next cheapest edge.

## Kruskal's algorithm

Kruskal's algorithm follows a greedy strategy:

> Repeatedly choose the lightest edge that connects two currently disconnected
> components.

An edge is safe to add when its endpoints belong to different components. If
both endpoints are already in the same component, adding the edge would create
a cycle, so the edge is skipped.

### Logic and steps

For a graph with `V` vertices and `E` edges:

```text
1. Verify that the graph is undirected.
2. Sort all edges by non-decreasing weight.
3. Initialize a DSU with one component for each vertex.
4. Start with an empty MST and selectedEdges = 0.
5. For each edge (u, v) in sorted order:
      a. Find the representative of u and v.
      b. If the representatives differ:
            - Union the two components.
            - Add (u, v) to the MST.
            - Increment selectedEdges.
      c. Otherwise, skip the edge because it creates a cycle.
6. Stop when selectedEdges == V - 1.
7. If fewer than V - 1 edges were selected, the graph is disconnected
   and an MST does not exist.
```

The implementation in `KruskalAlgorithm.java` applies this process and returns
the selected edges. It expects vertex identifiers in the range `1` through `V`
and reports a disconnected graph with an exception.

## Prim's algorithm

Prim's algorithm is another greedy algorithm for finding an MST. Instead of
sorting every edge in the graph, it grows one tree from a starting vertex.
At every step, it chooses the lightest edge that crosses from the vertices
already in the tree to a vertex outside the tree.

The implementation in `PrimsAlgorithm.java` starts with vertex `1`. It uses a
min-priority queue ordered by edge weight and a `visited` array to track the
vertices that have already been added to the tree. The method returns
`V - 1` selected edges for a connected graph and throws an exception when the
graph is disconnected.

### Logic and steps

For a connected, undirected, weighted graph with `V` vertices and `E` edges:

```text
1. Choose a starting vertex (this implementation uses vertex 1).
2. Mark the starting vertex as visited.
3. Add all edges leaving the starting vertex to a min-priority queue.
4. While the queue is not empty:
      a. Remove the edge with the smallest weight.
      b. If its destination is already visited, discard the edge.
         It would lead back into the tree and create a cycle.
      c. Otherwise:
            - Add the edge to the MST.
            - Mark its destination as visited.
            - Add all edges leaving the new vertex to the queue.
5. Stop after selecting V - 1 edges.
6. If fewer than V - 1 edges were selected, the graph is disconnected
   and an MST does not exist.
```

The priority queue may contain edges that become obsolete after another edge
visits their destination. This is intentional: those edges are removed lazily,
and the `visited` check prevents them from being added to the MST.

### Correctness intuition

At any point, the visited vertices form one connected tree. The queue contains
candidate edges from that tree to unvisited vertices. The minimum-weight
candidate is the lightest edge crossing the cut between the current tree and
the remaining vertices. By the cut property, that edge is safe to add to an
MST. Repeating this process adds one new vertex at a time without creating a
cycle; after `V - 1` additions, the result is a spanning tree of minimum total
weight.

### Complexity

- Each graph edge can be inserted into and removed from the priority queue.
- Priority-queue operations take `O(log E)`.
- Total time: `O(E log E)`, commonly written as `O(E log V)` for a simple
  graph.
- Extra space: `O(V + E)` for the visited array, queue, and result.

### Example

For edges `(A, B, 1)`, `(A, C, 3)`, `(B, C, 2)`, and `(C, D, 4)`, starting at
`A`:

1. The queue contains `(A, B, 1)` and `(A, C, 3)`.
2. Select `(A, B, 1)` and add `B`'s outgoing edges.
3. Select `(B, C, 2)`; `(A, C, 3)` is now obsolete because `C` is reached by
   the cheaper edge.
4. Select `(C, D, 4)`.

The MST is `{(A, B), (B, C), (C, D)}` with total weight `7`.

### Disjoint Set Union (DSU)

The DSU keeps track of which vertices are already connected:

- `find(vertex)` returns the root (representative) of the vertex's component.
- `union(u, v)` merges two different components and returns `true`.
- If `find(u) == find(v)`, `u` and `v` are already connected, so `union` returns
  `false` and the edge is rejected.

This implementation uses two optimizations:

- **Path compression:** while finding a root, point every visited vertex
  directly to that root.
- **Union by rank:** attach the shorter-rank tree below the taller-rank tree.
  When both ranks are equal, attach one root below the other and increase the
  new root's rank.

Together, these make each DSU operation effectively constant time in practice:
`O(alpha(V))` amortized, where `alpha` is the inverse Ackermann function.

### Correctness intuition

Edges are considered from lightest to heaviest. At every step, the chosen edge
connects two separate components, so it cannot create a cycle. By the cut
property, the lightest edge crossing any cut between two components is safe to
include in some MST. Therefore, every accepted edge can be part of an MST, and
after `V - 1` accepted edges the result is a spanning tree with minimum total
weight.

### Complexity

- Sorting edges: `O(E log E)`
- DSU processing: `O(E alpha(V))`
- Total: `O(E log E)`
- Extra space: `O(V + E)` for the DSU and edge list

### Example

For edges `(A, B, 1)`, `(B, C, 2)`, `(A, C, 3)`, and `(C, D, 4)`:

1. Select `(A, B, 1)`.
2. Select `(B, C, 2)`.
3. Skip `(A, C, 3)` because `A` and `C` are already connected.
4. Select `(C, D, 4)`.

The MST is `{(A, B), (B, C), (C, D)}` with total weight `7`.
