# Floyd-Warshall Algorithm

Floyd-Warshall is a dynamic programming algorithm used to find the shortest paths between **all pairs of vertices** in a weighted graph.

It works by gradually allowing more vertices to be used as intermediate points in a path.  
For each possible intermediate vertex `k`, it checks whether going from `i` to `j` through `k` is shorter than the currently known path.

## Core idea

If `distance[i][j]` is the shortest known distance from vertex `i` to vertex `j`, then the algorithm tries to improve it using:

```java
distance[i][j] = min(distance[i][j], distance[i][k] + distance[k][j]);
```

This means:
- keep the current shortest path, or
- use vertex `k` as a bridge between `i` and `j`

## Steps

1. Initialize the distance matrix.
2. Set `distance[i][i] = 0` for every vertex `i`.
3. Set direct edge weights where edges exist.
4. For every vertex `k`, try to improve every pair `(i, j)` using `k` as an intermediate vertex.

## Pseudocode

```java
for k = 1..V
    for i = 1..V
        for j = 1..V
            if distance[i][k] != INF and distance[k][j] != INF
                distance[i][j] = min(distance[i][j], distance[i][k] + distance[k][j])
```

## Time and space complexity

- **Time:** `O(V^3)`
- **Space:** `O(V^2)`

## Important notes

- Works with **negative edge weights**.
- Does **not** work correctly if the graph contains a **negative cycle**.
- Best used when you need shortest paths between all pairs of vertices.
