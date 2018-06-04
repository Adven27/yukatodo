import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class DBTaskRepository implements TaskRepository {
    private final EntityManager entityManager = EntityManagerUtil.getEntityManager();

    @Override
    public void add(Task task) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(task);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }

    }

    @Override
    public Task find(String name) {
        List<Task> tasks;
        try {
            entityManager.getTransaction().begin();
            tasks = entityManager.createQuery("from Task").getResultList();
            for (Task t : tasks) {
                if (t.getDescription().equals(name))
                    return t;
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
        return null;
    }

    @Override
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        try {
            entityManager.getTransaction().begin();
            tasks = entityManager.createQuery("from Task").getResultList();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
        return tasks;
    }

    @Override
    public void update(Task task) {

    }

    @Override
    public void delete(Task task) {

    }

    public void delete(String description) {
        try {
            entityManager.getTransaction().begin();
            Task task = (Task) entityManager.find(Task.class, description);
            entityManager.remove(task);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }

    public void update(int id, String name, boolean state) {

        try {
            entityManager.getTransaction().begin();
            Task task = entityManager.find(Task.class, id);
            task.setDescription(name);
            task.setState(state);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }

    }


    @Override
    public void deleteAll() {
        try {
            entityManager.getTransaction().begin();
            entityManager.clear();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }

    }
}
