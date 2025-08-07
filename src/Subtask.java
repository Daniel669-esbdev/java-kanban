public class Subtask extends Save {
    private int epicId;

    public Subtask(String title, String description, int id, TaskPriority status, int epicId) {
        super(title, description, id, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
