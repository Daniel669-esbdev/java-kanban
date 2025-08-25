import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_LIMIT = 10;
    private final List<Save> history = new ArrayList<>();

    @Override
        public void add(Save task) {
        if (history.size() == HISTORY_LIMIT) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public List<Save> getHistory() {
        return new ArrayList<>(history);
    }
}
