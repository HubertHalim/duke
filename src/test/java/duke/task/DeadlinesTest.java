package duke.task;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class DeadlinesTest {

    Task task = new Deadlines("go to school", new Date(2019, 2, 4));

    @Test
    public void markDoneTest() {
        assertFalse(task.isDone());
        task.markAsDone();
        assertTrue(task.isDone());
    }

    @Test
    public void getTaskNameTest() {
        assertEquals(task.getTaskName(), "go to school");
    }

    @Test
    public void getTaskTimeTest() {
        assertEquals(task.getTaskTime(), new Date(2019, 2, 4));
    }

}