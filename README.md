# graph-problem-mastery-track
A repository to practice graph theory and problems 

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
