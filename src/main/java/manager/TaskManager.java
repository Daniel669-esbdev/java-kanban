package manager;

import model.*;

import java.util.List;

public interface TaskManager {

    Save createTask(Save task);
    Epic createEpic(Epic epic);
    Subtask createSubtask(Subtask subtask);

    void updateTask(Save task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);

    void removeTaskById(int id);
    void removeEpicById(int id);
    void removeSubtaskById(int id);

    void clearAllTasks();
    void clearAllEpics();
    void clearAllSubtasks();

    Save getTaskById(int id);
    Epic getEpicById(int id);
    Subtask getSubtaskById(int id);

    List<Save> getTasks();
    List<Epic> getEpics();
    List<Subtask> getSubtasks();
    List<Subtask> getSubtasksOfEpic(int epicId);

    List<Save> getHistory();
    List<Save> getPrioritizedTasks();
}