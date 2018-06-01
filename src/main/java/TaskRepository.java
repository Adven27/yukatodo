import java.util.List;

public interface TaskRepository {

    void add(Task task);

    Task find(String name);

    List<Task> findAll();

    void update(Task task);

    void delete(Task task);

    void deleteAll();

}
