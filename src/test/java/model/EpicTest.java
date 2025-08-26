package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {

    @Test
    void epicsWithSameIdShouldBeEqual() {
        Epic e1 = new Epic("Эпик1", "Описание");
        e1.setId(10);
        Epic e2 = new Epic("Эпик2", "Другое");
        e2.setId(10);

        assertEquals(e1, e2);
    }

    @Test
        void addAndRemoveSubtaskIds() {
            Epic epic = new Epic("Эпик", "Описание");
            epic.setId(1);

            epic.addSubtaskId(101);
            epic.addSubtaskId(102);
            assertEquals(2, epic.getSubtaskIds().size());

            epic.removeSubtaskId(101);
            assertEquals(1, epic.getSubtaskIds().size());
            assertFalse(epic.getSubtaskIds().contains(101));

            epic.clearSubtaskIds();
            assertTrue(epic.getSubtaskIds().isEmpty());
    }

    @Test
        void toStringShouldContainAllInfo() {
            Epic epic = new Epic("Эпик", "Описание");
            epic.setId(5);
            epic.addSubtaskId(100);
            epic.addSubtaskId(101);

            String str = epic.toString();
            assertTrue(str.contains("5"));
            assertTrue(str.contains("Эпик"));
            assertTrue(str.contains("Описание"));
            assertTrue(str.contains("subtasks=[100, 101]"));
    }
}
