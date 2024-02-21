package com.example.cloud_solutions_bp.configuration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAConfiguration {

/*    private static final String PERSISTENCE_UNIT_NAME = "adress_pu";
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    private static EntityManager entityManager = factory.createEntityManager();

    public static EntityManagerFactory getEntityManagerFactory() {
        return factory;
    }

    public static EntityManager getEntityManager() {
        return entityManager;
    }

    public static void shutdown() {
        if (entityManager != null) {
            entityManager.close();
        }
        if (factory != null) {
            factory.close();
        }
    }*/

    private static final String PERSISTENCE_UNIT_NAME = "adress_pu";
    private static EntityManagerFactory factory;
    private static EntityManager entityManager;

    private JPAConfiguration(){

    }

    private static void initialize() {
        if (factory == null || !factory.isOpen()) {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }

        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = factory.createEntityManager();
        }
    }

    private EntityManagerFactory getEntityManagerFactory() {
        initialize();
        return factory;
    }

    public static EntityManager getEntityManager() {
        initialize();
        return entityManager;
    }

    public static void shutdown() {
        if (entityManager != null) {
            entityManager.close();
        }
        if (factory != null) {
            factory.close();
        }

}
}