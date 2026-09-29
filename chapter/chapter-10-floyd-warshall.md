# Chapter 10: Floyd-Warshall - all-pairs shortest paths

## Index

- [1. The problem](#1-the-problem)
- [2. A graph to reason about](#2-a-graph-to-reason-about)
- [3. Building the distance matrix](#3-building-the-distance-matrix)
- [4. Relaxing through an intermediate vertex](#4-relaxing-through-an-intermediate-vertex)
- [5. Negative edges and negative cycles](#5-negative-edges-and-negative-cycles)
- [6. Complexity and when to use it](#6-complexity-and-when-to-use-it)
- [7. Bare-minimum pseudocode](#7-bare-minimum-pseudocode)
- [8. BDD practice](#8-bdd-practice)
- [9. Your implementation task](#9-your-implementation-task)

## 1. The problem

Suppose a graph has many possible start and destination vertices. Running a
single-source algorithm once is not enough: we want the shortest distance for
**every ordered pair** `(source, destination)`.

Floyd-Warshall solves this by gradually answering a more specific question:

> What is the shortest path from `i` to `j` if only vertices `1..k` may be
> used as intermediate vertices?

When `k` increases, one new possibility becomes available. That observation
leads directly to dynamic programming.

## 2. A graph to reason about

Use this directed graph as a running example:

```text
1 --4--> 2 --3--> 3 --2--> 4
 \----------------10-------->
```

- The direct path `1 -> 3` costs `10`.
- The route `1 -> 2 -> 3` costs `4 + 3 = 7`, so it is better.
- The route `1 -> 2 -> 3 -> 4` costs `4 + 3 + 2 = 9`.
- There is no route from `4` to `1`; that pair is unreachable.

The implementation uses a one-based matrix, so `distance[i][j]` means the
current best distance from vertex `i` to vertex `j`. Unreachable pairs use
`Integer.MAX_VALUE`, which acts as infinity.

## 3. Building the distance matrix

Before considering intermediate vertices, initialize the matrix:

- Set every entry to infinity.
- Set `distance[i][i] = 0`; staying at a vertex costs nothing.
- Copy each direct edge into the matrix.
- If parallel edges exist, retain the smaller edge weight.

For the example, the useful initial entries are:

| Pair | Initial distance |
| --- | ---: |
| `1 -> 1`, `2 -> 2`, `3 -> 3`, `4 -> 4` | `0` |
| `1 -> 2` | `4` |
| `2 -> 3` | `3` |
| `3 -> 4` | `2` |
| `1 -> 3` | `10` |
| `4 -> 1` | infinity |

Now ask: can allowing vertex `2` as an intermediate improve `1 -> 3`?
The candidate is `distance[1][2] + distance[2][3] = 4 + 3 = 7`, so the matrix
should replace `10` with `7`.

## 4. Relaxing through an intermediate vertex

For each possible intermediate vertex `k`, inspect every pair `(i, j)`:

```text
candidate = distance[i][k] + distance[k][j]
distance[i][j] = min(distance[i][j], candidate)
```

The two nested lookups explain the recurrence:

- `distance[i][k]` reaches the intermediate vertex.
- `distance[k][j]` leaves it for the destination.
- Their sum is a path from `i` to `j` through `k`.

The outer loop **must** be `k`. After processing `k`, every matrix entry
represents paths whose allowed intermediate vertices are among `1..k`.
The implementation in `practice.path.FloydWarshall` follows this ordering.
It also checks both halves of a candidate against infinity before adding them,
so an unreachable path is never treated as a real numeric distance.

## 5. Negative edges and negative cycles

A negative edge is valid. For example, if `2 -> 3` costs `-6`, the algorithm
can correctly prefer a route whose total cost becomes negative.

A negative cycle is different:

- A cycle with total weight below zero can be repeated indefinitely.
- Each repetition makes the path cheaper.
- Therefore, there is no finite shortest-path answer for affected pairs.

After all relaxations, Floyd-Warshall detects this when some
`distance[v][v] < 0`. The implementation throws `GraphException` rather than
returning misleading distances.

## 6. Complexity and when to use it

- **Time:** `O(V^3)` because it examines every `(k, i, j)` combination.
- **Space:** `O(V^2)` for the distance matrix.
- It is a good choice when distances between many or all pairs are needed.
- It supports negative edges, but not graphs with negative cycles.
- It can represent disconnected graphs; unreachable pairs remain infinity.

## 7. Bare-minimum pseudocode

```text
distance = infinity matrix
for each vertex v:
    distance[v][v] = 0
for each edge (u, v, weight):
    distance[u][v] = min(distance[u][v], weight)

for k from 1 to V:
    for i from 1 to V:
        for j from 1 to V:
            if distance[i][k] != infinity
                    and distance[k][j] != infinity:
                distance[i][j] = min(
                    distance[i][j],
                    distance[i][k] + distance[k][j]
                )

for v from 1 to V:
    if distance[v][v] < 0:
        report a negative cycle
return distance
```

## 8. BDD practice

The executable examples are in:

`src/test/resources/features/chapter/topic_10/floyd_warshall.feature`

The suite and step definitions are in:

- `src/test/java/chapter/topic_10/FloydWarshallBddTest.java`
- `src/test/java/chapter/topic_10/FloydWarshallSteps.java`

The feature covers:

- chained paths that beat a direct edge;
- negative edges;
- negative-cycle detection;
- zero self-distances; and
- unreachable pairs remaining at `Integer.MAX_VALUE`.

The disconnected-pair scenario is important because it verifies the
infinity guard in the relaxation step, not just successful reachable paths.

## 9. Your implementation task

Open `src/main/java/practice/path/FloydWarshall.java` and implement or review
the missing Floyd-Warshall section using the ideas above.

- Preserve the matrix conventions used by `Graph` and this lesson.
- Do not add two infinity values.
- Detect a negative cycle after relaxation.
- Run `FloydWarshallBddTest` and use a failing assertion's printed matrix and
  exception details to locate the defect.

The BDD test passing is the signal that the implementation handles the
explained behavior.
