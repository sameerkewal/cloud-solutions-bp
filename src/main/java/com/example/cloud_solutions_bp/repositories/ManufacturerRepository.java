package com.example.cloud_solutions_bp.repositories;

import com.example.cloud_solutions_bp.util.Util;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import com.example.cloud_solutions_bp.entities.Manufacturer;
import jakarta.persistence.RollbackException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

public class ManufacturerRepository extends Repository<Manufacturer> {

    private EntityManager entityManager;
    private final Util util;


    public ManufacturerRepository(EntityManager entityManager) {

        super(entityManager);
        this.entityManager = entityManager;
        util = new Util();

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
            util.handleRollBackException(re, transaction);

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

    public boolean hasExistingName(Manufacturer manufacturer){
        try {
            this.entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("select mfr from Manufacturer mfr where mfr.name = :p1");
            query.setParameter("p1", manufacturer.getName());
            List<Manufacturer> resultList = query.getResultList();
            entityManager.getTransaction().commit();

            if(!resultList.isEmpty()){
                return true;
            }
            return false;

        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
                return false;
            }
        }
        return false;
    }
}
