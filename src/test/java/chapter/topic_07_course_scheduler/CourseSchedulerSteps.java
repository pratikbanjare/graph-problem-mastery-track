package chapter.topic_07_course_scheduler;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import practice.graph.scheduler.CourseScheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class CourseSchedulerSteps {

    private static final Logger LOGGER = Logger.getLogger(CourseSchedulerSteps.class.getName());

    private CourseScheduler courseScheduler;
    private int numCourses;
    private int[][] prerequisites;
    private boolean canFinishResult;
    private List<Integer> schedule;

    @Given("there are {int} courses")
    public void there_are_courses(int numCourses) {
        this.numCourses = numCourses;
        LOGGER.info("Setup: there are " + numCourses + " courses.");
    }

    @Given("the prerequisite relationships are:")
    public void the_prerequisite_relationships_are(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);

        prerequisites = new int[rows.size()][2];
        for (int i = 0; i < rows.size(); i++) {
            Map<String, String> row = rows.get(i);
            prerequisites[i][0] = Integer.parseInt(row.get("course"));
            prerequisites[i][1] = Integer.parseInt(row.get("prerequisite"));
        }
        LOGGER.info("Prerequisite graph loaded: " + describePrerequisites(prerequisites));
    }

    @Given("there are no prerequisite relationships")
    public void there_are_no_prerequisite_relationships() {
        prerequisites = new int[0][0];
        LOGGER.info("No prerequisite relationships were provided.");
    }

    @When("the scheduler checks whether the courses can be completed")
    public void the_scheduler_checks_whether_the_courses_can_be_completed() {
        courseScheduler = new CourseScheduler();
        LOGGER.info("Checking completion for " + numCourses + " courses and prerequisites "
                + describePrerequisites(prerequisites));
        canFinishResult = courseScheduler.canFinish(numCourses, prerequisites);
        LOGGER.info("canFinish(...) returned " + canFinishResult + ".");
    }

    @When("the scheduler tries to produce a course schedule")
    public void the_scheduler_tries_to_produce_a_course_schedule() {
        courseScheduler = new CourseScheduler();
        LOGGER.info("Attempting to build a schedule for " + numCourses + " courses with "
                + describePrerequisites(prerequisites));
        schedule = courseScheduler.getSchedule(numCourses, prerequisites);
        LOGGER.info("Generated schedule: " + schedule);
    }

    @Then("it should confirm that all courses can be finished")
    public void it_should_confirm_that_all_courses_can_be_finished() {
        LOGGER.info("Assertion: expected completion to be true. Actual value: " + canFinishResult
                + "; prerequisites=" + describePrerequisites(prerequisites));
        Assertions.assertTrue(
                canFinishResult,
                "expected all courses to be finishable; prerequisites=" + describePrerequisites(prerequisites)
        );
    }

    @Then("it should report that the courses cannot be finished")
    public void it_should_report_that_the_courses_cannot_be_finished() {
        LOGGER.info("Assertion: expected completion to be false. Actual value: " + canFinishResult
                + "; prerequisites=" + describePrerequisites(prerequisites));
        Assertions.assertFalse(
                canFinishResult,
                "expected the course graph to be impossible; prerequisites=" + describePrerequisites(prerequisites)
        );
    }

    @Then("it should return a valid order of courses")
    public void it_should_return_a_valid_order_of_courses() {
        schedule = courseScheduler.getSchedule(numCourses, prerequisites);
        LOGGER.info("Validating non-empty schedule: " + schedule + " for prerequisites "
                + describePrerequisites(prerequisites));
        Assertions.assertFalse(schedule.isEmpty(),
                "expected a non-empty schedule; prerequisites=" + describePrerequisites(prerequisites));
        assertScheduleIsValid(schedule, prerequisites);
    }

    @Then("it should return all courses in a valid order")
    public void it_should_return_all_courses_in_a_valid_order() {
        schedule = courseScheduler.getSchedule(numCourses, prerequisites);
        LOGGER.info("Validating full schedule size=" + schedule.size() + " for " + numCourses
                + " courses; schedule=" + schedule + "; prerequisites=" + describePrerequisites(prerequisites));
        Assertions.assertEquals(numCourses, schedule.size(),
                "schedule size mismatch; expected " + numCourses + " but got " + schedule.size()
                        + "; prerequisites=" + describePrerequisites(prerequisites));
        assertScheduleIsValid(schedule, prerequisites);
    }

    @Then("it should return a valid course order without violating prerequisites")
    public void it_should_return_a_valid_course_order_without_violating_prerequisites() {
        schedule = courseScheduler.getSchedule(numCourses, prerequisites);
        LOGGER.info("Validating prerequisite-safe ordering: " + schedule + " for prerequisites "
                + describePrerequisites(prerequisites));
        Assertions.assertEquals(numCourses, schedule.size(),
                "schedule size mismatch; expected " + numCourses + " but got " + schedule.size()
                        + "; prerequisites=" + describePrerequisites(prerequisites));
        assertScheduleIsValid(schedule, prerequisites);
    }

    @Then("it should return no schedule")
    public void it_should_return_no_schedule() {
        LOGGER.info("Assertion: expected empty schedule. Actual schedule=" + schedule
                + "; prerequisites=" + describePrerequisites(prerequisites));
        Assertions.assertTrue(schedule.isEmpty(),
                "expected empty schedule for impossible course dependency graph; actual=" + schedule);
    }

    private void assertScheduleIsValid(List<Integer> producedSchedule, int[][] edges) {
        Map<Integer, Integer> position = new HashMap<>();
        for (int i = 0; i < producedSchedule.size(); i++) {
            position.put(producedSchedule.get(i), i);
        }

        LOGGER.info("Checking schedule order against prerequisites: " + describePrerequisites(edges));

        for (int[] edge : edges) {
            int course = edge[0];
            int prerequisite = edge[1];

            Assertions.assertTrue(
                    position.containsKey(course),
                    "Course " + course + " is missing from the schedule. Full schedule=" + producedSchedule
            );
            Assertions.assertTrue(
                    position.containsKey(prerequisite),
                    "Prerequisite " + prerequisite + " is missing from the schedule. Full schedule=" + producedSchedule
            );

            Assertions.assertTrue(
                    position.get(prerequisite) < position.get(course),
                    "Course " + course + " appears before its prerequisite " + prerequisite
                            + ". Schedule=" + producedSchedule + ". Prerequisites=" + describePrerequisites(edges)
            );
        }
    }

    private String describePrerequisites(int[][] edges) {
        if (edges == null || edges.length == 0) {
            return "[]";
        }

        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < edges.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(edges[i][0]).append(" -> ").append(edges[i][1]);
        }
        builder.append("]");
        return builder.toString();
    }
}