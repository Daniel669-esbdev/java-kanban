import java.util.List;

public interface HistoryManager {
    void add(Save task);
    List<Save> getHistory();
}
