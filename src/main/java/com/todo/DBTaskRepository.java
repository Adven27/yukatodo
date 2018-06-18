package com.todo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class DBTaskRepository implements TaskRepository {
    private final EntityManager entityManager = EntityManagerUtil.getEntityManager();
    private static final Logger LOG = LoggerFactory.getLogger(DBTaskRepository.class);

    @Override
    public void add(Task task) {
        try {
            LOG.info("INSERTING " + task);
            entityManager.getTransaction().begin();
            entityManager.merge(task);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }

    }

    @Override
    public Task find(String name) {
        Task task = null;
        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("from Task where description = :param", Task.class);
            query.setParameter("param", name);
            task = (Task) query.getSingleResult();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
        return task;
    }

    @Override
    public List<Task> findAll() {
        List tasks = new ArrayList<>();
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
        add(task);
    }

    @Override
    public void delete(Task task) {
        try {
            entityManager.getTransaction().begin();
            //select / delete
            entityManager.remove(entityManager.find(Task.class, task.getId()));
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }

    @Override
    public void deleteAll() {
        try {
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("TRUNCATE TABLE TASK").executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
}