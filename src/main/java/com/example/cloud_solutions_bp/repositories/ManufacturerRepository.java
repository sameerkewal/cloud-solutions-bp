package com.example.cloud_solutions_bp.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import com.example.cloud_solutions_bp.entities.Manufacturer;
import jakarta.persistence.RollbackException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

public class ManufacturerRepository extends Repository<Manufacturer> {

    private EntityManager entityManager;

    public ManufacturerRepository(EntityManager entityManager) {

        super(entityManager);
        this.entityManager = entityManager;
    }


    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        EntityTransaction transaction = entityManager.getTransaction();


        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }

            Manufacturer manufacturerToUpdate = find(manufacturer.getId(), Manufacturer.class);

            manufacturerToUpdate.setName(manufacturer.getName());
            manufacturerToUpdate.setCountry(manufacturer.getCountry());

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
        return find(manufacturer.getId(), Manufacturer.class);


    }


    public Manufacturer findManufacturerByName(String name) {
        this.entityManager.getTransaction().begin();

        Query query = entityManager.createQuery("select mfr from Manufacturer mfr where " +
                "lower(name)=lower(:p1)");

        query.setParameter("p1", name);

        List<Manufacturer> resultList = query.getResultList();
        entityManager.getTransaction().commit();
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }

    public List<Manufacturer> getAllManufacturers() {
        try {
            this.entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("select mfr from Manufacturer mfr");
            List<Manufacturer> resultList = query.getResultList();
            entityManager.getTransaction().commit();

            return resultList;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
        return null;
    }
}
