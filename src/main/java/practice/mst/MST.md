# Minimum Spanning Tree (MST)

## What is an MST?

A Minimum Spanning Tree is a subset of the graph that:
- connects all vertices,
- contains exactly V - 1 edges for a graph with V vertices,
- has minimum possible total edge weight,
- does not form a cycle.

This is different from shortest path:
- Shortest path asks: "What is the cheapest route from A to B?"
- MST asks: "How can I connect all vertices with the minimum total cost?"

A graph must usually be connected and undirected for an MST to exist.

## Kruskal's Algorithm

### Core idea
Kruskal's algorithm builds the MST by picking the lightest available edge that does not create a cycle.

It is best suited when you want to work with the global set of edges and keep adding the cheapest safe edge.

### Key data structure
Kruskal uses Disjoint Set Union (DSU), also called Union-Find.

- `parent[x]` = parent of vertex x
- `find(x)` = finds the representative/root of x
- `union(a, b)` = merges two sets if they are different

### Why DSU is needed
If two vertices are already connected through the current MST, adding another edge between them would create a cycle. DSU helps check that quickly.

### Kruskal steps
```text
1. Ensure the graph is undirected.
2. Collect all edges into a list.
3. Sort all edges by weight in ascending order.
4. Initialize a DSU for all vertices.
5. Traverse the sorted edges:
      if find(u) != find(v):
          accept the edge
          union(u, v)
6. Stop when you have selected V - 1 edges.
7. If fewer than V - 1 edges are selected, the graph is disconnected.
```

### Kruskal logic in simple words
- Start with the cheapest edge.
- If this edge connects two different components, it is safe to include it.
- If it connects vertices already in the same component, skip it.
- Repeat until the MST has V - 1 edges.

### Kruskal pseudocode
```text
function kruskal(graph):
    if graph is directed:
        throw error

    edges = all weighted edges
    sort edges by weight

    parent = [1..V]
    rank = [0..0]

    mst = []
    selected = 0

    for each edge (u, v, w) in sorted edges:
        if find(u) != find(v):
            union(u, v)
            mst.add((u, v, w))
            selected++

        if selected == V - 1:
            break

    if selected < V - 1:
        throw error "graph is disconnected"

    return mst
```

### DSU optimization
To make DSU efficient:
- Path compression: flatten the tree while finding a parent.
- Union by rank: attach the smaller rank under the larger rank.

This keeps the operations almost constant in practice.

### Kruskal complexity
- Sorting edges: O(E log E)
- DSU operations: O(E α(V))
- Total: O(E log E)

This is efficient for sparse and dense graphs.

## Prim's Algorithm

### Core idea
Prim's algorithm grows the MST from a single vertex. At every step, it picks the cheapest edge that connects the current tree to a vertex not yet included.

This is a "grow from inside" approach, while Kruskal is a "pick global cheapest safe edge" approach.

### Fundamental idea
"I already have a tree. What is the cheapest edge that connects my tree to a vertex outside it?"

### Prim steps
```text
1. Choose any starting vertex.
2. Mark it as visited.
3. Add all edges from that vertex to a min-priority queue.
4. While the priority queue is not empty:
      poll the minimum-weight edge
      if the destination is already visited:
          skip it
      else:
          include the edge in the MST
          mark the destination as visited
          add all edges from the new vertex to the priority queue
5. Stop when V - 1 edges are selected.
6. If the queue empties too early, the graph is disconnected.
```

### Prim logic in simple words
- Start from one vertex.
- Keep track of the cheapest edge crossing from the current tree to the remaining graph.
- Keep adding edges until all vertices are connected.

### Prim pseudocode
```text
function prim(graph, start):
    visited = [false] * V
    minHeap = priority queue of edges
    mst = []

    visited[start] = true
    push all edges from start into minHeap

    while minHeap is not empty:
        (u, v, w) = minHeap.poll()

        if visited[v]:
            continue

        mst.add((u, v, w))
        visited[v] = true

        if mst.size == V - 1:
            break

        push all edges from v into minHeap

    if mst.size != V - 1:
        throw error "graph is disconnected"

    return mst
```

### Data structure used
Prim usually uses:
- a min-priority queue (heap)
- a visited array

This makes it easy to always choose the lightest crossing edge.

### Prim complexity
With a priority queue and adjacency list:
- O(E log V)

With an adjacency matrix:
- O(V^2)

Prim is often faster than Kruskal when the graph is dense and we want to grow a tree from a single source.

## Kruskal vs Prim

- Kruskal: sorts all edges globally, picks smallest safe edge.
- Prim: grows a tree from a start node, picks the smallest edge from the current tree.
- Kruskal uses DSU.
- Prim uses a priority queue + visited set.

### Typical usage
- Use Kruskal when you need a global view of edges and are working with a graph represented as edge list.
- Use Prim when the graph is connected and you want to build the tree incrementally from one starting point.

## Key takeaway
Both algorithms produce a valid MST for a connected, weighted, undirected graph, but they use different strategies:
- Kruskal focuses on safe edge selection.
- Prim focuses on expanding the active tree.

When implemented correctly, both guarantee the minimum total cost without creating cycles.
