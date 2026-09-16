package practice.scheduler;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import practice.graph.scheduler.CourseScheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseSchedulerSteps {

    private CourseScheduler courseScheduler;
    private int numCourses;
    private int[][] prerequisites;
    private boolean canFinishResult;
    private List<Integer> schedule;

    @Given("there are {int} courses")
    public void there_are_courses(int numCourses) {
        this.numCourses = numCourses;
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
    }

    @Given("there are no prerequisite relationships")
    public void there_are_no_prerequisite_relationships() {
        prerequisites = new int[0][0];
    }

    @When("the scheduler checks whether the courses can be completed")
    public void the_scheduler_checks_whether_the_courses_can_be_completed() {
        courseScheduler = new CourseScheduler();
        canFinishResult = courseScheduler.canFinish(numCourses, prerequisites);
    }

    @When("the scheduler tries to produce a course schedule")
    public void the_scheduler_tries_to_produce_a_course_schedule() {
        courseScheduler = new CourseScheduler();
        schedule = courseScheduler.getSchedule(numCourses, prerequisites);
    }

    @Then("it should confirm that all courses can be finished")
    public void it_should_confirm_that_all_courses_can_be_finished() {
        Assertions.assertTrue(canFinishResult);
    }

    @Then("it should report that the courses cannot be finished")
    public void it_should_report_that_the_courses_cannot_be_finished() {
        Assertions.assertFalse(canFinishResult);
    }

    @Then("it should return a valid order of courses")
    public void it_should_return_a_valid_order_of_courses() {
        schedule = courseScheduler.getSchedule(numCourses, prerequisites);
        Assertions.assertFalse(schedule.isEmpty());
        assertScheduleIsValid(schedule, prerequisites);
    }

    @Then("it should return all courses in a valid order")
    public void it_should_return_all_courses_in_a_valid_order() {
        schedule = courseScheduler.getSchedule(numCourses, prerequisites);
        Assertions.assertEquals(numCourses, schedule.size());
        assertScheduleIsValid(schedule, prerequisites);
    }

    @Then("it should return a valid course order without violating prerequisites")
    public void it_should_return_a_valid_course_order_without_violating_prerequisites() {
        schedule = courseScheduler.getSchedule(numCourses, prerequisites);
        Assertions.assertEquals(numCourses, schedule.size());
        assertScheduleIsValid(schedule, prerequisites);
    }

    @Then("it should return no schedule")
    public void it_should_return_no_schedule() {
        Assertions.assertTrue(schedule.isEmpty());
    }

    private void assertScheduleIsValid(List<Integer> producedSchedule, int[][] edges) {
        Map<Integer, Integer> position = new HashMap<>();
        for (int i = 0; i < producedSchedule.size(); i++) {
            position.put(producedSchedule.get(i), i);
        }

        for (int[] edge : edges) {
            int course = edge[0];
            int prerequisite = edge[1];

            Assertions.assertTrue(
                    position.containsKey(course),
                    "Course " + course + " is missing from the schedule"
            );
            Assertions.assertTrue(
                    position.containsKey(prerequisite),
                    "Prerequisite " + prerequisite + " is missing from the schedule"
            );

            Assertions.assertTrue(
                    position.get(prerequisite) < position.get(course),
                    "Course " + course + " appears before its prerequisite " + prerequisite
            );
        }
    }
}