# graph-problem-mastery-track
A repository to practice graph theory and problems 

## Algorithm Documentation

Algorithm notes and explanations live in the `docs/algorithms/` folder, organized by topic (e.g. `docs/algorithms/mst.md`, `docs/algorithms/scc.md`, `docs/algorithms/bellman-ford.md`, `docs/algorithms/kahns-algorithm.md`, `docs/algorithms/topological-ordering.md`).

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
