package com.example.cloud_solutions_bp.repositories;

import com.example.cloud_solutions_bp.util.Util;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.RollbackException;
import org.hibernate.exception.ConstraintViolationException;

import java.sql.SQLIntegrityConstraintViolationException;

public abstract class Repository<T> {

    private EntityManager entityManager;

    private Util util;

    public Repository(EntityManager entityManager){

        this.entityManager = entityManager;
        this.util = new Util();
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
            util.handleRollBackException(re, transaction);
        }

        return addedObject;
    }


    public void delete(T object) {
            try {
                entityManager.getTransaction().begin();
                entityManager.remove(object);
                entityManager.getTransaction().commit();

            }catch(RollbackException re){
                if(re.getCause() instanceof ConstraintViolationException){
                    entityManager.getTransaction().rollback();
                    throw(ConstraintViolationException)re.getCause();

                }if(entityManager.getTransaction().isActive()){
                    entityManager.getTransaction().rollback();

                }else{
                    throw re;
                }

            }
    }

    public T find(Integer id, Class<T>entityclass){
        return entityManager.find(entityclass, id);
    }

    public abstract T update(T object);
}
