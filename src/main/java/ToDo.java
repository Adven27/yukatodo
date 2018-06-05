import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class ToDo {


    private static final String COMMA_DELIMITER = ", ";
    private static final String NEW_LINE_SEPARATOR = "\n";
    File file;
    TaskRepository taskRepository = new InMemoryTaskRepository();

    public void add(String s) {
        taskRepository.add(new Task(s));
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void delete(String s) {
        taskRepository.delete(new Task(s));
    }

    public Task getTaskByName(String description) {
        return taskRepository.find(description);

    }

    /**
     * Добавляем задачи в csv - файл
     *
     * @param fileName file name
     * @return file
     */
    public File savetoFile(String fileName) {

        file = new File(fileName);
        try (FileWriter fr = new FileWriter(file)) {

            for (Task t : taskRepository.findAll()) {
                fr.append(t.getDescription());
                fr.append(COMMA_DELIMITER);
                fr.append(String.valueOf(t.getState()));
                fr.append(NEW_LINE_SEPARATOR);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return file;

    }

    /**
     * Загружает список задач из файла
     *
     * @param fileName file name
     * @return tasks лист задач
     */
    public List<Task> readFromFile(File fileName) {
        try {
            return Files.readAllLines(fileName.toPath()).stream().map(this::taskFromLine).collect(toList());
        } catch (Exception e) {
            return emptyList();
        }
    }

    private Task taskFromLine(String line) {
        String[] vals = line.split(",");
        return new Task(vals[0], Boolean.parseBoolean(vals[1]));
    }

    public List<Task> showCompletedTask() {
        return taskRepository.findAll().stream()
                .filter(Task::getState)
                .collect(toList());
    }

    public List<Task> showIncompleteTasks() {
        return taskRepository.findAll().stream()
                .filter(task -> !task.getState())
                .collect(toList());
    }

    public void deleteAllTasks() {
        taskRepository.deleteAll();
    }
}

