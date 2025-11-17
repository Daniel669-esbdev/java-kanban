package com.example;

import manager.*;
import model.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Path file = Paths.get("tasks.csv");
        try {
            if (Files.notExists(file)) {
                Files.createFile(file);
                System.out.println("Создан файл tasks.csv");
            }
        } catch (Exception e) {
            System.out.println("Не удалось создать файл: " + e.getMessage());
        }

        TaskManager manager = Managers.getDefault();

        Save task1 = new Save("Купить продукты", "Сходить в магазин", TaskPriority.NEW);
        Save task2 = new Save("Помыть машину", "Автомойка вечером", TaskPriority.NEW);

        manager.createTask(task1);
        manager.createTask(task2);

        Epic epic1 = new Epic("Подготовка к отпуску", "Собрать чемодан и документы");
        manager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Собрать одежду", "Футболки и шорты", epic1.getId());
        Subtask subtask2 = new Subtask("Купить билеты", "Самолёт туда-обратно", epic1.getId());

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        manager.getSubtaskById(subtask1.getId());

        printAllTasks(manager);

        System.out.println("\nПриоритетные задачи (по времени начала):");
        for (Save task : manager.getPrioritizedTasks()) {
            System.out.println(task);
        }
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Save task : manager.getTasks()) {
            System.out.println(task);
        }

        System.out.println("\nЭпики:");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);
            for (Subtask subtask : manager.getSubtasksOfEpic(epic.getId())) {
                System.out.println("  > " + subtask);
            }
        }

        System.out.println("\nПодзадачи:");
        for (Subtask subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("\nИстория просмотров:");
        for (Save task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}