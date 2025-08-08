import java.util.ArrayList;
import java.util.List;

public class Save {
    protected String title;
    protected String description;
    protected int id;
    protected TaskPriority status;

    public Save(String title, String description, TaskPriority status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskPriority getStatus() {
        return status;
    }

    public void setStatus(TaskPriority status) {
        this.status = status;
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
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{id=" + id + ", title='" + title + "', description='" + description + "', status=" + status + "}";
    }
}
