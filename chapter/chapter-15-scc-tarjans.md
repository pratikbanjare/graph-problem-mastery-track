# Topic 15: Strongly Connected Components with Tarjan's Algorithm

## Index

- [Learning path](#learning-path)
- [1. What makes a component strongly connected?](#1-what-makes-a-component-strongly-connected)
- [2. The question DFS must answer](#2-the-question-dfs-must-answer)
- [3. Discovery time and low-link](#3-discovery-time-and-low-link)
- [4. The active stack and SCC roots](#4-the-active-stack-and-scc-roots)
- [5. Trace an example graph](#5-trace-an-example-graph)
- [6. Connect the ideas to the implementation](#6-connect-the-ideas-to-the-implementation)
- [7. Complexity and boundaries](#7-complexity-and-boundaries)
- [8. Bare-minimum pseudocode](#8-bare-minimum-pseudocode)
- [9. Your implementation task](#9-your-implementation-task)
- [BDD feature and test locations](#bdd-feature-and-test-locations)
- [Scenario coverage analysis](#scenario-coverage-analysis)

## Learning path

We will answer these questions in order:

1. What does it mean for directed vertices to belong to the same component?
2. How can one DFS determine whether a search branch can return to an earlier
   vertex?
3. Which vertices should remain active while that return is still possible?
4. How does the active stack tell us when a complete component has been found?
5. Where do these ideas appear in the Java implementation and BDD scenarios?

## 1. What makes a component strongly connected?

In a directed graph, a path from `u` to `v` does not guarantee a path from `v`
back to `u`. Vertices are in the same **strongly connected component (SCC)**
only when every vertex in the group can reach every other vertex in the group.

- A pair of opposite-direction paths joins vertices into one SCC.
- A one-way edge between SCCs does not merge them.
- Every vertex belongs to exactly one SCC, including isolated vertices.

For example, `4 -> 5` and `5 -> 4` put `4` and `5` together. An edge
`2 -> 4` does not also include `2` unless there is a path back from `4` to `2`.

The definition asks about return paths. The next question is how one DFS can
notice the existence of such a return path efficiently.

## 2. The question DFS must answer

During DFS, each vertex is discovered once. For the vertex currently being
explored, consider:

- its DFS-tree children, which may lead farther down the search;
- edges from its descendants back to an ancestor;
- edges to vertices that have already been fully processed.

Only a path that reaches an ancestor which is still part of the active search
can show that the current DFS branch can return into an earlier part of the
search. A completed SCC is no longer a valid return route for the active DFS
tree.

To keep track of how far a branch can reach back, give each vertex a discovery
number and maintain a low-link value.

## 3. Discovery time and low-link

When DFS first visits vertex `v`, assign it the next increasing
**discovery time**. Set `lowLink[v]` to that time initially.

`lowLink[v]` is the smallest discovery time reachable from `v`'s DFS subtree
using DFS-tree edges and a back edge to a vertex that is still active. Update
it in these cases:

- After recursively visiting an undiscovered neighbor `w`, combine the
  subtree's result: `lowLink[v] = min(lowLink[v], lowLink[w])`.
- For an already discovered neighbor `w` that is still on the stack, use its
  discovery time: `lowLink[v] = min(lowLink[v], discoveryTime[w])`.
- Ignore an already discovered neighbor that is no longer on the stack: it
  belongs to a component that has already been completed.

The last distinction is easy to miss. A directed edge to a finished vertex
does not prove that the current vertex can return to that earlier search
branch. The stack tells us which discovered vertices are still active.

Once low-link values are available, we can identify the moment an entire
component is ready to be removed from that stack.

## 4. The active stack and SCC roots

Push each newly discovered vertex onto a stack and mark it as active. Do not
remove it when its recursive call returns; it may still be connected through
an ancestor to vertices that are being explored.

When `lowLink[v] == discoveryTime[v]`, no vertex reachable from `v`'s DFS
subtree can reach an earlier active vertex. Therefore `v` is the root of an
SCC:

- pop vertices from the top of the stack;
- mark each popped vertex inactive;
- stop after popping `v`;
- save the popped vertices as one SCC.

The stack contains exactly the unfinished vertices, so it both supports
low-link updates and marks the boundary of a completed component. Let's trace
this process on a small graph.

## 5. Trace an example graph

Consider this directed graph, also used in the BDD feature:

```text
       +----> 2 ----+
       |            |
1 -----+            +----> 4 <----> 5
       |            |
       +----> 3 ----+
```

The edges are `1 -> 2`, `1 -> 3`, `2 -> 4`, `3 -> 4`, `4 -> 5`, and
`5 -> 4`. The bidirectional connection between `4` and `5` is a cycle, but
the edges leading into it do not lead back to `1`, `2`, or `3`.

Assume DFS visits outgoing edges in the order shown:

1. Discover `1`, then `2`, then `4`, then `5`. Their discovery times are
   `0`, `1`, `2`, and `3`.
2. At `5`, the edge to active vertex `4` lowers `lowLink[5]` to `2`.
   Returning from `5` carries that value to `4`, so `lowLink[4]` is also `2`.
3. Since `lowLink[4] == discoveryTime[4]`, pop `5` and `4`: they form one
   SCC. The later edges from `2` and `1` into `4` now point to a completed
   component and cannot lower those vertices' low-link values.
4. DFS next visits `3`. Its edge to completed vertex `4` is ignored for
   low-link purposes. Vertices `3`, `2`, and `1` each become roots of their
   own singleton SCC.

The groups are `{1}`, `{2}`, `{3}`, and `{4, 5}`. Notice why “discovered”
alone is not enough to classify an edge: `4` is visited when `3` sees it, but
it has already left the active stack.

We can now map each part of this trace to the implementation's arrays, stack,
and recursive method.

## 6. Connect the ideas to the implementation

Open `../src/main/java/practice/scc/TarjanAlgorithm.java` and follow
`stronglyConnectedComponents` and its private `dfs` helper:

- `discoveryTIme` stores first-visit times; `-1` means undiscovered.
- `lowLink` stores the earliest active discovery reachable from each DFS
  subtree.
- `onStack` distinguishes active vertices from vertices in completed SCCs.
- `stack` contains discovered vertices whose SCC has not yet been emitted.
- `time[0]` is shared across recursive calls so each discovery time is unique.
- The outer loop starts DFS from every undiscovered vertex, covering
  disconnected parts of the graph too.
- The `lowLink[vertex] == discoveryTIme[vertex]` condition recognizes an SCC
  root; the pop loop collects exactly that component.

The graph uses vertex labels `1` through `V`, which explains why the arrays
have `V + 1` entries and leave index `0` unused. The order of SCCs and of
vertices within each SCC is not a required result; only the partition matters.
Tarjan's method computes all SCCs in one DFS traversal.

## 7. Complexity and boundaries

Each vertex is discovered and pushed once, and each directed edge is examined
once:

- **Time:** `O(V + E)`.
- **Space:** `O(V)` for the arrays, recursion stack, and active stack, apart
  from the returned SCC lists.

The algorithm depends on directed edges. It rejects undirected graphs because
their edge semantics do not match the directed reachability problem.

## 8. Bare-minimum pseudocode

```text
for each vertex:
    if undiscovered:
        dfs(vertex)

dfs(v):
    assign discoveryTime[v]; lowLink[v] = discoveryTime[v]
    push v; mark v on stack
    for each neighbor w:
        if w undiscovered:
            dfs(w); lowLink[v] = min(lowLink[v], lowLink[w])
        else if w is on stack:
            lowLink[v] = min(lowLink[v], discoveryTime[w])
    if lowLink[v] == discoveryTime[v]:
        pop through v and save those vertices as one SCC
```

## BDD feature and test locations

The BDD feature is
`../src/test/resources/features/chapter/topic_15/tarjan_algorithm.feature`.
Its step definitions are in
`../src/test/java/chapter/topic_15/TarjanAlgorithmSteps.java`; the runner is
`../src/test/java/chapter/topic_15/TarjanAlgorithmBddTest.java`.

## Scenario coverage analysis

The original feature covered a cycle, several SCCs, and an acyclic graph, but
did not exercise some important Tarjan cases. It now also covers isolated
vertices, a self-loop, and an edge from a later DFS root into an already
completed SCC. That last case specifically checks that an edge to a vertex
which is no longer on the stack does not incorrectly lower a low-link value.
The directed-cycle and multiple-component scenarios continue to exercise
back-edge handling and SCC-root popping. An undirected-input rejection case
is also included. Isolated vertices are checked both alone and alongside a
nontrivial SCC.

## 9. Your implementation task

Implement the concepts in
`../src/main/java/practice/scc/TarjanAlgorithm.java`. Work through the
discovery-time, low-link, active-stack, and SCC-root steps rather than trying
to group vertices from reachability in only one direction.

Then run the BDD test yourself:

```text
mvn -Dtest=chapter.topic_15.TarjanAlgorithmBddTest test
```

If an assertion fails, use the step-definition logs to compare the input
graph, expected SCC partition, and actual output. A successful BDD run means
the implementation passes these scenarios; it is strong feedback, not a
proof for every possible graph.