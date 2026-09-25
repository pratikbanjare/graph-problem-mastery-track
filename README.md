# graph-problem-mastery-track
A repository to practice graph theory and problems.

## Maven setup

This repository builds a reusable Maven library from `src/main/java`.

Stable entry points are documented with `package-info.java` files under:

- `practice.graph`
- `practice.path`
- `practice.mst`
- `practice.scc`
- `practice.sort`
- `practice.model`

Demo launchers live under `examples/src/main/java` so they are not published in the library jar.

### Run tests

```bash
mvn clean test
```

## Connected components BDD

Connected components now have Cucumber BDD coverage in:

- `src/test/resources/features/graph/connected-components.feature`
- `src/test/java/practice/graph/ConnectedComponentsSteps.java`

Run all unit and BDD tests with:

```bash
mvn test
```
