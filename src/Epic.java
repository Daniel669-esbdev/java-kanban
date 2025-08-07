import java.util.ArrayList;
import java.util.List;

public class Epic extends Save {
    private List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String title, String description, int id) {
        super(title, description, id, TaskPriority.NEW);
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void clearSubtaskIds() {
        subtaskIds.clear();
    }
}
