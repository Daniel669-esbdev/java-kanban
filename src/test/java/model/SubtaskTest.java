package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubtaskTest {

    @Test
    void subtasksWithSameIdShouldBeEqual() {
        Subtask s1 = new Subtask("Подзадач1", "Описание1", TaskPriority.NEW, 2);
        s1.setId(5);
        Subtask s2 = new Subtask("Подзадача2", "Описание2", TaskPriority.DONE, 2);
        s2.setId(5);

        assertEquals(s1, s2);
    }
}
