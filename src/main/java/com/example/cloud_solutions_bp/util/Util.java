package com.example.cloud_solutions_bp.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.RollbackException;
import org.hibernate.exception.ConstraintViolationException;

public class Util {



    public void handleRollBackException(RollbackException re, EntityTransaction transaction){
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
