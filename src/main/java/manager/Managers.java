package manager;

import java.nio.file.Path;

public final class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return FileBackedTaskManager.loadFromFile(Path.of("tasks.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}