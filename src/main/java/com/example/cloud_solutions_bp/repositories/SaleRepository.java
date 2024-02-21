package com.example.cloud_solutions_bp.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import com.example.cloud_solutions_bp.entities.Sale;
import com.example.cloud_solutions_bp.entities.SaleProducts;

import java.util.ArrayList;
import java.util.List;

public class SaleRepository extends Repository<Sale> {

    private EntityManager entityManager;

    public SaleRepository(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;


    }

    @Override
    public Sale update(Sale object) {
        return null;
    }

    public List<SaleProducts> getProductsFromSale(Sale sale) {

        List<SaleProducts> resultList = new ArrayList<>();
//        if (!this.entityManager.getTransaction().isActive()) {
            this.entityManager.getTransaction().begin();
//        } else {

            Query query = entityManager.createQuery("select sps from SaleProducts sps where sps.sale.id=:p1");
            query.setParameter("p1", sale.getId());
            resultList = query.getResultList();
            this.entityManager.getTransaction().commit();

            return resultList;
        }
//        return resultList;
//
//    }


}
