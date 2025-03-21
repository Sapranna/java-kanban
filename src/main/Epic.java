import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String title, String description, TaskStatus status, ArrayList<Integer> subtasksId) {
        super(title, description, status);
        this.subtasksId = subtasksId;
    }

    public Epic(String title, String description, TaskStatus status) {
        super(title, description, status);
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setSubtasksId(ArrayList<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + subtasksId;
    }

}
