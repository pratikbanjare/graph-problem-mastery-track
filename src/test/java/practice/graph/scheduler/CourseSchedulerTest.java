package practice.graph.scheduler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseSchedulerTest {

    @Test
    public void courseSchedulerTest() {
        CourseScheduler courseScheduler = new CourseScheduler();
        int numCourses = 4;
        int[][] prerequisites = new int[][] {
                {1,0},{2,1},{3,2}
        };

        Assertions.assertTrue(courseScheduler.canFinish(numCourses, prerequisites));
    }

    @Test
    public void courseSchedulerTest2() {
        CourseScheduler courseScheduler = new CourseScheduler();
        int numCourses = 4;
        int[][] prerequisites = new int[][] {
                {1,0},{2,1},{3,2}
        };

        List<Integer> expected = Arrays.asList(0,1,2,3);

        List<Integer> actual = courseScheduler.getSchedule(numCourses, prerequisites);

        Assertions.assertEquals(expected, actual);
    }
}
