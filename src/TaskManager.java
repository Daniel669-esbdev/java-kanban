import java.util.*;

public class TaskManager {
    private int nextId = 1;

    private final Map<Integer, Save> tasks = new HashMap<>();
    private final Map<Integer, Epic> epic = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    public Save createTask(Save task) {
        Save newTask = new Save(task.getTitle(), task.getDescription(), nextId, task.getStatus());
        tasks.put(nextId, newTask);
        nextId++;
        return newTask;
    }

    public Epic createEpic(Epic epicMini) {
        Epic newEpic = new Epic(epicMini.getTitle(), epicMini.getDescription(), nextId);
        epic.put(nextId, newEpic);
        nextId++;
        return newEpic;
    }

    public Subtask createSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        Epic epicMini = epic.get(epicId);

        if (epicMini == null) {
            return null;
        }

        Subtask newSubtask = new Subtask(
                subtask.getTitle(),
                subtask.getDescription(),
                nextId,
                subtask.getStatus(),
                epicId
        );

        subtasks.put(nextId, newSubtask);
        epicMini.addSubtaskId(nextId);
        upEpiStat(epicId);
        nextId++;
        return newSubtask;
    }

    public void updateTask(Save task) {
        Save taskMini = tasks.get(task.getId());
        if (taskMini != null) {
            taskMini.setTitle(task.getTitle());
            taskMini.setDescription(task.getDescription());
            taskMini.setStatus(task.getStatus());
        }
    }

    public void updateEpic(Epic epicMiniNew) {
        Epic epicMini = epic.get(epicMiniNew.getId());
        if (epicMini != null) {
            epicMini.setTitle(epicMiniNew.getTitle());
            epicMini.setDescription(epicMiniNew.getDescription());
        }
    }

    public void updateSubtask(Subtask subtaskMiniNew) {
        Subtask subtaskMini = subtasks.get(subtaskMiniNew.getId());
        if (subtaskMini != null) {
            subtaskMini.setTitle(subtaskMiniNew.getTitle());
            subtaskMini.setDescription(subtaskMiniNew.getDescription());
            subtaskMini.setStatus(subtaskMiniNew.getStatus());
            upEpiStat(subtaskMini.getEpicId());
        }
    }

    private void upEpiStat(int epicId) {
        Epic epicMini = epic.get(epicId);
        if (epicMini == null) return;

        List<Integer> subtaskIds = epicMini.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epicMini.setStatus(TaskPriority.NEW);
            return;
        }

        boolean firstNew = true;
        boolean secondNew = true;

        for (int subId : subtaskIds) {
            Subtask subtaskMini = subtasks.get(subId);
            if (subtaskMini.getStatus() != TaskPriority.NEW) {
                firstNew = false;
            }
            if (subtaskMini.getStatus() != TaskPriority.DONE) {
                secondNew = false;
            }
        }

        if (secondNew) {
            epicMini.setStatus(TaskPriority.DONE);
        } else if (firstNew) {
            epicMini.setStatus(TaskPriority.NEW);
        } else {
            epicMini.setStatus(TaskPriority.IN_PROGRESS);
        }
    }
    public TaskRead getReader() {
        return new TaskRead(tasks, epic, subtasks);
    }
}
