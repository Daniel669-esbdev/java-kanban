package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Save {
    private final int epicId;

    public Subtask(String title, String description, int epicId) {
        this(title, description, TaskPriority.NEW, epicId, null, null);
    }

    public Subtask(String title, String description, TaskPriority status, int epicId) {
        this(title, description, status, epicId, null, null);
    }

    public Subtask(String title, String description, TaskPriority status, int epicId,
                   LocalDateTime startTime, Duration duration) {
        super(title, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", status=" + getStatus() +
                ", epicId=" + epicId +
                ", startTime=" + getStartTime() +
                ", duration=" + (getDuration() != null ? getDuration().toMinutes() + "min" : "null") +
                ", endTime=" + getEndTime() +
                '}';
    }
}