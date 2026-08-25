# Problem 1 : Graph Representation 

Implement an undirected graph in Java using an adjacency list.

Suppose we have 

```text
0 --- 1
|     |
|     |
2 --- 3
```

The graph contains 

```text
0 - 1
0 - 2
1 - 3
2 - 3
```

Create a class concemptually like 

```java
class Graph {
    // your fields

    Graph(int vertices) {
        // initialize
    }

    void addEdge(int u, int v) {
        // ...
    }

    boolean hasEdge(int u, int v) {
        // ...
    }

    void removeEdge(int u, int v) {
        // ...
    }

    void printGraph() {
        // ...
    }
}
```

### Requirements

Your graph should:

1. Store `V` vertices numbered `0 ... V-1`.
2. Represent the graph using an adjacency list.
3. `addEdge(u, v)` creates an undirected edge.
4. `hasEdge(u, v)` checks whether the edge exists.
5. `removeEdge(u, v)` removes the edge.
6. `printGraph()` displays every vertex and its neighbors.

For example, something like:

```text
0 -> 1 2
1 -> 0 3
2 -> 0 3
3 -> 1 2
```



