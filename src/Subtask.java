public class Subtask extends Task{
    private int parentEpicId;
    
    public Subtask(String title, String description, TaskStatus status, int parentEpicId) {
        super(title, description, status);
        this.parentEpicId = parentEpicId;
    }
    
    public int getParentEpicId() {
        return parentEpicId;
    }

    public void setParentEpicId(int parentEpicId) {
        this.parentEpicId = parentEpicId;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + parentEpicId;
    }

}
