# Kahn's Algorithm: Topological Sorting

## Index

- [The ordering problem](#the-ordering-problem)
- [In-degree: what is ready to come next?](#in-degree-what-is-ready-to-come-next)
- [A worked example](#a-worked-example)
- [Why the queue produces a topological order](#why-the-queue-produces-a-topological-order)
- [Detecting cycles](#detecting-cycles)
- [Complexity](#complexity)
- [Bare-minimum pseudocode](#bare-minimum-pseudocode)
- [BDD feature and test](#bdd-feature-and-test)
- [Implementation exercise](#implementation-exercise)

## The ordering problem

A **topological ordering** of a directed graph is a sequence of its vertices in
which every edge points forward: for each edge `u -> v`, `u` appears before `v`.
It is useful for ordering tasks with prerequisites, such as courses, build
steps, or dependent jobs.

Such an ordering exists only when the directed graph has no cycle. Kahn's
algorithm constructs the ordering by repeatedly choosing a vertex whose
prerequisites have all been handled. To know which vertices are ready, first
count their incoming edges.

## In-degree: what is ready to come next?

The **in-degree** of a vertex is the number of directed edges entering it.

- A vertex with in-degree `0` has no remaining prerequisites, so it can be
  placed next in the ordering.
- When a vertex is placed, each outgoing edge from it satisfies one
  prerequisite of its destination. Decrement that destination's in-degree.
- A destination becomes ready exactly when its in-degree reaches `0`.

The implementation uses vertices numbered from `1` through `V`, matching the
graph representation in `KahnsAlgorithm.java`.

## A worked example

Consider this directed acyclic graph:

```text
1: 2, 3, 4
2: 3, 4
3: 4, 5
4: -
5: -
```

Its edges are `1 -> 2`, `2 -> 3`, `1 -> 3`, `2 -> 4`, `3 -> 4`, `1 -> 4`,
and `3 -> 5`. Therefore, the initial in-degree counts for vertices `1` through
`5` are `[0, 1, 2, 3, 1]`. Vertex `1` is the only ready vertex, so the queue
starts as `[1]`.

Follow the queue one vertex at a time:

1. Remove `1` and append it to the result. Its outgoing edges make `2`'s
   in-degree `0`, so enqueue `2`; `3` and `4` are not ready yet.
2. Remove `2` and append it. Its edges make `3`'s in-degree `0`, so enqueue
   `3`; `4` still has one incoming edge remaining.
3. Remove `3` and append it. Its edges make both `4` and `5` ready, so enqueue
   them.
4. Remove and append `4`, then remove and append `5`.

The resulting order is `[1, 2, 3, 4, 5]`. Each edge points from an earlier
vertex to a later one. Other graphs can have more than one valid order: when
several vertices have in-degree `0`, any of them may be selected next.

## Why the queue produces a topological order

The queue contains only vertices whose incoming edges have all been removed
from consideration. Therefore, when a vertex is appended to the result, every
predecessor of that vertex has already been appended. This guarantees that no
edge points backward in the result.

The implementation uses a FIFO queue and scans vertices in numeric order when
seeding it. These choices make its output deterministic for a given graph, but
the particular output is not the definition of correctness; the edge ordering
property is.

## Detecting cycles

If the graph contains a cycle, its vertices can never become ready: each still
has at least one incoming edge from within the cycle. The queue eventually
empties before all vertices have been appended.

- If the result contains all `V` vertices, the graph is a DAG and the result is
  a topological ordering.
- If fewer than `V` vertices were processed, a cycle remains, so the
  implementation throws `GraphException`.

A self-loop (`u -> u`) is a cycle too: it contributes to `u`'s in-degree, and
that count can never reach zero.

## Complexity

The algorithm counts each edge once to compute in-degrees and processes each
vertex and edge at most once.

- **Time:** `O(V + E)`
- **Extra space:** `O(V)` for the in-degree array, queue, and result

## Bare-minimum pseudocode

```text
count incoming edges for every vertex
enqueue every vertex with in-degree 0
while queue is not empty:
    remove a vertex; append it to the order
    decrement each neighbor's in-degree; enqueue neighbors reaching 0
if order has fewer than V vertices: report a cycle
```

## BDD feature and test

- Feature: [`kahns_algorithm_for_topological_sort.feature`](../src/test/resources/features/chapter/topic_07/kahns_algorithm_for_topological_sort.feature)
- Step definitions: [`KahnsAlgorithmStep.java`](../src/test/java/chapter/topic_07/KahnsAlgorithmStep.java)
- BDD test suite: [`KahnsAlgorithmBddTest.java`](../../../src/test/java/practice/kahns/KahnsAlgorithmBddTest.java)
- Run the test suite with: `mvn -Dtest=KahnsAlgorithmBddTest test`

## Implementation exercise

Now implement the steps above in
[`KahnsAlgorithm.java`](../src/main/java/practice/graph/KahnsAlgorithm.java).
Pay special attention to calculating in-degrees, maintaining the queue, and
checking whether every vertex was processed. Then run the BDD test described
above; a passing test confirms that the implementation satisfies the behavior
covered by its scenarios.
