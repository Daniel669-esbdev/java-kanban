public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Save task1 = manager.createTask(new Save("Покупка еды для дома", "Сходить в магазин и составить список покупок", TaskPriority.NEW));
        Save task2 = manager.createTask(new Save("Оплатить интернет", "Через приложение", TaskPriority.NEW));

        Epic epic1 = manager.createEpic(new Epic("Машина", "Написать план для накопления"));
        Subtask subtask1 = manager.createSubtask(new Subtask("Найти работу", "Изучить Java для работы", TaskPriority.NEW, epic1.getId()));
        Subtask subtask2 = manager.createSubtask(new Subtask("Закончить курс по Яндексу", "Оплатить и проходить курс", TaskPriority.NEW, epic1.getId()));

        Epic epic2 = manager.createEpic(new Epic("Заняться спортом", "Составить план тренировок"));
        Subtask subtask3 = manager.createSubtask(new Subtask("Найти зал возле дома", "Написать менеджеру о покупке абонемента", TaskPriority.NEW, epic2.getId()));

        TaskManager.TaskRead reader = manager.getReader();
        System.out.println("Все задачи: " + reader.getAllTasks());
        System.out.println("Все эпики: " + reader.getAllEpics());
        System.out.println("Все подзадачи: " + reader.getAllSubtasks());

        subtask1.setStatus(TaskPriority.DONE);
        subtask2.setStatus(TaskPriority.DONE);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);

        System.out.println("Прогресс эпика1 после выполнения: " + reader.getEpic(epic1.getId()).getStatus());

        reader.getAllTasks().remove(task1);
        System.out.println("Оставшиеся задачи: " + reader.getAllTasks());
    }
}
