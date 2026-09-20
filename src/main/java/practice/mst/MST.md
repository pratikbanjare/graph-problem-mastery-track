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
