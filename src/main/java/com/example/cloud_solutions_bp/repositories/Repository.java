package com.example.cloud_solutions_bp.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.RollbackException;
import org.hibernate.exception.ConstraintViolationException;

import java.sql.SQLIntegrityConstraintViolationException;

public abstract class Repository<T> {

    private EntityManager entityManager;

    public Repository(EntityManager entityManager){

        this.entityManager = entityManager;
    }

    public T add(T object) {
        T addedObject = null;
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }

            addedObject = entityManager.merge(object);
            transaction.commit();
        } catch (RollbackException re) {
            if (re.getCause() instanceof ConstraintViolationException) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }

                throw (ConstraintViolationException) re.getCause();
            }

            if (transaction.isActive()) {
                transaction.rollback();
            } else {
                throw re;
            }
        }

        return addedObject;
    }


    public void delete(T object) {
        EntityTransaction transaction = entityManager.getTransaction();

            try {
                if (!transaction.isActive()) {
                    transaction.begin();
                }

                entityManager.remove(object);
                entityManager.getTransaction().commit();

            } catch (RollbackException re) {
                if (re.getCause() instanceof ConstraintViolationException) {
                    if (transaction.isActive()) {
                        transaction.rollback();
                    }

                    throw (ConstraintViolationException) re.getCause();
                }

                if (transaction.isActive()) {
                    transaction.rollback();
                } else {
                    throw re;
                }
            }
    }

    public T find(Integer id, Class<T>entityclass){
        return entityManager.find(entityclass, id);
    }

    public abstract T update(T object);
}
