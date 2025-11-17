package manager;

import model.*;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path file;

    private FileBackedTaskManager(Path file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(Path file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.load();
        return manager;
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        lines.add("id,type,title,status,description,epic,startTime,duration");

        for (Save task : getTasks()) {
            lines.add(toString(task));
        }
        for (Epic epic : getEpics()) {
            lines.add(toString(epic));
        }
        for (Subtask subtask : getSubtasks()) {
            lines.add(toString(subtask));
        }

        lines.add("");
        List<Integer> historyIds = getHistory().stream()
                .map(Save::getId)
                .toList();
        if (!historyIds.isEmpty()) {
            lines.add(historyIds.stream()
                    .map(String::valueOf)
                    .reduce((a, b) -> a + "," + b)
                    .orElse(""));
        }

        try {
            Files.createDirectories(file.getParent());
            Files.write(file, lines);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл: " + file, e);
        }
    }

    private String toString(Save task) {
        String type = task instanceof Epic ? "EPIC" : task instanceof Subtask ? "SUBTASK" : "TASK";
        String epicId = task instanceof Subtask ? String.valueOf(((Subtask) task).getEpicId()) : "";
        String startTime = task.getStartTime() != null ? task.getStartTime().toString() : "";
        String duration = task.getDuration() != null ? String.valueOf(task.getDuration().toMinutes()) : "";

        return String.join(",",
                String.valueOf(task.getId()),
                type,
                task.getTitle(),
                task.getStatus().toString(),
                task.getDescription(),
                epicId,
                startTime,
                duration
        );
    }

    private void load() {
        if (!Files.exists(file)) return;

        try {
            List<String> lines = Files.readAllLines(file);
            boolean isHistory = false;
            int maxId = 0;

            for (String line : lines) {
                if (line.isBlank()) {
                    isHistory = true;
                    continue;
                }
                if (isHistory) {
                    if (!line.isBlank()) {
                        for (String idStr : line.split(",")) {
                            int id = Integer.parseInt(idStr.trim());
                            getTaskById(id);
                            getEpicById(id);
                            getSubtaskById(id);
                        }
                    }
                } else {
                    Save task = fromString(line);
                    if (task != null) {
                        int id = task.getId();
                        maxId = Math.max(maxId, id);

                        if (task instanceof Epic epic) {
                            epics.put(id, epic);
                        } else if (task instanceof Subtask subtask) {
                            subtasks.put(id, subtask);
                            Epic epic = epics.get(subtask.getEpicId());
                            if (epic != null) {
                                epic.addSubtaskId(id);
                            }
                        } else {
                            tasks.put(id, task);
                        }
                        if (task.getStartTime() != null) {
                            prioritizedTasks.add(task);
                        }
                    }
                }
            }
            nextId = maxId + 1;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла: " + file, e);
        }
    }

    private Save fromString(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 5) return null;

        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        String title = parts[2];
        TaskPriority status = TaskPriority.valueOf(parts[3]);
        String description = parts[4];

        LocalDateTime startTime = parts[6].isEmpty() ? null : LocalDateTime.parse(parts[6]);
        Duration duration = parts[7].isEmpty() ? null : Duration.ofMinutes(Long.parseLong(parts[7]));

        return switch (type) {
            case "TASK" -> {
                Save task = new Save(title, description, status, startTime, duration);
                task.setId(id);
                yield task;
            }
            case "EPIC" -> {
                Epic epic = new Epic(title, description);
                epic.setId(id);
                epic.setStatus(status);
                yield epic;
            }
            case "SUBTASK" -> {
                int epicId = Integer.parseInt(parts[5]);
                Subtask subtask = new Subtask(title, description, status, epicId, startTime, duration);
                subtask.setId(id);
                yield subtask;
            }
            default -> null;
        };
    }

    @Override public Save createTask(Save task) { super.createTask(task); save(); return task; }
    @Override public Epic createEpic(Epic epic) { super.createEpic(epic); save(); return epic; }
    @Override public Subtask createSubtask(Subtask subtask) { super.createSubtask(subtask); save(); return subtask; }
    @Override public void updateTask(Save task) { super.updateTask(task); save(); }
    @Override public void updateEpic(Epic epic) { super.updateEpic(epic); save(); }
    @Override public void updateSubtask(Subtask subtask) { super.updateSubtask(subtask); save(); }
    @Override public void removeTaskById(int id) { super.removeTaskById(id); save(); }
    @Override public void removeEpicById(int id) { super.removeEpicById(id); save(); }
    @Override public void removeSubtaskById(int id) { super.removeSubtaskById(id); save(); }
    @Override public void clearAllTasks() { super.clearAllTasks(); save(); }
    @Override public void clearAllEpics() { super.clearAllEpics(); save(); }
    @Override public void clearAllSubtasks() { super.clearAllSubtasks(); save(); }
}

class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message, Throwable cause) {
        super(message, cause); ///
    }
}