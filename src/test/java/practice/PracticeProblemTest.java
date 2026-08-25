package practice;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PracticeProblemTest {
    @Test
    void addsTwoNumbers() {
        PracticeProblem problem = new PracticeProblem();
        assertEquals(7, problem.add(2, 5));
    }
}
