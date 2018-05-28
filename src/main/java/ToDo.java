import java.util.ArrayList;
import java.util.List;

public class ToDo {

    List<Task> tasks = new ArrayList<Task>();

    public void add(String s) {
        tasks.add(new Task(s));
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public void delete(String s) {
        tasks.remove(new Task(s));
    }

}
