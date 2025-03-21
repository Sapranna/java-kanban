import java.util.List;
import java.util.Map;

public interface TaskManager {
    //получение списка всех задач
    Map<Integer, Task> getTasks();

    //получение списка всех эпиков
    Map<Integer, Epic> getEpics();

    //получение списка всех подзадач
    Map<Integer, Subtask> getSubtasks();

    //удаление всех задач
    void removeAllTasks();

    //удаление всех эпиков
    void removeAllEpics();

    //удаление всех подзадач
    void removeAllSubtasks();

    //получение задачи по идентификатору
    Task getTask(int id);

    //получение эпика по идентификатору
    Epic getEpic(int id);

    //получение подзадачи по идентификатору
    Subtask getSubtask(int id);

    //добавление задачи
    void addTask(Task task);

    //добавление эпика
    void addEpic(Epic epic);

    //добавление подзадачи
    void addSubtask(Subtask subtask);

    //обновление задачи
    void updateTask(Task task);

    //обновление эпика
    void updateEpic(Epic epic);

    //обновление подзадачи
    void updateSubtask(Subtask subtask);

    //удаление задачи
    void removeTask(int id);

    //удаление эпика
    void removeEpic(int id);

    //удаление сабтаска
    void removeSubtask(int id);

    //получение истории просмотра задач
    List<Task> getHistory();

}
