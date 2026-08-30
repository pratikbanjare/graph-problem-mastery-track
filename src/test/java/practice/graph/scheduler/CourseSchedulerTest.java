package practice.graph.scheduler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CourseSchedulerTest {

    @Test
    public void courseSchedulerTest() {
        CourseScheduler courseScheduler = new CourseScheduler();
        int numCourses = 4;
        int[][] prerequisites = new int[][] {
                {1,0},{2,1},{3,2}
        };

        Assertions.assertNotNull(courseScheduler.canFinish(numCourses, prerequisites));
    }
}
