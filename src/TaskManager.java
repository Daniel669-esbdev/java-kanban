import java.util.*;

public class TaskManager {
    private int nextId = 1;

    private final Map<Integer, Save> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    public Save createTask(Save task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) return null;

        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        epic.addSubtaskId(subtask.getId());
        updateEpicStatus(epic.getId());
        return subtask;
    }

    public void updateTask(Save task) {
        Save existing = tasks.get(task.getId());
        if (existing != null) {
            existing.setTitle(task.getTitle());
            existing.setDescription(task.getDescription());
            existing.setStatus(task.getStatus());
        }
    }

    public void updateEpic(Epic epic) {
        Epic existing = epics.get(epic.getId());
        if (existing != null) {
            existing.setTitle(epic.getTitle());
            existing.setDescription(epic.getDescription());
        }
    }

    public void updateSubtask(Subtask subtaskMiniNew) {
        Subtask subtaskMini = subtasks.get(subtaskMiniNew.getId());
        if (subtaskMini != null) {
            if (subtaskMini.getEpicId() == subtaskMiniNew.getEpicId()) {
                subtaskMini.setTitle(subtaskMiniNew.getTitle());
                subtaskMini.setDescription(subtaskMiniNew.getDescription());
                subtaskMini.setStatus(subtaskMiniNew.getStatus());
                updateEpicStatus(subtaskMini.getEpicId());
            } else {
                System.out.println("Ошибка,нельзя изменить айди");
            }
        }
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        List<Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus(TaskPriority.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (int id : subtaskIds) {
            Subtask s = subtasks.get(id);
            if (s == null) {

                allNew = false;
                allDone = false; // сделал как вы и просили, так же решил поменять и первое название
                continue;
            }
            TaskPriority status = s.getStatus();
            if (status != TaskPriority.NEW) {
                allNew = false;
            }
            if (status != TaskPriority.DONE) {
                allDone = false;
            }
        }

        if (allDone) {
            epic.setStatus(TaskPriority.DONE);
        } else if (allNew) {
            epic.setStatus(TaskPriority.NEW);
        } else {
            epic.setStatus(TaskPriority.IN_PROGRESS);
        }
    }

    public void clearAllTasks() {
        tasks.clear();
    }

    public void clearAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void clearAllSubtasks() {
        subtasks.clear();
        for (Epic e : epics.values()) {
            e.clearSubtaskIds();
            e.setStatus(TaskPriority.NEW);
        }
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeEpicById(int id) {
        Epic removed = epics.remove(id);
        if (removed != null) {
            for (int subId : removed.getSubtaskIds()) {
                subtasks.remove(subId);
            }
        }
    }

    public void removeSubtaskById(int id) {
        Subtask removed = subtasks.remove(id);
        if (removed != null) {
            Epic parent = epics.get(removed.getEpicId());
            if (parent != null) {
                parent.removeSubtaskId(id);
                updateEpicStatus(parent.getId());
            }
        }
    }

    public List<Subtask> getSubtasksOfEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return Collections.emptyList();
        }
        List<Subtask> result = new ArrayList<>();
        for (int subId : epic.getSubtaskIds()) {
            Subtask s = subtasks.get(subId);
            if (s != null) {
                result.add(s);
            }
        }
        return result;
    }

    // я просто загуглил как можно назвать файл и мне вышли варианты, один из вариантов был Save
    // постарался полностью исправить код, но не уверен, что выполнил все требования, исправления дались тяжело
    // и как я к вам могу обращаться?


    public TaskRead getReader() {
        return new TaskRead(tasks, epics, subtasks);
    }

    public static class TaskRead {
        private final Map<Integer, Save> tasks;
        private final Map<Integer, Epic> epics;
        private final Map<Integer, Subtask> subtasks;

        public TaskRead(Map<Integer, Save> tasks, Map<Integer, Epic> epics, Map<Integer, Subtask> subtasks) {
            this.tasks = tasks;
            this.epics = epics;
            this.subtasks = subtasks;
        }

        public Save getTask(int id) {
            return tasks.get(id);
        }

        public Epic getEpic(int id) {
            return epics.get(id);
        }

        public Subtask getSubtask(int id) {
            return subtasks.get(id);
        }

        public List<Save> getAllTasks() {
            return new ArrayList<>(tasks.values());
        }

        public List<Epic> getAllEpics() {
            return new ArrayList<>(epics.values());
        }

        public List<Subtask> getAllSubtasks() {
            return new ArrayList<>(subtasks.values());
        }
    }
}
