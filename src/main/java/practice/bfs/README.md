# Breadth-First Search (BFS)

Breadth-First Search visits a graph in **layers**. Starting at one vertex, it
visits every directly connected neighbor before moving to vertices that are two
edges away, then three edges away, and so on.

## Underlying concept

BFS uses two pieces of state:

1. A **queue** stores vertices in the order they should be processed. The
   first vertex added is the first vertex removed (FIFO).
2. A `visited` array prevents a vertex from being added more than once. A
   vertex is marked when it enters the queue, not when it is removed.

For the graph used in `GraphBfsTest`:

```text
      1
     / \
    2   3
    |   |
    4   5
    |
    6
```

Starting at vertex `1`:

| Step | Queue before processing | Vertex processed | Queue after adding unvisited neighbors |
| --- | --- | --- | --- |
| 1 | `[1]` | `1` | `[2, 3]` |
| 2 | `[2, 3]` | `2` | `[3, 4]` |
| 3 | `[3, 4]` | `3` | `[4, 5]` |
| 4 | `[4, 5]` | `4` | `[5, 6]` |
| 5 | `[5, 6]` | `5` | `[6]` |
| 6 | `[6]` | `6` | `[]` |

Therefore, the test expects:

```text
1 2 3 4 5 6
```

The second test demonstrates that a BFS started at `1` does not visit a
separate component:

```text
1 --- 2 --- 3     4 --- 5
```

Its output is `1 2 3`, and vertices `4` and `5` remain unvisited. To traverse
the whole graph, start another BFS from each vertex that is still unvisited;
this is the approach used by `connectedComponent()`.

## Complexity

With an adjacency-list representation, BFS runs in `O(V + E)` time and uses
`O(V)` additional space, where `V` is the number of vertices and `E` is the
number of edges.
