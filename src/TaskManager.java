import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public static int counter = 0;
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();

    //получение списка всех задач
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    //получение списка всех эпиков
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    //получение списка всех подзадач
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    //удаление всех задач
    public void removeAllTasks() {
        if (!tasks.isEmpty()) {
            tasks.clear();
        } else {
            System.out.println("Осуществлена попытка удалить пустой список задач");
        }
    }

    //удаление всех эпиков
    public void removeAllEpics() {
       if (!epics.isEmpty()) {
           epics.clear();
           removeAllSubtasks(); //также удаляем все подзадачи эпиков
       } else {
           System.out.println("Осуществлена попытка удалить пустой список эпиков");
       }
    }

    //удаление всех подзадач
    public void removeAllSubtasks() {
        if (!subtasks.isEmpty()) {
            subtasks.clear();
            //также удаляем в эпиках списки сабтасок
            for (Epic epic : epics.values()) {
                if (!epic.getSubtasksOfEpic().isEmpty()) {
                    epic.setSubtasksOfEpic(null);
                }
            }
        } else {
            System.out.println("Осуществлена попытка удалить пустой список подзадач");
        }
    }

    //получение задачи по идентификатору
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else {
            System.out.println("Задачи с id " + id + " не существует");
            return null;
        }
    }

    //получение эпика по идентификатору
    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            return epics.get(id);
        } else {
            System.out.println("Эпика с id " + id + " не существует");
            return null;
        }
    }

    //получение подзадачи по идентификатору
    public Subtask getSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        } else {
            System.out.println("Подзадачи с id " + id + " не существует");
            return null;
        }
    }

    //добавление задачи
    public void addTask(Task task) {
        if (task != null) {
            counter++;
            task.setId(counter);
            tasks.put(counter, task);
        } else {
            System.out.println("Попытка добавить пустую задачу");
        }
    }

    //добавление эпика
    public void addEpic(Epic epic) {
        if (epic != null) {
            counter++;
            epic.setId(counter);
            epics.put(counter, epic);
        } else {
            System.out.println("Попытка добавить пустой эпик");
        }
    }

    //добавление подзадачи
    public void addSubtask(Subtask subtask) {
        if (subtask != null && subtask.getParentEpicId() != 0) {
            counter++;
            subtask.setId(counter);
            subtasks.put(counter, subtask);
            //добавление подзадачи в список подзадач эпика
            int idOfEpic = subtask.getParentEpicId();
            ArrayList<Integer> subtasksOfEpic = epics.get(idOfEpic).getSubtasksOfEpic();
            subtasksOfEpic.add(counter);
            epics.get(idOfEpic).setSubtasksOfEpic(subtasksOfEpic);
        } else {
            System.out.println("Попытка добавить пустую подзадачу или подзадачу без эпика");
        }
    }

    //обновление задачи
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Попытка обновить несуществующую задачу");
        }
    }

    //обновление эпика
    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            System.out.println("Попытка обновить несуществующий эпик");
            return;
        }
        //если осуществляется попытка обновить эпик с указанием статуса NEW, проверяем статусы подзадач(при наличии)
        if (TaskStatus.NEW.equals(epic.getStatus())) {
           for (int id : epic.getSubtasksOfEpic()) {
               if (!TaskStatus.NEW.equals(getSubtaskById(id).getStatus())) {
                   System.out.println("Статус эпика не может быть NEW, так как у него есть не новые подзадачи. " +
                           "Обновите эпик, указав корректный статус");
                   return;
               }
           }
        }
        if (TaskStatus.DONE.equals(epic.getStatus())) {
            for (int id : epic.getSubtasksOfEpic()) {
                if (!TaskStatus.DONE.equals(getSubtaskById(id).getStatus())) {
                    System.out.println("Статус эпика не может быть DONE, так как у него есть незавершенные " +
                            "подзадачи. Обновите эпик, указав корректный статус");
                    return;
                }
            }
        }
        epics.put(epic.getId(), epic);
    }

    //обновление подзадачи
    public void updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            System.out.println("Попытка обновить несуществующую подзадачу");
            return;
        }
        subtasks.put(subtask.getId(), subtask);
        //если статус сабтаски DONE и статус ее эпика не DONE - проверяем статусы всех сабтасок эпика
        if (TaskStatus.DONE.equals(subtask.getStatus()) &&
                !TaskStatus.DONE.equals(epics.get(subtask.getParentEpicId()).getStatus())) {
            for (int id : epics.get(subtask.getParentEpicId()).getSubtasksOfEpic()) {
                if ((TaskStatus.NEW.equals(subtasks.get(id).getStatus())) ||
                (TaskStatus.IN_PROGRESS.equals(subtasks.get(id).getStatus()))) {
                        return;
                } else {
                    epics.get(subtask.getParentEpicId()).setStatus(TaskStatus.DONE);
                    //так как все сабтаски эпика в статусе DONE, меняем статус эпика на DONE
                }
            }
        } else if (TaskStatus.IN_PROGRESS.equals(subtask.getStatus()) &&
                !TaskStatus.IN_PROGRESS.equals(epics.get(subtask.getParentEpicId()).getStatus())) {
            epics.get(subtask.getParentEpicId()).setStatus(TaskStatus.IN_PROGRESS);
            //так как обновилась сабтаска в статусе IN_PROGRESS то и статус эпика должен быть IN_PROGRESS
        }
    }

    //удаление задачи
    public void removeTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Задача с id " + id + " не найдена");
        }
    }

    //удаление эпика
    public void removeEpic(int id) {
        ArrayList<Integer> listOfSubtasks = new ArrayList<>();
        if (epics.containsKey(id)) {
            listOfSubtasks = epics.get(id).getSubtasksOfEpic();
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
    public void removeSubtask(int id) {
        if (subtasks.containsKey(id)) {
            int idOfEpic = subtasks.get(id).getParentEpicId();
            subtasks.remove(id);
            //также удаляем id подзадачи из списка подзадач эпика
            ArrayList<Integer> subtasksOfEpic = epics.get(idOfEpic).getSubtasksOfEpic();
            if (subtasksOfEpic.contains(id)) {
                subtasksOfEpic.remove(Integer.valueOf(id));
                epics.get(idOfEpic).setSubtasksOfEpic(subtasksOfEpic);
            }
        } else {
            System.out.println("Подзадача с id " + id + " не найдена");
        }
    }
}
