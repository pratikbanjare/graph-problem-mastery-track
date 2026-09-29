# Detecting a Cycle in a Directed Graph

## Index

- [1. Learning goal](#1-learning-goal)
- [2. Start with a directed graph](#2-start-with-a-directed-graph)
- [3. Recognize what a cycle means](#3-recognize-what-a-cycle-means)
- [4. Track the DFS state](#4-track-the-dfs-state)
- [5. Walk through the example](#5-walk-through-the-example)
- [6. Handle disconnected graphs](#6-handle-disconnected-graphs)
- [7. Minimal pseudocode](#7-minimal-pseudocode)
- [8. Practice task](#8-practice-task)
- [9. BDD test location](#9-bdd-test-location)

## 1. Learning goal

Given a **directed** graph with `V` vertices and `E` edges, determine whether at
least one directed cycle exists.

The implementation to complete or study is:

- `src/main/java/practice/graph/GraphCycleDetector.java`
- Public entry point: `cycleDetector(Graph graph)`
- Adjacency lookup: `graph.getEdgesOfVertex(vertex)`

The key idea is to use depth-first search (DFS) and distinguish a vertex that is
being explored now from one that was completely explored earlier.

## 2. Start with a directed graph

An edge `u -> v` can be followed only from `u` to `v`. For example:

```text
Vertices: 1, 2, 3, 4

1 -> 2
2 -> 3
1 -> 3
3 -> 4
4 -> 1
```

Before deciding whether this graph is cyclic, follow one DFS path from vertex
`1`: `1 -> 2 -> 3 -> 4`. The next edge is `4 -> 1`, so the search returns to
an ancestor on its current path. That is exactly the pattern that defines a
cycle: `1 -> 2 -> 3 -> 4 -> 1`.

## 3. Recognize what a cycle means

During DFS, an edge to a vertex can mean two different things:

- The vertex is on the **current DFS path**.
  - Returning to it closes a directed cycle.
- The vertex was **fully processed in an earlier DFS path**.
  - Reaching it again is not automatically a cycle; it may simply be a shared
    destination.

For the graph above, vertex `1` is still on the active path when `4 -> 1` is
examined. Therefore, the edge is a back edge and the graph is cyclic.

## 4. Track the DFS state

`GraphCycleDetector` uses an integer state array. For each vertex:

- `0` — unvisited; DFS has not started here.
- `1` — visiting; the vertex is on the current recursion path.
- `2` — processed; all outgoing edges were checked and it is no longer on the
  current path.

This three-state model matters because a boolean `visited` array cannot
distinguish an active ancestor from a vertex completed in another branch.

When DFS enters a vertex, change its state from `0` to `1`. After every
outgoing edge has been checked, change it to `2`:

```text
unvisited (0) -> visiting (1) -> processed (2)
```

While examining `vertex -> neighbor`:

- `state[neighbor] == 1` means a cycle was found.
- `state[neighbor] == 0` means recursively explore `neighbor`.
- `state[neighbor] == 2` means no new cycle is implied by this edge.

## 5. Walk through the example

Use the following graph:

```text
1 -> 2
2 -> 3
1 -> 3
3 -> 4
4 -> 1
```

The DFS progresses naturally:

1. Enter `1`: states are `1:visiting`, `2:unvisited`, `3:unvisited`,
   `4:unvisited`.
2. Follow `1 -> 2`; enter `2`.
3. Follow `2 -> 3`; enter `3`.
4. Follow `3 -> 4`; enter `4`.
5. Inspect `4 -> 1`. Vertex `1` is `visiting`, so report `true`
   immediately.

Now remove `4 -> 1`:

1. DFS still explores `1 -> 2 -> 3 -> 4`.
2. Vertex `4` has no unexplored outgoing edge, so it becomes `processed`.
3. The recursion finishes and all reachable vertices become `processed`.
4. No edge points to a `visiting` vertex, so report `false`.

## 6. Handle disconnected graphs

DFS from vertex `1` cannot reach every component. Therefore, the public method
must start a DFS from **each vertex that is not processed**.

For example:

```text
1 -> 2          3 -> 4
                4 -> 5
                5 -> 3
```

The first DFS processes the acyclic component `1 -> 2`. The outer loop must
then start another DFS at `3`; that search finds `3 -> 4 -> 5 -> 3`.

This is why the outer loop in `cycleDetector(Graph graph)` is as important as
the recursive helper: it guarantees that every component is examined.

## 7. Minimal pseudocode

```text
state[1..V] = 0

for each vertex:
    if state[vertex] != 2 and dfs(vertex):
        return true
return false

dfs(vertex):
    state[vertex] = 1
    for each neighbor of vertex:
        if state[neighbor] == 1:
            return true
        if state[neighbor] == 0 and dfs(neighbor):
            return true
    state[vertex] = 2
    return false
```

## 8. Practice task

Implement or complete the missing cycle-detection section in
`src/main/java/practice/graph/GraphCycleDetector.java`.

- Use the three DFS states exactly as described above.
- Check every connected component.
- Return `true` as soon as a back edge is found.
- Run `GraphCycleDetectorBddTest` after your implementation.

The BDD scenarios are the executable check that the implementation handles
cycles, acyclic graphs, self-loops, and disconnected components correctly.

## 9. BDD test location

The feature file associated with `GraphCycleDetectorBddTest` is:

`../src/test/resources/features/chapter/topic_05/graph_cycle_detector.feature`
