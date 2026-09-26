# Bipartite Graph

## Index

* [What a bipartite graph means](#what-a-bipartite-graph-means)
* [A worked example](#a-worked-example)
* [Why coloring detects the answer](#why-coloring-detects-the-answer)
* [BFS strategy](#bfs-strategy)
* [How the implementation maps to the idea](#how-the-implementation-maps-to-the-idea)
* [Running the BDD test](#running-the-bdd-test)
* [BDD coverage and missing scenarios](#bdd-coverage-and-missing-scenarios)
* [Your implementation exercise](#your-implementation-exercise)

## What a bipartite graph means

* A graph is **bipartite** when its vertices can be divided into two groups so
  that every edge connects one group to the other.
* No edge may join two vertices inside the same group.
* This is also a coloring problem:
  * Use two colors.
  * Require every edge to have different-colored endpoints.
* Thinking in colors leads naturally to the algorithm used by
  `BipartiteChecker`.

## A worked example

Consider this undirected graph:

```text
1 -- 2 -- 3
|         |
4 --------+
```

* Edges are `(1, 2)`, `(2, 3)`, `(3, 4)`, and `(4, 1)`.
* Start by coloring vertex `1` with color `1`.
* Every neighbor must receive the opposite color:

```text
Group 1 (color 1): 1, 3
Group 2 (color 0): 2, 4
```

* Every edge crosses between the two groups, so this even cycle is bipartite.
* Now add edge `(1, 3)`:
  * Vertices `1` and `3` already have the same color.
  * The edge creates a conflict.
  * The resulting odd cycle `1-2-3-1` is not bipartite.

## Why coloring detects the answer

* When we visit an uncolored vertex, assign it a color opposite to the vertex
  we came from.
* If an edge has endpoints with the same color:
  * The two-color requirement is impossible.
  * The graph is not bipartite.
* The graph may contain several disconnected components:
  * A single isolated vertex is a valid component.
  * Two separate valid components are collectively bipartite.
  * The algorithm must start a traversal from every uncolored vertex, not only
    from vertex `1`.

## BFS strategy

* Breadth-first search (BFS) is a convenient way to propagate colors:
  * Keep a `color` array initialized to `-1` for every vertex.
  * For each uncolored vertex, put it in a queue and assign color `1`.
  * Remove vertices from the queue one at a time.
  * Color each uncolored neighbor with `1 - color[current]`.
  * If a colored neighbor has the same color as `current`, return `false`.
  * If all components finish without a conflict, return `true`.
* The `Graph` class numbers vertices from `1` through `getVertexCount()`.
  * The implementation allocates one extra array position.
  * Array index `0` is ignored.

## How the implementation maps to the idea

* Open `src/main/java/practice/graph/BipartiteChecker.java` while reading.
* `isBipartite`:
  * Creates the color array.
  * Fills it with `-1`.
  * Scans all vertices so disconnected components are included.
* `isBipartiteInternal` performs the BFS for one component.
* `queue` provides FIFO traversal order.
* `color[neighbor] = color[v] == 1 ? 0 : 1` assigns the opposite group.
* The equal-color branch returns `false` as soon as an edge violates the rule.
* The class relies on `Graph.getEdgesOfVertex(v)`, which returns both
  directions for the default undirected graph.

## Minimal pseudocode

```text
color all vertices as -1

for each vertex s:
    if color[s] is -1:
        color[s] = 1
        put s in queue
        while queue is not empty:
            v = remove from queue
            for each neighbor of v:
                if color[neighbor] is -1:
                    color[neighbor] = 1 - color[v]
                    put neighbor in queue
                else if color[neighbor] == color[v]:
                    return false

return true
```

## Running the BDD test

* The BDD test is
  `src/test/java/practice/bipartite/BipartiteCheckerBddTest.java`.
* It loads the feature directory through Cucumber.
* The step definitions are in
  `src/test/java/practice/bipartite/bdd/BipartiteCheckerSteps.java`.
* The associated BDD feature is
  `../../../src/test/resources/features/chapter/topic_04/bipartite_checker.feature`.
* Run the test with:

```bash
mvn -Dtest=practice.bipartite.BipartiteCheckerBddTest test
```

* The test output and assertion logs show:
  * The expected result.
  * The actual result.
  * The vertex count.
  * The edge count.
* A successful run confirms the behavior exercised by the feature scenarios.

## BDD coverage and missing scenarios

* The feature currently checks:
  * One edge, which is the smallest non-empty bipartite graph.
  * A triangle, which is an odd cycle and must fail.
  * A four-vertex cycle, which is an even cycle and must pass.
* Important related scenarios are still missing:
  * A disconnected graph, to verify that every component is traversed.
  * A graph with isolated vertices or no edges.
  * A self-loop such as `(1, 1)`, which is an immediate same-color conflict.
  * A larger graph with multiple components, combining a valid component and
    an odd-cycle component.
* These scenarios would strengthen the feature's coverage of the branches in
  `BipartiteChecker`, especially the outer loop over disconnected components.

## Your implementation exercise

* Implement the explained two-color BFS in
  `src/main/java/practice/graph/BipartiteChecker.java`.
* Use `-1` for unvisited vertices.
* Alternate between colors `0` and `1`.
* Detect same-color neighbors.
* Process disconnected components.
* Run `BipartiteCheckerBddTest` after your implementation.
  * A passing BDD test means your implementation is correct for the scenarios
    currently described in the feature file.
  * The missing scenarios above are good next tests to add.
