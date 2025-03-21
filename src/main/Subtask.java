public class Subtask extends Task{
    private int EpicId;
    
    public Subtask(String title, String description, TaskStatus status, int parentEpicId) {
        super(title, description, status);
        this.EpicId = parentEpicId;
    }
    
    public int getParentEpicId() {
        return EpicId;
    }

    public void setParentEpicId(int parentEpicId) {
        this.EpicId = parentEpicId;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + EpicId;
    }

}
