# Connected Components

## Index

- [What is a connected component?](#what-is-a-connected-component)
- [How BFS works](#how-bfs-works)
- [Using BFS to find components](#using-bfs-to-find-components)
- [Scan all vertices](#scan-all-vertices)
- [Your turn](#your-turn)

## What is a connected component?

Consider this undirected graph:

```text
  1 --- 2       4 --- 5       6
        |
        3
```

An **edge** is a link between vertices. Vertices are connected when a path of edges links them.

In this graph:

- `1`, `2`, and `3` form one component: `1` reaches `3` through `2`.
- `4` and `5` form another component.
- Isolated vertex `6` is a component by itself.

A component is a maximal group: no path leads from its vertices to another group.

## How BFS works

Breadth-first search (BFS) explores a graph in layers, starting from one vertex. It uses a **queue** (first in, first out) to remember which vertex to visit next:

1. Add the start vertex to the queue and mark it visited.
2. Remove the front vertex from the queue and inspect its neighbors.
3. Add each unvisited neighbor to the back of the queue, marking it visited immediately.
4. Repeat until the queue is empty.

In the example graph, start at `1`. The queue shows what BFS will visit next:

| Step | Visit | Queue after adding unvisited neighbors |
|---|---:|---|
| Start | - | `[1]` |
| 1 | Remove `1`; add neighbor `2` | `[2]` |
| 2 | Remove `2`; add unvisited neighbor `3` | `[3]` |
| 3 | Remove `3`; no unvisited neighbors | `[]` |

Since the queue is empty, BFS is done. It visited `{1, 2, 3}`. If there were an extra edge `1-3`, BFS would see `3` already marked visited and would not enqueue it again.

```text
bfs(graph, start, visited):
    queue = [start]
    visited[start] = true
    component = []

    while queue is not empty:
        current = remove front of queue
        add current to component

        for each neighbor of current:
            if neighbor is not visited:
                visited[neighbor] = true
                add neighbor to back of queue

    return component
```

## Using BFS to find components

One BFS finds only the vertices reachable from its start. It cannot reach `4`, `5`, or `6` from `1`. Scan all vertices to find the other components.

## Scan all vertices

Scan vertices `1` through `V`, using the same `visited` array:

1. Skip a visited vertex; an earlier BFS already found its component.
2. For an unvisited vertex, start BFS and save all reached vertices as a new component.

For the example graph:

1. Start BFS at `1`; save `{1, 2, 3}`.
2. Skip `2` and `3`; they are already visited.
3. Start BFS at `4`; save `{4, 5}`.
4. Start BFS at `6`; save `{6}`.

The result is `[{1, 2, 3}, {4, 5}, {6}]`.

```text
connectedComponents(graph):
    visited = array of false values
    components = []

    for each vertex from 1 to V:
        if vertex is not visited:
            component = bfs(graph, vertex, visited)
            add component to components

    return components
```

## Your turn

Implement the connected-components behavior in `../src/main/java/practice/graph/ConnectedComponents.java`.

Remember:

- Share `visited` across searches.
- Mark neighbors visited when enqueuing them.

Run `../src/test/java/practice/graph/RunConnectedComponentsCucumberTest.java`. Its scenarios are in `../src/test/resources/features/chapter/topic_02/connected-components.feature`.

From the repository root, run:

```bash
mvn -Dtest=RunConnectedComponentsCucumberTest test
```

The BDD test checks:

- Disconnected groups.
- Isolated vertices.
- One connected group.

It also depends on `Graph.addEdge(...)` and `Graph.getEdgesOfVertex(...)`. Implement these graph operations if the test fails before comparing components.
