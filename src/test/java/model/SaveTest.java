package model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SaveTest {

    @Test
        void tasksWithSameIdShouldBeEqual() {
            Save task1 = new Save("Задача1", "Описание1", TaskPriority.NEW);
            task1.setId(1);

            Save task2 = new Save("Задача2", "Описание2", TaskPriority.DONE);
            task2.setId(1);

            assertEquals(task1, task2, "Задачи с одинаковым id должны быть равн");
            assertEquals(task1.hashCode(), task2.hashCode(), "Хешкод задач с одинаковым id одинаковые");
    }

    @Test
        void tasksWithDifferentIdShouldNotBeEqual() {
            Save task1 = new Save("Задача1", "Описание1", TaskPriority.NEW);
            task1.setId(1);

            Save task2 = new Save("Задача2", "Описание2", TaskPriority.DONE);
            task2.setId(2);

            assertNotEquals(task1, task2, "Задачи с разными id равны");
    }

    @Test
        void getSetFieldsShouldWorkCorrectly() {
            Save task = new Save("Страница", "пояснение", TaskPriority.NEW);
            task.setId(5);
            task.setTitle("Новая стр");
            task.setDescription("Новое пояснение");
            task.setStatus(TaskPriority.DONE);

            assertEquals(5, task.getId());
            assertEquals("Новая стр", task.getTitle());
            assertEquals("Новое пояснение", task.getDescription());
            assertEquals(TaskPriority.DONE, task.getStatus());
    }
}
