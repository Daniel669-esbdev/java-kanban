import java.util.List;

public interface TaskManager {
    Save createTask(Save task);
    Epic createEpic(Epic epic);
    Subtask createSubtask(Subtask subtask);

    void updateTask(Save task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);

    void clearAllTasks();
    void clearAllEpics();
    void clearAllSubtasks();

    void removeTaskById(int id);
    void removeEpicById(int id);
    void removeSubtaskById(int id);

    List<Subtask> getSubtasksOfEpic(int epicId);

    Save getTaskById(int id);
    Epic getEpicById(int id);
    Subtask getSubtaskById(int id);

    List<Save> getTasks();
    List<Epic> getEpics();
    List<Subtask> getSubtasks();

    List<Save> getHistory();
}
