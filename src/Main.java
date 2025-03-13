public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        //добавление 2х тасок
        Task task1 = new Task("Task1", "desc task1", TaskStatus.NEW);
        manager.addTask(task1);
        Task task2 = new Task("Task2", "desc task2", TaskStatus.NEW);
        manager.addTask(task2);

        //Добавление эпика epic1 с двумя сабтасками
        Epic epic1 = new Epic("Epic1", "desc epic1", TaskStatus.NEW);
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1", "desc subtask1", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask2", "desc subtask2", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask2);

        //добавление эпика epic2 с одной сабтаской
        Epic epic2 = new Epic("Epic2", "desc epic2", TaskStatus.NEW);
        manager.addEpic(epic2);
        Subtask subtask3 = new Subtask("Subtask3", "desc subtask3", TaskStatus.NEW, epic2.getId());
        manager.addSubtask(subtask3);

        System.out.println("Задачи: " + manager.getTasks().values());
        System.out.println("Подзадачи: " + manager.getSubtasks().values());
        System.out.println("Эпики: " + manager.getEpics().values());

        //обновление подзадачи
        Subtask subtask4 = new Subtask("SubtaskNew", "desc subtaskNew", TaskStatus.IN_PROGRESS, epic1.getId());
        subtask4.setId(subtask2.getId());
        manager.updateSubtask(subtask4);

        //обновление эпика
        Epic epic3 = new Epic("EpicNew", "new desc epic", TaskStatus.IN_PROGRESS, epic1.getSubtasksId());
        epic3.setId(epic1.getId());
        manager.updateEpic(epic3);

        //обновление задачи
        Task task3 = new Task("TaskNew", "new desc task", TaskStatus.DONE);
        task3.setId(task1.getId());
        manager.updateTask(task3);

        //удаление задачи
        manager.removeTask(task3.getId());

        //удаление эпика
        manager.removeEpic(epic1.getId());

        //удаление подзадачи
        manager.removeSubtask(subtask3.getId());

        System.out.println("-".repeat(50));
        System.out.println("Задачи: " + manager.getTasks().values());
        System.out.println("Подзадачи: " + manager.getSubtasks().values());
        System.out.println("Эпики: " + manager.getEpics().values());

    }
}
