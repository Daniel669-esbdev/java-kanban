package manager;

import model.Save;
import model.TaskPriority;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    @Test
        void shouldAddAndFindTasksById() {
            InMemoryTaskManager manager = new InMemoryTaskManager();
            Save task = new Save("Задача", "Описание", TaskPriority.NEW);
            int taskId = manager.createTask(task).getId();

            Save found = manager.getTaskById(taskId);
            assertEquals(task, found);
    }

    @Test
        void shouldNotConflictGeneratedAndSetId() {
            InMemoryTaskManager manager = new InMemoryTaskManager();

            Save manual = new Save("Ручная", "Описание", TaskPriority.NEW);
            manual.setId(100);
            manager.createTask(manual);

            Save auto = new Save("Авто", "Описание", TaskPriority.NEW);
            int autoId = manager.createTask(auto).getId();

            assertNotEquals(manual.getId(), autoId,
                    "ID ручно и авто не должны быть конфликтными");
    }

    @Test
        void shouldKeepTaskImmutableAfterAdd() {
            InMemoryTaskManager manager = new InMemoryTaskManager();
            Save task = new Save("Исходная", "Описание", TaskPriority.NEW);
            int taskId = manager.createTask(task).getId();

            Save fromManager = manager.getTaskById(taskId);
            assertEquals(task.getTitle(), fromManager.getTitle());
            assertEquals(task.getDescription(), fromManager.getDescription());
            assertEquals(task.getStatus(), fromManager.getStatus());
    }
}
