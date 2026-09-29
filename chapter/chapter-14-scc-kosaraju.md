# Topic 14: Strongly Connected Components with Kosaraju's Algorithm

## Index

- [Learning path](#learning-path)
- [1. What makes a component strongly connected?](#1-what-makes-a-component-strongly-connected)
- [2. Why one depth-first search is not enough](#2-why-one-depth-first-search-is-not-enough)
- [3. Let finishing times reveal the right order](#3-let-finishing-times-reveal-the-right-order)
- [4. Reverse the graph to isolate each component](#4-reverse-the-graph-to-isolate-each-component)
- [5. Trace the example graph](#5-trace-the-example-graph)
- [6. Connect the idea to the implementation](#6-connect-the-idea-to-the-implementation)
- [7. Complexity and boundaries](#7-complexity-and-boundaries)
- [8. Bare-minimum pseudocode](#8-bare-minimum-pseudocode)
- [9. Your implementation task](#9-your-implementation-task)
- [BDD feature and test locations](#bdd-feature-and-test-locations)
- [Scenario coverage analysis](#scenario-coverage-analysis)

## Learning path

We will build the algorithm from one question to the next:

1. When should two directed-graph vertices belong to the same group?
2. Why can a plain DFS accidentally mix several such groups?
3. What does the order in which DFS finishes vertices tell us?
4. How does reversing every edge help separate the groups?
5. How do those ideas map to the Java method and its BDD checks?

## 1. What makes a component strongly connected?

In a directed graph, a vertex can reach another without being able to return.
That is why ordinary connected-components reasoning is not enough.

Two vertices belong to the same **strongly connected component (SCC)** exactly
when each can reach the other by following directed edges. SCCs partition all
vertices:

- every vertex belongs to one SCC, including an isolated vertex;
- vertices in one SCC are mutually reachable;
- vertices in different SCCs are not mutually reachable.

For example, if the graph contains `4 -> 5` and `5 -> 4`, then `4` and `5`
are in one SCC. If it contains only `1 -> 2`, then `1` and `2` are in
different SCCs: there is no path from `2` back to `1`.

So we need a method that checks reachability in both directions without
running a separate search for every pair of vertices.

## 2. Why one depth-first search is not enough

A DFS from a vertex answers, “Which vertices can this vertex reach?” It does
not answer, “Which of those vertices can also reach it?”

Suppose an SCC has an edge leading out to another SCC. A DFS in the original
graph can leave the first component and visit vertices in the second. If we
group every newly visited vertex together, we merge vertices that cannot get
back to the starting component.

We therefore need two pieces of information:

- a careful order for choosing where the second search begins;
- a graph direction in which that search cannot leak into the wrong SCC.

The first DFS provides the ordering information through its **finishing
order**.

## 3. Let finishing times reveal the right order

In DFS, a vertex is finished only after all its outgoing neighbors have been
visited. Record each vertex at that moment, not when first discovered.

In the graph of SCCs, edges between components cannot form a directed cycle;
otherwise those components would be one SCC. The component that DFS finishes
later is therefore positioned so that processing components in decreasing
finish time helps expose one SCC at a time after the graph is reversed.

The first pass must cover the whole graph, not just vertex `1`:

- start DFS at every vertex that is still unvisited;
- append each vertex to a list when its DFS call finishes;
- reverse that list to process vertices in decreasing finishing time.

But the order alone does not stop a second DFS from crossing into other
components. For that, we change edge direction.

## 4. Reverse the graph to isolate each component

The **transpose** (or reversed graph) keeps all vertices and reverses each
directed edge: `u -> v` becomes `v -> u`.

Then process vertices in decreasing finishing time on this reversed graph:

- if a vertex is already visited, its SCC has already been collected;
- otherwise, start DFS there and collect all newly visited vertices as one SCC.

Why does this work? The first pass orders SCCs by their dependency structure.
In the transpose, the next unvisited SCC reached by that order cannot spill
into a different unvisited SCC. Thus each second-pass DFS returns exactly
one component.

With the two passes in place, we can trace a concrete graph and see why each
step is necessary.

## 5. Trace the example graph

Use this directed graph, also covered by the BDD feature:

```text
       +----> 2 ----+
       |            |
1 -----+            +----> 4 <----> 5
       |            |
       +----> 3 ----+
```

Its edges are `1 -> 2`, `1 -> 3`, `2 -> 4`, `3 -> 4`, `4 -> 5`, and
`5 -> 4`. The mutual edges make `{4, 5}` an SCC; the one-way paths into that
pair do not make `1`, `2`, or `3` part of it.

Assume DFS visits neighbors in the order they were added in the feature:

1. **First pass:** start at `1`, follow `1 -> 2 -> 4 -> 5`. Finish and record
   `5`, then `4`, then `2`. Back at `1`, visit `3`; its edge to `4` reaches an
   already visited vertex, so finish `3`, then `1`.
2. The finishing list is `[5, 4, 2, 3, 1]`; reverse it to get
   `[1, 3, 2, 4, 5]`.
3. **Transpose pass:** `1`, then `3`, then `2` each start a DFS that collects
   only that vertex. When the order reaches `4`, the reversed `4 <-> 5` cycle
   collects `{4, 5}` together.

The resulting SCCs are `{1}`, `{2}`, `{3}`, and `{4, 5}`. The order of SCCs
and the order of vertices inside an SCC are not meaningful; the grouping is.

Now we can connect each step in that trace to the existing Java implementation.

## 6. Connect the idea to the implementation

Open `../src/main/java/practice/scc/KosarajuAlgorithm.java` and map the concepts
to the method `stronglyConnectedAlgorithm`:

- The `GraphType.UNDIRECTED` guard rejects input that does not have directed
  reachability semantics.
- The first `visited` array ensures every vertex is included in the first
  DFS forest.
- `dfs(graph, vertex, visited, finishingOrder)` appends the vertex after its
  descendants, producing finishing order.
- `Collections.reverse(finishingOrder)` prepares decreasing finishing time.
- `GraphOperation.reverse(graph)` constructs the transpose without changing
  the original graph.
- The fresh second-pass `visited` array ensures vertices collected in one SCC
  are not collected again.
- Each second-pass DFS writes into one `scc` list, then adds that list to
  `sccLists`.

The graph numbers vertices from `1` through `V`, so the arrays allocate
`V + 1` slots and leave index `0` unused. The order returned by this method is
one valid traversal order, not a promised canonical order.

With both passes accounted for, the remaining question is whether this
approach is efficient enough for a full graph.

## 7. Complexity and boundaries

Each vertex and directed edge is examined a constant number of times across
the two DFS passes and the transpose construction:

- **Time:** `O(V + E)`.
- **Space:** `O(V + E)` for the transpose and traversal/result storage.

Kosaraju's algorithm applies to directed graphs. A graph with no edges still
has one singleton SCC per vertex; a cycle may form one larger SCC. The graph
API requires at least one vertex, so a zero-vertex graph cannot be constructed
through the current API.

## 8. Bare-minimum pseudocode

```text
for each vertex:
    if unvisited:
        DFS(original graph), recording vertex when finished

reverse finishing order
transpose = reverse every edge
clear visited

for each vertex in reversed finishing order:
    if unvisited:
        DFS(transpose), collecting one SCC
        save that SCC

return all SCCs
```

## 9. Your implementation task

Implement the concepts in
`../src/main/java/practice/scc/KosarajuAlgorithm.java`. Check that your method
performs both DFS passes, records vertices on DFS return, reverses the graph
before the second pass, and starts a new component only at an unvisited
vertex.

Then run the BDD suite yourself:

```text
mvn -Dtest=chapter.topic_14.KosarajuAlgorithmBddTest test
```

When a scenario fails, first read the step-definition log: it prints the
input graph, expected SCCs, actual SCCs, and exception details. A successful
BDD run shows that the implementation passes these example-based checks; it
does not prove correctness for every possible graph.

## BDD feature and test locations

The scenarios are in
`../src/test/resources/features/chapter/topic_14/kosaraju_algorithm.feature`.
The step definitions are in
`src/test/java/chapter/topic_14/KosarajuAlgorithmSteps.java`, and the BDD
runner is `src/test/java/chapter/topic_14/KosarajuAlgorithmBddTest.java`.

## Scenario coverage analysis

The feature covers directed cycles, several SCCs connected by one-way edges,
disconnected vertices, self-loops, one-way reachability, and rejection of an
undirected graph. One useful gap was a graph that combines a nontrivial SCC
with isolated vertices; the feature now includes that scenario to verify the
first DFS pass visits all vertices, not only vertices reachable from an edge.

The graph API disallows a zero-vertex graph, so an empty-graph scenario cannot
be expressed through the current `Graph` constructor.
