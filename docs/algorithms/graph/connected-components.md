# Connected Components

## BFS solution idea

Use BFS to find all connected components in an undirected graph:

1. Keep a `visited` set/array for all vertices.
2. Iterate through every vertex in the graph.
3. If a vertex is not visited, start a BFS from it.
4. During that BFS, collect all reached vertices into one component list.
5. Add that component list to the final list of components.
6. Continue until all vertices are visited.

Each BFS run discovers exactly one connected component.

## Pseudocode

```text
connectedComponents(graph):
    visited = set()
    components = []

    for each vertex in graph.vertices:
        if vertex not in visited:
            component = []
            queue = new Queue()
            queue.add(vertex)
            visited.add(vertex)

            while queue is not empty:
                current = queue.remove()
                component.add(current)

                for each neighbor in graph.neighbors(current):
                    if neighbor not in visited:
                        visited.add(neighbor)
                        queue.add(neighbor)

            components.add(component)

    return components
```

## How to use `ConnectedComponents` class

If you have a `ConnectedComponents` class, typical usage is:

```text
graph = ... // build graph with all vertices and edges
connectedComponents = new ConnectedComponents(graph)

result = connectedComponents.findAll()
print(result) // e.g. [[0, 1, 2], [3, 4], [5]]
```

Use `findAll()` (or the equivalent method in your implementation) to get a list of all components, where each inner list contains vertices belonging to one connected component.
