# Dijkstra's Shortest-Path Algorithm

## Index

- [1. Why this algorithm exists](#1-why-this-algorithm-exists)
- [2. The idea behind the greedy choice](#2-the-idea-behind-the-greedy-choice)
- [3. The graph we will use as an example](#3-the-graph-we-will-use-as-an-example)
- [4. How the priority queue drives the process](#4-how-the-priority-queue-drives-the-process)
- [5. Why negative weights are not allowed](#5-why-negative-weights-are-not-allowed)
- [6. Bare-minimum pseudocode](#6-bare-minimum-pseudocode)
- [7. Practice task](#7-practice-task)
- [8. BDD and test locations](#8-bdd-and-test-locations)

## 1. Why this algorithm exists

Breadth-first search is perfect when every edge costs the same amount. But in
real graphs, roads, links, or dependencies often have different weights. The
shortest path is no longer “fewest edges”; it becomes “minimum total cost.”

This is exactly where Dijkstra's algorithm becomes useful:

- It works for weighted graphs with non-negative edge costs.
- It grows the shortest known distance from the source one step at a time.
- It always picks the next vertex with the currently smallest tentative cost.

The implementation to study or complete is:

- `../src/main/java/practice/path/ShortestPath.java`
- Core methods: `djikstra(Graph graph, int source)` and `djikstraPath(...)`

## 2. The idea behind the greedy choice

The key insight is this:

- Maintain `distance[v]` as the best-known total weight from the source to `v`.
- For each edge `u -> v` with weight `w`, try the candidate:

  `candidate = distance[u] + w`

- If `candidate < distance[v]`, then the route through `u` is better, so update:

  `distance[v] = candidate`

This is called relaxation.

The subtle but important part is that we do not blindly update everything in
any order. We only keep exploring from the vertex whose current best-known cost
is the smallest. That is why we use a priority queue.

## 3. The graph we will use as an example

Consider this weighted directed graph:

```text
        1
       / \
      2   3
     / \  /
    4   5
     \  /
        6
```

with edge weights:

```text
1 -> 2 (4)
1 -> 3 (2)
2 -> 3 (1)
2 -> 5 (5)
3 -> 2 (2)
3 -> 4 (3)
4 -> 6 (1)
5 -> 6 (2)
```

If we start from vertex `1`, here is the natural thinking:

- Distance to `1` is `0`.
- The direct neighbors are `2` and `3`.
- `1 -> 3` is cheaper than `1 -> 2`, so `distance[3]` becomes `2`.
- `distance[2]` becomes `4`.

Now the algorithm picks the smallest tentative distance among all explored
vertices: `3` is smaller than `2`, so it expands `3` next.

From `3` we discover:

- `3 -> 2` gives a cheaper route to `2`: `2 + 2 = 4` (same as before)
- `3 -> 4` gives `2 + 3 = 5`

Then we expand `2` (distance `4`), and discover a cheaper route to `5`:

- `2 -> 5` = `4 + 5 = 9`

Finally, from `4` or `5`, we can reach `6` using the cheapest route. The best
cost to `6` is the minimum among all discovered candidates.

This is the heart of the algorithm: always choose the frontier vertex with the
smallest current distance, then relax its outgoing edges.

## 4. How the priority queue drives the process

A normal queue is not enough because we need to always remove the currently
smallest candidate first. That is why Dijkstra uses a min-priority queue.

```java
class DistanceEntry {
    int vertex;
    int distance;
}

PriorityQueue<DistanceEntry> pq =
    new PriorityQueue<>((a, b) -> Integer.compare(a.getDistance(), b.getDistance()));
```

The queue stores `(vertex, tentativeDistance)` pairs.

At each step:

- poll the minimum-distance entry
- ignore it if it is stale
- relax all neighbors from that vertex
- if a better route appears, push the new `(neighbor, updatedDistance)` pair

This ensures that when a vertex is processed, its recorded distance is already
as small as possible among all known options.

## 5. Why negative weights are not allowed

Dijkstra assumes every edge cost is non-negative.

Why?

- If a path contains a negative edge, a later route might appear artificially
  cheaper after a temporary “expensive” choice.
- The greedy choice would no longer be safe.
- A graph with a negative edge breaks the proof that the smallest tentative cost
  is always the correct next choice.

That is why the implementation contains a validation step:

```java
if (weightedEdge.getWeight() < 0) {
    throw new GraphException(...);
}
```

So the algorithm is intentionally defensive.

## 6. Bare-minimum pseudocode

```text
distance[source] = 0
priorityQueue.add((source, 0))

while priorityQueue is not empty:
    current = priorityQueue.poll()

    if current.distance > distance[current.vertex]:
        continue

    for each neighbor of current.vertex:
        candidate = distance[current.vertex] + weight(current, neighbor)

        if candidate < distance[neighbor]:
            distance[neighbor] = candidate
            parent[neighbor] = current.vertex
            priorityQueue.add((neighbor, candidate))

return distance[]
```

For rebuilding the shortest path itself, follow the `parent` pointers backward
from the target to the source, then reverse the list.

## 7. Practice task

Implement or complete the Dijkstra logic in:

`../src/main/java/practice/path/ShortestPath.java`

Focus on these ideas:

- initialize the distance array with `Integer.MAX_VALUE`
- keep the source at distance `0`
- use a min-priority queue to pick the next lightest frontier node
- relax every outgoing edge
- reject negative-weight graphs during validation
- reconstruct the path using the `parent` array

Then run the BDD test:

```bash
mvn -Dtest=ShortestPathBddTest test
```

A passing result means the missing Dijkstra logic is implemented correctly.

## 8. BDD and test locations

The feature file associated with the Dijkstra BDD suite is:

- `../src/test/resources/features/shortestPath/Dijkstras_shortest_path.feature`

The step-definition file is:

- `../src/test/java/practice/path/dikstra/ShortestPathSteps.java`

The test runner is:

- `../src/test/java/practice/path/dikstra/ShortestPathBddTest.java`

For the chapter-based organization, this topic belongs to:

- `chapter.topic_08`

and the corresponding feature file is arranged as:

- `../src/test/resources/features/chapter/topic_08/dijkstra_shortest_path.feature`

If the BDD test passes, the shortest-path behavior it covers—distance updates,
path reconstruction, unreachable vertices, source-target identity, and negative
weight rejection—is implemented correctly.

### Quick checkpoint

Ask yourself:

- Why is the priority queue needed instead of a normal queue?
- Why does a stale entry get ignored?
- Why do we reject negative weights?
- What does a `parent` pointer actually reveal about the path?

If you can answer these, you are already thinking in the same way as the
algorithm itself.

