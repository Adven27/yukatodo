import java.util.List;

import static java.util.stream.Collectors.toList;

public class ToDo {

    final TaskRepository taskRepository;
    final Importer importer;



    public ToDo(TaskRepository taskRepository, Importer importer) {
        this.taskRepository = taskRepository;
        this.importer = importer;
    }

    public void add(String s) {
        taskRepository.add(new Task(s));
    }

    public List<Task> getAllTasks() {
        try {
            return taskRepository.findAll();
        } catch (IllegalStateException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public void delete(String s) {
        taskRepository.delete(new Task(s));
    }

    public Task getTaskByName(String description) {
        return taskRepository.find(description);

    }


    public Object exportTasks() {
       return importer.save(taskRepository.findAll());

    }


    public List<Task> importTasks() {
        return importer.load();

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