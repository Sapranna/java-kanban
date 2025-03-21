import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager taskManager = Managers.getDefault();

    @Test
    void shouldTaskBeEqualsTaskFromTaskManagerById() {
        Task task1 = new Task("task1", "about task1",TaskStatus.NEW);
        taskManager.addTask(task1);
        final int taskId = task1.getId();
        assertEquals(task1, taskManager.getTask(taskId), "Задачи не совпадают");
    }

    @Test
    void shouldEpicBeEqualsEpicFromTaskManagerById() {
        Epic epic1 = new Epic("epic1", "about epic1", TaskStatus.NEW);
        taskManager.addEpic(epic1);
        final int epicId = epic1.getId();
        assertEquals(epic1, taskManager.getEpic(epicId), "Эпики не совпадают");
    }

    @Test
    void shouldSubtaskBeEqualsSubtaskFromTaskManagerById() {
        Epic epic1 = new Epic("epic1", "about epic1", TaskStatus.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "about subtask1", TaskStatus.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        final int subtaskId = subtask1.getId();
        assertEquals(subtask1, taskManager.getSubtask(subtaskId), "Подзадачи не совпадют");
    }

    @Test
    void shouldAddedTaskBeStable(){
        Task task1 = new Task("task1", "about task1",TaskStatus.NEW);
        taskManager.addTask(task1);
        final int taskId = task1.getId();
        //сравниваем данные созданной задачи с данными сохраненной задачи, полученной с помощью taskManager
        assertEquals("task1", taskManager.getTask(taskId).getTitle(), "Названия задач не совпадают");
        assertEquals("about task1", taskManager.getTask(taskId).getDescription(),
                "Описания задач не совпадают");
        assertEquals(TaskStatus.NEW, taskManager.getTask(taskId).getStatus(), "Статусы задач не совпадают");
    }

    @Test
    void shouldAddedEpicBeStable(){
        Epic epic1 = new Epic("epic1", "about epic1",TaskStatus.NEW);
        taskManager.addEpic(epic1);
        final int epicId = epic1.getId();
        //сравниваем данные созданного эпика с данными сохраненного эпика, полученного с помощью taskManager
        assertEquals("epic1", taskManager.getEpic(epicId).getTitle(), "Названия эпиков не совпадают");
        assertEquals("about epic1", taskManager.getEpic(epicId).getDescription(),
                "Описания эпиков не совпадают");
        assertEquals(TaskStatus.NEW, taskManager.getEpic(epicId).getStatus(), "Статусы эпиков не совпадают");
    }

    @Test
    void shouldAddedSubtaskBeStable(){
        Epic epic1 = new Epic("epic1", "about epic1", TaskStatus.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "about subtask1", TaskStatus.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        final int subtaskId = subtask1.getId();
        //сравниваем данные созданной подзадачи с данными сохраненной подзадачи, полученной с помощью taskManager
        assertEquals("subtask1", taskManager.getSubtask(subtaskId).getTitle(),
                "Названия подзадач не совпадают");
        assertEquals("about subtask1", taskManager.getSubtask(subtaskId).getDescription(),
                "Описания подзадач не совпадают");
        assertEquals(TaskStatus.NEW, taskManager.getSubtask(subtaskId).getStatus(),
                "Статусы подзадач не совпадают");
        assertEquals(epic1.getId(), taskManager.getSubtask(subtaskId).getParentEpicId(),
                "Родительский эпик не совпадает");
    }

    @Test
    void shouldReviewedTaskBeAddedInHistory(){
        Task task1 = new Task("task1", "about task1",TaskStatus.NEW);
        taskManager.addTask(task1);
        final int taskId = task1.getId();
        //вызвали метод getTask(int id) чтобы задача попала в историю просмотра
        taskManager.getTask(taskId);
        final List<Task> history = taskManager.getHistory();
        assertNotNull(history, "История просмотров не найдена");
        assertEquals(1, history.size(), "История просмотров не равна просмотру 1 задачи");
    }

    @Test
    void shouldReviewedEpicBeAddedInHistory(){
        Epic epic1 = new Epic("epic1", "about epic1",TaskStatus.NEW);
        taskManager.addEpic(epic1);
        final int epicId = epic1.getId();
        //вызвали метод getEpic(int id) чтобы эпик попал в историю просмотра
        taskManager.getEpic(epicId);
        final  List<Task> history = taskManager.getHistory();
        assertNotNull(history, "История просмотров не найдена");
        assertEquals(1, history.size(), "История просмотров не равна просмотру 1 эпика");
    }

    @Test
    void shouldSubtaskBeRemoved(){
        Epic epic1 = new Epic("epic1", "about epic1", TaskStatus.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "about subtask1", TaskStatus.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        //удалили подзадачу, ее эпик остался без подзадач
        taskManager.removeSubtask(subtask1.getId());
        assertTrue(taskManager.getSubtasks().isEmpty(), "Подзадача не удалена");
        assertEquals(1, taskManager.getEpics().size(), "Некорректный размер списка эпиков");
        assertTrue(taskManager.getEpic(epic1.getId()).getSubtasksId().isEmpty(), "У эпика осталась подзадача");
    }

    @Test
    void shouldTasksBeRemoved(){
        Task task1 = new Task("task1", "about task1",TaskStatus.NEW);
        taskManager.addTask(task1);
        //удалили задачи
        taskManager.removeTask(task1.getId());
        assertTrue(taskManager.getTasks().isEmpty(), "Задача не удалена");
    }

    @Test
    void shouldEpicAndSubtasksBeRemoved(){
        Epic epic1 = new Epic("epic1", "about epic1", TaskStatus.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "about subtask1", TaskStatus.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        final int subtaskId = subtask1.getId();
        //удалили эпики
        taskManager.removeEpic(epic1.getId());
        assertTrue(taskManager.getEpics().isEmpty(), "Эпики не удалены");
        assertTrue(taskManager.getSubtasks().isEmpty(), "Подзадачи не удалены");
    }
}