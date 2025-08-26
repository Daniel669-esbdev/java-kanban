package manager;

import model.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Save> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = new InMemoryHistoryManager();
    private int nextId = 1;

    private int generateId() {
        return nextId++;
    }

    @Override
        public Save createTask(Save task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
        public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
        public Subtask createSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtaskId(subtask.getId());
            updateEpicStatus(epic);
        }
        return subtask;
    }

    @Override
        public void updateTask(Save task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
        public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic);
        }
    }

    @Override
     public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                updateEpicStatus(epic);
            }
        }
    }

    @Override
     public void clearAllTasks() {
        tasks.clear();
    }

    @Override
        public void clearAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
     public void clearAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtaskIds();
            updateEpicStatus(epic);
        }
    }

    @Override
        public void removeTaskById(int id) {
        tasks.remove(id);
    }

    @Override
        public void removeEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Integer subId : epic.getSubtaskIds()) {
                subtasks.remove(subId);
            }
        }
    }

    @Override
        public void removeSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epic);
            }
        }
    }

    @Override
        public Save getTaskById(int id) {
        Save task = tasks.get(id);
        if (task != null) historyManager.add(task);
        return task;
    }

    @Override
        public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) historyManager.add(epic);
        return epic;
    }

    @Override
            public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) historyManager.add(subtask);
        return subtask;
    }

    @Override
        public List<Save> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
        public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
        public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
        public List<Subtask> getSubtasksOfEpic(int epicId) {
        List<Subtask> result = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (Integer subId : epic.getSubtaskIds()) {
                Subtask subtask = subtasks.get(subId);
                if (subtask != null) {
                    result.add(subtask);
                }
            }
        }
        return result;
    }

    @Override
        public List<Save> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicStatus(Epic epic) {
        List<Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus(TaskPriority.NEW);
            return;
        }
        boolean allNew = true;
        boolean allDone = true;
        for (int id : subtaskIds) {
            Subtask subtask = subtasks.get(id);
            if (subtask != null) {
                if (subtask.getStatus() != TaskPriority.NEW) {
                    allNew = false;
                }
                if (subtask.getStatus() != TaskPriority.DONE) {
                    allDone = false;
                }
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
}
