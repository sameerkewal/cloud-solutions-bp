package com.example.cloud_solutions_bp.repositories;

import com.example.cloud_solutions_bp.entities.Customer;
import com.example.cloud_solutions_bp.util.Util;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import com.example.cloud_solutions_bp.entities.Product;
import jakarta.persistence.RollbackException;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository extends Repository<Product> {

    private EntityManager entityManager;
    private final Util util;

    public ProductRepository(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
        util = new Util();
    }

    @Override
    public Product update(Product product) {
        try {
            Product productToUpdate = find(product.getId(), Product.class);
            entityManager.getTransaction().begin();

            productToUpdate.setName(product.getName());
            productToUpdate.setPrice(product.getPrice());
            productToUpdate.setManufacturer(product.getManufacturer());

            entityManager.getTransaction().commit();
            return find(product.getId(), Product.class);
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.out.println(e.getMessage());
        }
        return find(product.getId(), Product.class);
    }

    public List<Product> findProductByManufacturer(String manufacturer) {
        this.entityManager.getTransaction().begin();

        Query query = entityManager.createQuery("" +
                "select  pdt  " +
                "from    Product pdt " +
                "join    Manufacturer  mfr on pdt.manufacturer.id=mfr.id " +
                "where lower(mfr.name)=lower(:p1)");

        query.setParameter("p1", manufacturer);

        List<Product> resultList = query.getResultList();
        entityManager.getTransaction().commit();
        return resultList;


    }

    public List<Product>getProductByName(String productName){
        this.entityManager.getTransaction().begin();

        Query query = entityManager.createQuery("select  pdt from Product pdt where lower(name)=lower(:p1)");
        query.setParameter("p1", productName);

        List<Product> resultList = query.getResultList();
        this.entityManager.getTransaction().commit();
        return resultList;
    }

    public String getAllProducts() {
        EntityTransaction transaction = entityManager.getTransaction();
        List<Object[]> resultList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Query query = entityManager.createQuery("select  pdt.id, " +
                    "                                           pdt.name, " +
                    "                                           pdt.price, " +
                    "                                           pdt.manufacturer.name " +
                                                        ",      pdt.manufacturer.country" +
                    "                                   from    Product pdt");

            query = entityManager.createQuery("select sps.product_id                                     AS product_id,\n" +
                    "       p.name                                             AS name,\n" +
                    "       sps.total_sold                                     AS total_sold,\n" +
                    "       p.price                                            AS price,\n" +
                    "       (sps.total_sold * p.price)                     AS total,\n" +
                    "       mfr.name,\n" +
                    "       mfr.country\n" +
                    "from (select sps.product.id    AS product_id,\n" +
                    "              sum(sps.quantity) AS total_sold\n" +
                    "       from SaleProducts sps\n" +
                    "       group by sps.product.id) sps join Product p\n" +
                    "      on (p.id = sps.product_id)" +
                    "join Manufacturer  mfr on p.manufacturer.id = mfr.id");
            resultList = query.getResultList();
            jsonArray = new JSONArray();

            for (Object[] obj : resultList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", obj[0]);
                jsonObject.put("name", obj[1]);
                jsonObject.put("totalSold", obj[2]);
                jsonObject.put("price", obj[3]);
                jsonObject.put("totalValue", obj[4]);
                jsonObject.put("manufacturerName", obj[5]);
                jsonObject.put("manufacturerCountry", obj[6]);


                jsonArray.put(jsonObject);
            }


            entityManager.getTransaction().commit();


        } catch (RollbackException re) {
            util.handleRollBackException(re, transaction);
        }

        String jsonString = jsonArray.toString();
        System.out.println(jsonString);
        return jsonString;

    }

    public String getProduct(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        List<Object[]> resultList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Query query = entityManager.createQuery("select  pdt.id, " +
                    "                                           pdt.name, " +
                    "                                           pdt.price, " +
                    "                                           pdt.manufacturer.name " +
                                                        ",      pdt.manufacturer.country" +
                    "                                   from    Product pdt" +
                    "                                   where   pdt.id=:p1");
            query.setParameter("p1", id);
            resultList = query.getResultList();
            jsonArray = new JSONArray();

            for (Object[] obj : resultList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", obj[0]);
                jsonObject.put("name", obj[1]);
                jsonObject.put("price", obj[2]);
                jsonObject.put("manufacturerName", obj[3]);
                jsonObject.put("manufacturerCountry", obj[4]);


                jsonArray.put(jsonObject);
            }


            entityManager.getTransaction().commit();


        } catch (RollbackException re) {
            util.handleRollBackException(re, transaction);
        }

        String jsonString = jsonArray.toString();
        System.out.println(jsonString);
        return jsonString;

    }


}

