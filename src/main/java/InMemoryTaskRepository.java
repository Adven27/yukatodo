import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskRepository implements TaskRepository {
    List<Task> tasks = new ArrayList<Task>();
    private static final String COMMA_DELIMITER = ", ";
    private static final String NEW_LINE_SEPARATOR = "\n";


    @Override
    public void add(Task task) {
        tasks.add(task);
    }

    @Override
    public Task find(String name) {
        for (Task t : tasks) {
            if (t.getDescription().equals(name)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public List<Task> findAll() {
        return tasks;
    }

    @Override
    public void update(Task task) {

    }

    @Override
    public void delete(Task task) {
        tasks.remove(task);
    }

    @Override
    public void deleteAll() {
        tasks.clear();
    }
}
