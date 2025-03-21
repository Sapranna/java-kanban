import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    TaskManager taskManager = Managers.getDefault();
    HistoryManager historyManager = Managers.getDefaultHistory();

    @Test
    void shouldTaskBeAddedInHistory(){
        Task task1 = new Task("task1", "about task1",TaskStatus.NEW);
        taskManager.addTask(task1);
        historyManager.add(task1);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История просмотров не найдена");
        assertEquals(1, history.size(), "История просмотров не равна просмотру 1 задачи");
    }

}
