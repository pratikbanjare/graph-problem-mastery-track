# Topological Sorting with Depth-First Search

## Index

- [1. Learning goal](#1-learning-goal)
- [2. What must an ordering preserve?](#2-what-must-an-ordering-preserve)
- [3. The example graph](#3-the-example-graph)
- [4. Why DFS postorder gives the answer](#4-why-dfs-postorder-gives-the-answer)
- [5. Detecting when no answer exists](#5-detecting-when-no-answer-exists)
- [6. Include every component](#6-include-every-component)
- [7. Minimal pseudocode](#7-minimal-pseudocode)
- [8. BDD feature location](#8-bdd-feature-location)
- [9. Your implementation task](#9-your-implementation-task)

## 1. Learning goal

Suppose tasks have prerequisites. If task `u` must happen before task `v`, draw
a directed edge `u -> v`. A **topological ordering** is a sequence of all
vertices that respects every such prerequisite.

This is possible only when the directed graph is acyclic (a DAG). In this
lesson, focus on the DFS implementation in
`../src/main/java/practice/graph/TopologicalSortAlgorithm.java`:

- Entry point: `dfsTopologicalSort(Graph graph)`
- Recursive helper: `dfsTopologicalSortInternal(...)`
- Neighbors: `graph.getEdgesOfVertex(vertex)`

The class also contains Kahn's algorithm; that method is outside the scope of
this lesson.

## 2. What must an ordering preserve?

For every directed edge `u -> v`, `u` must appear before `v`. The ordering is
not necessarily unique: unrelated vertices can often swap places.

For example, if `1 -> 2` and `1 -> 3`, both `1, 2, 3` and `1, 3, 2` respect
those dependencies. To see how DFS constructs one such order, follow this
graph:

## 3. The example graph

```text
    1
   / \
  v   v
  2   3
   \ /
    v
    4
```

Its edges are `1 -> 2`, `1 -> 3`, `2 -> 4`, and `3 -> 4`. Vertex `1` is a
prerequisite of both branches, while `4` depends on both `2` and `3`.

Starting DFS at `1`, the implementation follows neighbors in their stored
order:

1. Visit `1`, then follow `1 -> 2`.
2. Visit `2`, then follow `2 -> 4`.
3. Vertex `4` has no outgoing edges, so append `4`.
4. Finish `2`, so append `2`.
5. Return to `1` and visit `3`. Its neighbor `4` is already finished, so
   append `3`.
6. Finish `1`, so append `1`.

DFS has produced **postorder** `[4, 2, 3, 1]`. But this places each vertex
after its descendants, the reverse of what a topological ordering needs. What
simple transformation puts prerequisites first?

## 4. Why DFS postorder gives the answer

Reverse the postorder to get `[1, 3, 2, 4]`. Check the original edges:

- `1` is before `2` and `3`.
- Both `2` and `3` are before `4`.

This is a valid topological order. In general, DFS completely explores a
vertex's outgoing paths before appending that vertex. Reversing this finishing
order moves each prerequisite before the vertices that depend on it.

Because another traversal order could produce `[1, 2, 3, 4]`, tests should
usually verify edge ordering and vertex membership rather than require one
exact sequence.

## 5. Detecting when no answer exists

Before accepting a result, DFS must determine whether the graph contains a
cycle. A cycle represents dependencies that lead back to an earlier task,
which makes a valid ordering impossible.

The helper uses three states for each vertex:

- `0` — unvisited.
- `1` — visiting; the vertex is still on the active recursive path.
- `2` — finished; all outgoing edges have been explored.

While examining `vertex -> neighbor`:

- State `0`: recursively explore the neighbor.
- State `1`: the neighbor is an active ancestor, so this edge closes a cycle.
- State `2`: that vertex was fully explored; this edge alone is not a cycle.

In the example, adding `4 -> 1` leads from `4` back to the active `1`, forming
`1 -> 2 -> 4 -> 1`. The DFS implementation reports this by throwing an
`IllegalArgumentException`.

## 6. Include every component

A search started at one vertex cannot visit a disconnected component. The
public method therefore loops over all vertices and starts DFS whenever a
vertex is still unvisited.

For example, the graph may contain `1 -> 2` and a separate `3 -> 4`. Both
components must contribute vertices to the final ordering. A single DFS from
`1` would miss `3` and `4`; the outer loop prevents that.

Together, the outer loop, three-state DFS, postorder append, and final reversal
cover the whole directed graph in `O(V + E)` time, with `O(V)` additional
space (including the recursion stack).

## 7. Minimal pseudocode

```text
state[1..V] = unvisited
postorder = []

for each vertex:
    if vertex is unvisited:
        visit(vertex)

reverse(postorder)
return postorder

visit(vertex):
    mark vertex visiting
    for each neighbor:
        if neighbor is visiting: report cycle
        if neighbor is unvisited: visit(neighbor)
    mark vertex finished
    append vertex to postorder
```

## 8. BDD feature location

The feature file exercised by `TopologicalSortBddTest` is:

`../src/test/resources/features/chapter/topic_06/topological_sorting.feature`

Run the test with Maven:

```text
mvn -Dtest=TopologicalSortBddTest test
```

The scenarios check vertex membership, edge ordering, disconnected DAG
components, and cycle failure. The current feature does not separately cover
a self-loop, an isolated vertex, or a disconnected graph whose component is
cyclic.

## 9. Your implementation task

Study and implement the DFS topological-sort behavior in
`../src/main/java/practice/graph/TopologicalSortAlgorithm.java`, concentrating
only on `dfsTopologicalSort` and its recursive helper. Make sure the method
visits every component, detects a back edge, and reverses the DFS finishing
order. Then run `TopologicalSortBddTest`; passing it confirms that the
implementation satisfies the behaviors currently described by the BDD
scenarios.
