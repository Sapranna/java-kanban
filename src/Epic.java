import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Integer> subtasksOfEpic = new ArrayList<>();

    public Epic(String title, String description, TaskStatus status, ArrayList<Integer> subtasksOfEpic) {
        super(title, description, status);
        this.subtasksOfEpic = subtasksOfEpic;
    }

    public Epic(String title, String description, TaskStatus status) {
        super(title, description, status);
    }

    public ArrayList<Integer> getSubtasksOfEpic() {
        return subtasksOfEpic;
    }

    public void setSubtasksOfEpic(ArrayList<Integer> subtasksOfEpic) {
        this.subtasksOfEpic = subtasksOfEpic;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + subtasksOfEpic;
    }

}
