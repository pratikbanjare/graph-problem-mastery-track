# Chapter 1: Understand Graphs

Before writing graph algorithms, first learn what a graph is and how a
computer stores one. The `Graph` class in this repository will be our running
example.

## Index

1. [Start with a familiar problem](#1-start-with-a-familiar-problem)
2. [Understand an edge before storing it](#2-understand-an-edge-before-storing-it)
3. [Add meaning to an edge with a weight](#3-add-meaning-to-an-edge-with-a-weight)
4. [Decide how to store the graph](#4-decide-how-to-store-the-graph)
5. [Connect the representation to `Graph.java`](#5-connect-the-representation-to-graphjava)
6. [Follow the life of an edge](#6-follow-the-life-of-an-edge)
7. [Why this matters for algorithms](#7-why-this-matters-for-algorithms)
8. [Your implementation task](#8-your-implementation-task)

## 1. Start with a familiar problem

Imagine four places connected by roads:

```text
1 ----- 2
|       |
|       |
4 ----- 3
```

We want to answer questions such as:

- Which places are connected directly to place `1`?
- Is there a road from `1` to `3`?
- What is the total cost of travelling along a road?
- Can we add or remove a road?

The diagram is a **graph**. A graph gives us a way to model relationships
between things. The things are called **vertices** (or nodes), and the
relationships are called **edges**.

In this example:

```text
Vertices: {1, 2, 3, 4}
Edges:    {(1, 2), (2, 3), (3, 4), (4, 1)}
```

We often describe a graph as `G = (V, E)`, where `V` is the set of vertices
and `E` is the set of edges.

## 2. Understand an edge before storing it

The edge `(1, 2)` says that vertices `1` and `2` are directly connected. Such
vertices are called **neighbors** or **adjacent vertices**.

In the diagram, vertex `1` has two neighbors: `2` and `4`. The number of edges
connected to a vertex is its **degree**, so the degree of vertex `1` is `2`.

The diagram uses lines rather than arrows. Therefore, travelling from `1` to
`2` also allows travelling from `2` to `1`. This is an **undirected graph**:
`(1, 2)` and `(2, 1)` describe the same connection.

Sometimes direction matters. For example, a one-way road would be written as
`1 -> 2`. This is a **directed graph**: we can travel from `1` to `2`, but we
cannot assume that we can travel from `2` to `1`.

The `Graph` class supports both choices through `GraphType`:

```java
Graph roadMap = new Graph(4);                     // undirected by default
Graph oneWayMap = new Graph(4, GraphType.DIRECTED);
```

The number `4` creates vertices `1`, `2`, `3`, and `4`. This class uses
**1-based vertex labels**, so a vertex must be between `1` and the total
number of vertices. It does not use vertex `0`.

## 3. Add meaning to an edge with a weight

Not every connection has the same cost. If the road from `1` to `2` takes
five minutes, we can label it with weight `5`:

```text
1 --(5)--> 2
```

A graph without such values is called **unweighted**. A graph whose edges
carry values such as distance, cost, or time is **weighted**.

`Graph` supports both forms:

```java
roadMap.addEdge(1, 2);       // unweighted: weight defaults to 0
roadMap.addEdge(2, 3, 10);  // weighted: edge weight is 10
```

Even the unweighted form is represented internally as a weighted edge with
weight `0`. This lets algorithms use one consistent representation.

## 4. Decide how to store the graph

The diagram is easy for a person to understand, but an algorithm needs a data
structure. There are several common choices.

### Edge list

An edge list stores each connection once:

```text
[(1, 2), (2, 3), (3, 4), (4, 1)]
```

For weighted edges, each item also stores its weight, for example
`(1, 2, 5)`. An edge list is useful when an algorithm needs to examine every
edge, but it is inconvenient to find all neighbors of one vertex.

### Adjacency list

An adjacency list stores the outgoing neighbors for each vertex:

```text
1 -> [2, 4]
2 -> [1, 3]
3 -> [2, 4]
4 -> [3, 1]
```

For an undirected graph, every connection appears in both directions. The
connection `(1, 2)` is therefore stored as `2` in vertex `1`'s list and as
`1` in vertex `2`'s list. For a directed graph, only the actual outgoing
direction is stored.

This representation is efficient for graph traversal because an algorithm can
go directly to the neighbors of a vertex. It uses `O(V + E)` space, where `V`
is the number of vertices and `E` is the number of edges.

### Adjacency matrix

An adjacency matrix uses a table with one row and one column per vertex:

```text
    1 2 3 4
1   0 1 0 1
2   1 0 1 0
3   0 1 0 1
4   1 0 1 0
```

The value at row `u`, column `v` tells us whether `u` connects directly to
`v`. It can check one possible edge in `O(1)` time, but it always needs
`O(V^2)` space. Since `Graph` is intended to work with neighbor lists and
traversal algorithms, it uses an adjacency list instead.

## 5. Connect the representation to `Graph.java`

The class has two related collections:

```java
private final List<List<WeightedEdge>> weightedAdjacencyList;
private final List<Edge> edges;
```

`weightedAdjacencyList` is the main structure used to find neighbors. It
contains one list for every vertex. Each `WeightedEdge` stores:

- `to`: the neighboring vertex;
- `weight`: the value attached to the connection.

For the undirected graph containing `(1, 2)` with weight `5`, the adjacency
list conceptually contains:

```text
1 -> [(to=2, weight=5)]
2 -> [(to=1, weight=5)]
```

The separate `edges` list stores the original edge objects. Keeping both
forms is useful: neighbor-based algorithms can use the adjacency list, while
algorithms that inspect every edge can use the edge list.

When the constructor receives `vertices`, it creates an empty neighbor list
for each vertex. It rejects zero or negative vertex counts because a graph
must contain at least one vertex:

```java
Graph graph = new Graph(4);
```

## 6. Follow the life of an edge

Now that the storage is clear, the public operations become easier to
understand.

### Add an edge

```java
graph.addEdge(1, 2, 5);
```

The class first checks that both vertices are valid. It then:

1. Adds `(1 -> 2, weight=5)` to vertex `1`'s adjacency list.
2. Adds an `Edge(1, 2, 5)` to the global edge list.
3. If the graph is undirected, adds `(2 -> 1, weight=5)` as well.

An existing edge is not added a second time.

### Read the neighbors

```java
graph.getEdgesOfVertex(1);
```

This returns the destination vertices reachable directly from `1`, such as
`[2, 4]`. When the weight is needed, use:

```java
graph.getWeightedEdgesOfVertex(1);
```

The result contains `WeightedEdge` objects, so an algorithm can read both the
destination and the weight.

### Check or remove an edge

`hasEdge(1, 2)` checks whether vertex `1` has a direct connection to vertex
`2`. `removeEdge(1, 2)` removes the connection from the adjacency list and
from the global edge list. In an undirected graph it removes both stored
directions.

All operations validate vertex numbers. Passing `0` or a number greater than
the graph's vertex count raises a `GraphException` instead of silently
creating invalid data.

## 7. Why this matters for algorithms

Once a graph is represented as lists of neighbors, graph algorithms can
explore it systematically. A **path** is a sequence of vertices connected by
edges, and a **cycle** is a path that returns to its starting vertex.

For example, starting at `1`, an algorithm can read its adjacency list, visit
`2` and `4`, then continue reading their lists. This idea leads directly to
**Breadth-First Search (BFS)** and **Depth-First Search (DFS)**, which are the
next tools for answering reachability, path, cycle, and connectivity
questions.

## 8. Your implementation task

Now implement the concepts from this chapter in
`../src/main/java/practice/graph/Graph.java`.

Your implementation should support:

- Creating a graph with a valid number of 1-based vertices.
- Creating either an undirected or directed graph.
- Storing edges in an adjacency list and in the edge list.
- Adding unweighted and weighted edges.
- Avoiding duplicate edges.
- Returning neighbors and weighted neighbors.
- Checking whether an edge exists.
- Removing edges while respecting graph direction.
- Rejecting invalid vertex numbers with a `GraphException`.

Do not change the BDD scenarios to make them pass. Use them as the
specification for the behavior that `Graph.java` must provide.

When you have finished, run the Graph API BDD test:

```bash
mvn -q -Dtest=GraphApiBddTest test
```

The test class is located at
`../src/test/java/chapter/topic_01/GraphApiBddTest.java`. A successful run means
that the implementation covered by these scenarios is correct. If a scenario
fails, read its expected and actual values and the diagnostic logs to identify
which part of `Graph.java` needs attention.

Feel free to use `GraphTest.java` class to write your own custom test for validation.