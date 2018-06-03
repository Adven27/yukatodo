import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskRepository implements TaskRepository {

    EntityManager entityManager = EntityManagerUtil.getEntityManager();

    List<Task> tasks = new ArrayList<Task>();


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
