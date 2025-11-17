package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Save {
    protected String title;
    protected String description;
    protected int id;
    protected TaskPriority status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Save(String title, String description, TaskPriority status) {
        this(title, description, status, null, null);
    }

    public Save(String title, String description, TaskPriority status,
                LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TaskPriority getStatus() { return status; }
    public void setStatus(TaskPriority status) { this.status = status; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public Duration getDuration() { return duration; }
    public void setDuration(Duration duration) { this.duration = duration; }

    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) return null;
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Save)) return false;
        Save save = (Save) o;
        return id == save.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + ", title='" + title + "', status=" + status +
                ", startTime=" + startTime + ", duration=" + (duration != null ? duration.toMinutes() + "min" : "null") + '}';
    }
}