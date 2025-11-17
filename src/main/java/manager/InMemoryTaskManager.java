package manager;

import model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected final Map<Integer, Save> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected int nextId = 1;

    protected final Set<Save> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Save::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Save::getId)
    );

    private int generateId() {
        return nextId++;
    }

    @Override
    public Save createTask(Save task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        addToPrioritizedIfPossible(task);
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
            updateEpicTime(epic);
        }

        addToPrioritizedIfPossible(subtask);
        return subtask;
    }

    @Override
    public void updateTask(Save task) {
        if (tasks.containsKey(task.getId())) {
            removeFromPrioritizedIfNeeded(tasks.get(task.getId()));
            tasks.put(task.getId(), task);
            addToPrioritizedIfPossible(task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic);
            updateEpicTime(epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            removeFromPrioritizedIfNeeded(subtasks.get(subtask.getId()));
            subtasks.put(subtask.getId(), subtask);

            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                updateEpicStatus(epic);
                updateEpicTime(epic);
            }
            addToPrioritizedIfPossible(subtask);
        }
    }

    @Override
    public void removeTaskById(int id) {
        Save removed = tasks.remove(id);
        if (removed != null) {
            prioritizedTasks.remove(removed);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            historyManager.remove(id);
            for (Integer subId : new ArrayList<>(epic.getSubtaskIds())) {
                Subtask sub = subtasks.remove(subId);
                if (sub != null) {
                    prioritizedTasks.remove(sub);
                    historyManager.remove(subId);
                }
            }
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            prioritizedTasks.remove(subtask);
            historyManager.remove(id);

            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epic);
                updateEpicTime(epic);
            }
        }
    }

    @Override
    public void clearAllTasks() {
        tasks.values().forEach(t -> {
            prioritizedTasks.remove(t);
            historyManager.remove(t.getId());
        });
        tasks.clear();
    }

    @Override
    public void clearAllEpics() {
        epics.values().forEach(e -> historyManager.remove(e.getId()));
        subtasks.values().forEach(s -> {
            prioritizedTasks.remove(s);
            historyManager.remove(s.getId());
        });
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearAllSubtasks() {
        subtasks.values().forEach(s -> {
            prioritizedTasks.remove(s);
            historyManager.remove(s.getId());
        });
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtaskIds();
            updateEpicStatus(epic);
            updateEpicTime(epic);
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
        Epic epic = epics.get(epicId);
        if (epic == null) return List.of();

        return epic.getSubtaskIds().stream()
                .map(subtasks::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Save> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Save> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private boolean hasTimeOverlap(Save task) {
        if (task.getStartTime() == null || task.getDuration() == null) return false;

        LocalDateTime newStart = task.getStartTime();
        LocalDateTime newEnd = task.getEndTime();
        if (newEnd == null) return false;

        return prioritizedTasks.stream()
                .filter(t -> t.getId() != task.getId())
                .filter(t -> t.getStartTime() != null && t.getEndTime() != null)
                .anyMatch(existing -> {
                    LocalDateTime eStart = existing.getStartTime();
                    LocalDateTime eEnd = existing.getEndTime();
                    return newStart.isBefore(eEnd) && newEnd.isAfter(eStart);
                });
    }

    private void addToPrioritizedIfPossible(Save task) {
        if (task.getStartTime() != null && task.getDuration() != null) {
            if (hasTimeOverlap(task)) {
                throw new IllegalArgumentException("Задача пересекается по времени с другой задачей!");
            }
            prioritizedTasks.add(task);
        }
    }

    private void removeFromPrioritizedIfNeeded(Save oldTask) {
        if (oldTask != null && oldTask.getStartTime() != null) {
            prioritizedTasks.remove(oldTask);
        }
    }

    private void updateEpicStatus(Epic epic) {
        List<Subtask> epicSubtasks = getSubtasksOfEpic(epic.getId());

        if (epicSubtasks.isEmpty()) {
            epic.setStatus(TaskPriority.NEW);
            return;
        }

        boolean allNew = epicSubtasks.stream().allMatch(s -> s.getStatus() == TaskPriority.NEW);
        boolean allDone = epicSubtasks.stream().allMatch(s -> s.getStatus() == TaskPriority.DONE);

        if (allDone) {
            epic.setStatus(TaskPriority.DONE);
        } else if (allNew) {
            epic.setStatus(TaskPriority.NEW);
        } else {
            epic.setStatus(TaskPriority.IN_PROGRESS);
        }
    }

    private void updateEpicTime(Epic epic) {
        List<Subtask> epicSubtasks = getSubtasksOfEpic(epic.getId());

        if (epicSubtasks.isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(Duration.ZERO);
            epic.setEndTime(null);
            return;
        }

        LocalDateTime earliest = null;
        LocalDateTime latest = null;
        Duration total = Duration.ZERO;

        for (Subtask s : epicSubtasks) {
            if (s.getStartTime() != null && s.getDuration() != null) {
                LocalDateTime end = s.getEndTime();
                if (earliest == null || s.getStartTime().isBefore(earliest)) {
                    earliest = s.getStartTime();
                }
                if (end != null && (latest == null || end.isAfter(latest))) {
                    latest = end;
                }
                total = total.plus(s.getDuration());
            }
        }

        epic.setStartTime(earliest);
        epic.setDuration(total);
        epic.setEndTime(latest);
    }
}