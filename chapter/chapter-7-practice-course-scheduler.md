# Course Scheduler Practice — chapter.topic_07_course_scheduler

## 1. Problem statement

- A university wants to decide whether all required courses can be completed.
- Each course is a node in a directed graph.
- A prerequisite relation like `course A` depends on `course B` becomes a directed edge:
  - `B -> A`
- The scheduler must answer one question:
  - "Can every course be taken without violating dependency constraints?"

## 4. Your task in the implementation file

- Open `src/main/java/practice/graph/scheduler/CourseScheduler.java`.
- Implement the missing logic in the `canFinish(...)` method.
- If needed, complete `getSchedule(...)` so it returns a valid ordering only when the schedule is possible.

## 5. Validate with the BDD test

- Use the following command:

```bash
mvn -Dtest=CourseSchedulerBddTest test
```

- This BDD test verifies the expected behavior for:
  - valid dependency chains
  - no-prerequisite cases
  - circular dependency detection
  - multiple independent course paths
  - impossible scheduling cases

- If the BDD test passes, the missing implementation in `CourseScheduler.java` is correct.
- A failing BDD test usually points to one of these issues:
  - a cycle was not detected correctly
  - the queue was not seeded with the correct zero-indegree courses
  - the schedule is missing a course or violates dependency order


## 8. File map for this practice

- Implementation: `src/main/java/practice/graph/scheduler/CourseScheduler.java`
- BDD feature: `../src/test/resources/features/chapter/topic_07_course_scheduler/course_scheduler.feature`
- Step definition: `src/test/java/chapter/topic_07_course_scheduler/CourseSchedulerSteps.java`
- BDD test suite: `src/test/java/chapter/topic_07_course_scheduler/CourseSchedulerBddTest.java`

This exercise belongs to `chapter.topic_07_practice`: the goal is to model a real-world scheduling problem as a directed acyclic graph problem and validate the solution with behavior-driven tests.
