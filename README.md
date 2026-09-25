# graph-problem-mastery-track
A repository to practice graph theory and problems 

## Supported algorithms
- [Graph Representation](docs/algorithms/graph/problem-1-graph-representation.md)
- [Bipartite Graph](docs/algorithms/graph/bipartite-graph.md)
- [Breadth-First Search (BFS) - find connected componentes](docs/algorithms/graph/connected-components.md)
- [Graph Cycle Detector](docs/algorithms/graph/graph-cycle-detector.md)
- [Kahn's Algorithm](docs/algorithms/graph/kahns-algorithm.md)
- [Bellman Ford's Algorithm](docs/algorithms/path/Bellman_ford.md)
- [DAG Shortest Path Algorithm](docs/algorithms/path/dag_shortest_path.md)
- [Djikstra's Algorithm](docs/algorithms/path/Djikstras_shortest_path_algo.md)
- [Flyod-Warshall Algorithm](docs/algorithms/path/Flyod_warshall.md)
- [Strongly Connected Components (SSC)](docs/algorithms/scc/scc.md#strongly-connected-components)
  - [Kosaraju's Algorithm](docs/algorithms/scc/scc.md#kosarajus-algorithm)
  - [Tarjan's Algorithm](docs/algorithms/scc/scc.md#tarjans-algorithm)
- [Minimum Spanning Tree (MST)](docs/algorithms/mst/MST.md#minimum-spanning-tree)
  - [Kruskal's Algorithm](docs/algorithms/mst/MST.md#kruskals-algorithm)
  - [Prim's Algorithm](docs/algorithms/mst/MST.md#prims-algorithm)

## Maven setup

This repository is now a simple Maven Java project.

Project structure:

- `src/main/java/practice/Main.java` - entry point
- `src/main/java/practice/PracticeProblem.java` - sample class for practice
- `src/main/java/practice/graph/Graph.java` - graph implementation
- `src/test/java/practice/PracticeProblemTest.java` - JUnit test

### Run

```bash
mvn clean test
mvn -q exec:java -Dexec.mainClass="practice.Main"
```

## Connected components BDD

Connected components now have Cucumber BDD coverage in:

- `src/test/resources/features/graph/connected-components.feature`
- `src/test/java/practice/graph/ConnectedComponentsSteps.java`

Run all unit and BDD tests with:

```bash
mvn test
```
