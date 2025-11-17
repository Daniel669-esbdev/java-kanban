package manager;

import model.Save;
import java.util.List;

public interface HistoryManager {

    void add(Save task);

    void remove(int id);

    List<Save> getHistory();
}