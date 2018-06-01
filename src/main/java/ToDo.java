import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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


    public List<Task> readFromFile(File file2) {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file2))) {
            while (reader.ready()) {
                String[] str = reader.readLine().split(", ");
                tasks.add(new Task(str[0], Boolean.parseBoolean(str[1])));
            }
        } catch (Exception e) {
            System.out.println("MEOW");
        }
        return tasks;
    }

    public List<Task> showCompletedTask() {
        return taskRepository.findAll().stream()
                .filter(Task::getState)
                .collect(Collectors.toList());
    }

    public List<Task> showIncompleteTasks() {
        return taskRepository.findAll().stream()
                .filter(task -> !task.getState())
                .collect(Collectors.toList());
    }

    public void deleteAllTasks() {
        taskRepository.deleteAll();
    }
}

