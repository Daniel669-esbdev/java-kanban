public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

            Save task1 = manager.createTask(new Save("Покупка еды для дома", "Сходить в магазин и составить список покупок", 0, TaskPriority.NEW));
            Save task2 = manager.createTask(new Save("Оплатить интерне", "Через прогу", 0, TaskPriority.NEW));

            Epic epic1 = manager.createEpic(new Epic("Машина", "Написать план для накопления", 0));
            Subtask subtask1 = manager.createSubtask(new Subtask("Найти работу", "Изучить Java для работы", 0, TaskPriority.NEW, epic1.getId()));
            Subtask subtask2 = manager.createSubtask(new Subtask("Закончить курс по яндексу", "Оплатить и проходить курс", 0, TaskPriority.NEW, epic1.getId()));

            Epic epic2 = manager.createEpic(new Epic("Заняться спортом", "Составить план тренировок", 0));
            Subtask subtask3 = manager.createSubtask(new Subtask("Найти зал возле дома", "Написать менеджеру о желании преобрести абонимент", 0, TaskPriority.NEW, epic2.getId()));

            TaskRead reader = manager.getReader();
            System.out.println("Все задачи: " + reader.getAllTasks());
            System.out.println("Весь прогресс: " + reader.getAllEpics());
            System.out.println("Все подзадачи.: " + reader.getAllSubtasks());

            subtask1.setStatus(TaskPriority.DONE);
            subtask2.setStatus(TaskPriority.DONE);
            manager.updateSubtask(subtask1);
            manager.updateSubtask(subtask2);

        System.out.println("Прогресс обновился: " + reader.getEpic(epic1.getId()).getStatus());

            reader.getAllTasks().remove(task1);
            System.out.println("Оставшиеся задачи: " + reader.getAllTasks());
    }
}
