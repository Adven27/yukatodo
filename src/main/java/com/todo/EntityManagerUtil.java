package com.todo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class EntityManagerUtil {

    private static EntityManagerFactory emf = null;

    public static EntityManager getEntityManager() {
        if (emf == null){
            emf = Persistence.createEntityManagerFactory("postgres");
        }
        return emf.createEntityManager();
    }

}

