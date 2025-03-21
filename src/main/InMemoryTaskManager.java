import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    public int counter = 0;
    public Map<Integer, Task> tasks = new HashMap<>();
    public Map<Integer, Epic> epics = new HashMap<>();
    public Map<Integer, Subtask> subtasks = new HashMap<>();
    public HistoryManager historyManager = Managers.getDefaultHistory();

    //получение списка всех задач
    @Override
    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    //получение списка всех эпиков
    @Override
    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    //получение списка всех подзадач
    @Override
    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    //удаление всех задач
    @Override
    public void removeAllTasks() {
        if (!tasks.isEmpty()) {
            tasks.clear();
        } else {
            System.out.println("Осуществлена попытка удалить пустой список задач");
        }
    }

    //удаление всех эпиков
    @Override
    public void removeAllEpics() {
        if (!epics.isEmpty()) {
            epics.clear();
            removeAllSubtasks(); //также удаляем все подзадачи эпиков
        } else {
            System.out.println("Осуществлена попытка удалить пустой список эпиков");
        }
    }

    //удаление всех подзадач
    @Override
    public void removeAllSubtasks() {
        if (!subtasks.isEmpty()) {
            subtasks.clear();
            //также удаляем в эпиках списки сабтасок
            for (Epic epic : epics.values()) {
                if (!epic.getSubtasksId().isEmpty()) {
                    epic.setSubtasksId(new ArrayList<>());
                }
            }
        } else {
            System.out.println("Осуществлена попытка удалить пустой список подзадач");
        }
    }

    //получение задачи по идентификатору
    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            System.out.println("Задачи с id " + id + " не существует");
        } else {
            historyManager.add(task);
        }
        return task;
    }

    //получение эпика по идентификатору
    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            System.out.println("Эпика с id " + id + " не существует");
        } else {
            historyManager.add(epic);
        }
        return epic;
    }

    //получение подзадачи по идентификатору
    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            System.out.println("Подзадачи с id " + id + " не существует");
        } else {
            historyManager.add(subtask);
        }
        return subtask;
    }

    //добавление задачи
    @Override
    public void addTask(Task task) {
        if (task != null) {
            task.setId(++counter);
            tasks.put(counter, task);
        } else {
            System.out.println("Попытка добавить пустую задачу");
        }
    }

    //добавление эпика
    @Override
    public void addEpic(Epic epic) {
        if (epic != null) {
            epic.setId(++counter);
            epics.put(counter, epic);
        } else {
            System.out.println("Попытка добавить пустой эпик");
        }
    }

    //добавление подзадачи
    @Override
    public void addSubtask(Subtask subtask) {
        if ((subtask != null) && (subtask.getParentEpicId() != 0)) {
            int subtaskId = ++counter;
            subtask.setId(subtaskId);
            subtasks.put(subtaskId, subtask);
            //добавление подзадачи в список подзадач эпика
            int epicId = subtask.getParentEpicId();
            ArrayList<Integer> subtasksOfEpic = epics.get(epicId).getSubtasksId();
            subtasksOfEpic.add(subtaskId);
            epics.get(epicId).setSubtasksId(subtasksOfEpic);
            //вызов метода проверки статуса эпика
            updateEpicStatus(epicId);
        } else {
            System.out.println("Попытка добавить пустую подзадачу или подзадачу без эпика");
        }
    }

    //обновление задачи
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Попытка обновить несуществующую задачу");
        }
    }

    //обновление эпика
    @Override
    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            System.out.println("Попытка обновить несуществующий эпик");
            return;
        }
        //если осуществляется попытка обновить эпик с указанием статуса NEW, проверяем статусы подзадач(при наличии)
        if (TaskStatus.NEW.equals(epic.getStatus())) {
            for (int id : epic.getSubtasksId()) {
                if (!TaskStatus.NEW.equals(getSubtask(id).getStatus())) {
                    System.out.println("Статус эпика не может быть NEW, так как у него есть не новые подзадачи. " +
                            "Обновите эпик, указав корректный статус");
                    return;
                }
            }
        }
        if (TaskStatus.DONE.equals(epic.getStatus())) {
            for (int id : epic.getSubtasksId()) {
                if (!TaskStatus.DONE.equals(getSubtask(id).getStatus())) {
                    System.out.println("Статус эпика не может быть DONE, так как у него есть незавершенные " +
                            "подзадачи. Обновите эпик, указав корректный статус");
                    return;
                }
            }
        }
        epics.put(epic.getId(), epic);
    }

    //обновление подзадачи
    @Override
    public void updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            System.out.println("Попытка обновить несуществующую подзадачу");
            return;
        }
        subtasks.put(subtask.getId(), subtask);
        //вызов метода проверки статуса эпика
        updateEpicStatus(subtask.getParentEpicId());
    }

    //удаление задачи
    @Override
    public void removeTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Задача с id " + id + " не найдена");
        }
    }

    //удаление эпика
    @Override
    public void removeEpic(int id) {
        ArrayList<Integer> listOfSubtasks = new ArrayList<>();
        if (epics.containsKey(id)) {
            listOfSubtasks = epics.get(id).getSubtasksId();
            epics.remove(id);
        } else {
            System.out.println("Эпик с id " + id + " не найден");
            return;
        }
        //также удаляем сабтаски этого эпика
        for (Integer ls : listOfSubtasks) {
            subtasks.remove(ls);
        }
    }

    //удаление сабтаска
    @Override
    public void removeSubtask(int id) {
        if (subtasks.containsKey(id)) {
            int epicId = subtasks.get(id).getParentEpicId();
            subtasks.remove(id);
            //также удаляем id подзадачи из списка подзадач эпика
            ArrayList<Integer> subtasksOfEpic = epics.get(epicId).getSubtasksId();
            subtasksOfEpic.remove(Integer.valueOf(id));
            //вызов метода проверки статуса эпика
            updateEpicStatus(epicId);
        } else {
            System.out.println("Подзадача с id " + id + " не найдена");
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicStatus(int id) {
        TaskStatus epicStatus = epics.get(id).getStatus();
        if (TaskStatus.DONE.equals(epicStatus)) {
            System.out.println("Эпик уже был завершен");
            return;
        }
        int amountSubtasks = epics.get(id).getSubtasksId().size();
        //подсчитываем количество подзадач в статусах NEW и DONE
        int counterNew = 0;
        int counterDone = 0;
        for (int subtaskId : epics.get(id).getSubtasksId()) {
            TaskStatus subtaskStatus = subtasks.get(subtaskId).getStatus();
            if (TaskStatus.NEW.equals(subtaskStatus)) {
                counterNew++;
            } else if (TaskStatus.DONE.equals(subtaskStatus)) {
                counterDone++;
            }
        }
        //меняем статус эпика при необходимости
        if ((amountSubtasks == 0) || (amountSubtasks == counterNew)) {
            //подзадачи отсутствуют или все подзадачи эпика в статусе NEW
            if (!TaskStatus.NEW.equals(epicStatus)) { //
                epics.get(id).setStatus(TaskStatus.NEW);
            }
        } else if (amountSubtasks == counterDone) { //все подзадачи эпика в статусе DONE
            epics.get(id).setStatus(TaskStatus.DONE);
        } else if (!TaskStatus.IN_PROGRESS.equals(epicStatus)) { //иначе статус эпика должен быть IN_PROGRESS
            epics.get(id).setStatus(TaskStatus.IN_PROGRESS);
        }
    }

}
