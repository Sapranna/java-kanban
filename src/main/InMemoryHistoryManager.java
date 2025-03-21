import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    public static final int MAX_HISTORY_SIZE = 10;
    public List<Task> historyList = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return historyList;
    }

    @Override
    public void add(Task task) {
        //если достигнут максимальный размер списка - удаляем самый старый элемент, потом добавляем новый
        if (historyList.size() == MAX_HISTORY_SIZE) {
            historyList.removeFirst();
        }
        historyList.add(task);
    }
}
