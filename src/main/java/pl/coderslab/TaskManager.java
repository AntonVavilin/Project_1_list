package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {

    static final String FILE_NAME = "tasks.csv";
    static final String[] OPTIONS = {"add", "remove", "list", "exit"};
    static String[][] tasks;

    public static void colorOptions(String[] options) {
        System.out.println(ConsoleColors.BLUE);
        System.out.println("Welcome to Task Manager!. Please enter one of the following options" + ConsoleColors.RESET);
        for (String option : options) {
            System.out.println(option);
        }
    }

    public static void main(String[] args) {
        tasks = loadDataToTab(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        colorOptions(OPTIONS);
        while (true) {
            String line = scanner.nextLine();

            if (line.equals("exit")) {
                saveDataToTab(FILE_NAME, tasks);
                System.out.println("Tasks has been saved. Goodbye");
                System.exit(0);
            } else if (line.equals("add")) {
                addTask();
            } else if (line.equals("remove")) {
                try {
                    Scanner indexToRemove = new Scanner(System.in);
                    System.out.println("Please enter the task id you would like to remove");
                    String taskId = indexToRemove.nextLine();
                    removeTask(tasks, Integer.parseInt(taskId));
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    System.out.println("Please enter a valid task id");
                }

            } else if (line.equals("list")) {
                showList(tasks);


            } else {
                System.out.println(Arrays.toString(OPTIONS));
                System.out.println("Please enter a valid option from the list above");
            }
        }

    }

    public static String[][] loadDataToTab(String fileName) {
        Path file = Paths.get(fileName);
        if (!Files.exists(file)) {
            System.out.println("File does not exist");
            return null;
        }

        String[][] table = null;
        try {
            List<String> lines = Files.readAllLines(file);

            if (lines.isEmpty()) {
                System.out.println("File is empty");
                return null;
            }

            table = new String[lines.size()][];
            for (int i = 0; i < lines.size(); i++) {
                String[] split = lines.get(i).split(",");
                table[i] = new String[3];
                for (int j = 0; j < split.length; j++) {
                    table[i][j] = split[j];
                }
                for (int j = split.length; j < 3; j++) {
                    table[i][j] = "";
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
            return null;
        }
        return table;
    }

    public static void saveDataToTab(String fileName, String[][] table) {
        Path path = Paths.get(fileName);

        String[] lines = new String[table.length];
        for (int i = 0; i < table.length; i++) {
            lines[i] = String.join(",", table[i]);
        }

        try {
            Files.write(path, Arrays.asList(lines));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void addTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please add task description");
        String description = scanner.nextLine();
        System.out.println("Please add task due date");
        String dueDate = scanner.nextLine();
        System.out.println("Is your task important: true/false");
        String isImportant = scanner.nextLine();
        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = description;
        tasks[tasks.length - 1][1] = dueDate;
        tasks[tasks.length - 1][2] = isImportant;
    }

    public static void showList(String[][] table) {
        for (int i = 0; i < table.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < table[i].length; j++) {
                System.out.print(table[i][j] + " ");
            }
            System.out.println();
        }

    }

    public static void removeTask(String[][] table, int index) {
        tasks = ArrayUtils.remove(table, index);

    }
}
