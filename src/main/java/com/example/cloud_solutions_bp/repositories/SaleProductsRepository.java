package com.example.cloud_solutions_bp.repositories;

import com.example.cloud_solutions_bp.util.Util;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import com.example.cloud_solutions_bp.entities.Sale;
import com.example.cloud_solutions_bp.entities.SaleProducts;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.RollbackException;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONArray;
import org.json.JSONObject;


public class SaleProductsRepository extends Repository<SaleProducts>{

    private final EntityManager entityManager;


    private final Util util;




    public SaleProductsRepository(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
        util = new Util();


    }

    @Override
    public SaleProducts update(SaleProducts object) {
        return null;
    }


    public boolean checkIfSaleHasProductsAddedToIt(Sale sale){
        this.entityManager.getTransaction().begin();

        Query query = entityManager.createQuery(
                "select sps from SaleProducts sps join Sale sle on sle.id=sps.sale.id where sle.id=:p1");


        query.setParameter("p1", sale.getId());

        List<SaleProducts> result  = query.getResultList();

        this.entityManager.getTransaction().commit();

        return !(result.isEmpty());
    }


    public void deleteSaleProducts(Sale sale){
        this.entityManager.getTransaction().begin();

        Query query = entityManager.createQuery(
                "delete from SaleProducts where sale.id=:p1");

        query.setParameter("p1", sale.getId());
        query.executeUpdate();
        this.entityManager.getTransaction().commit();

    }


    public void getSalesBasedOnProducts() {
        entityManager.getTransaction().begin();

        Query query = entityManager.createQuery(
                "select  sps.product_id,  p.name, sps.total_sold, p.price, (sps.total_sold*p.price) as total_value from (select  product.id as product_id, sum(quantity) as total_sold from SaleProducts group by product.id) sps join Product p on p.id = sps.product_id order by 5 desc ");

        List<Object[]> resultList = query.getResultList();
        entityManager.getTransaction().commit();

        for (Object[] obj : resultList) {
            Integer id = (Integer) obj[0];
            String name = (String) obj[1];
            Long sum = (Long) obj[2];
            BigDecimal price = (BigDecimal) obj[3];
            BigDecimal totalSold = (BigDecimal) obj[4];

            System.out.println("product: " + name + ", price: " + price + ", total items sold: " + sum + ", value of total sold: " + totalSold);

        }
    }

        public void getMostValuableCustomers(){
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("select cust_with_id.customer_id, cmr.firstname, cmr.lastname, cust_with_id.total from(select sle.customer.id as customer_id, sum(all_sales.total_worth) as total from(select sle.id as sale_id, sum(sps.quantity*p.price) as total_worth from Sale sle join SaleProducts sps on sps.sale.id=sle.id join Product  p on p.id = sps.product.id group by sle.id) all_sales join Sale sle on sle.id = all_sales.sale_id group by sle.customer.id)cust_with_id join Customer cmr on cust_with_id.customer_id=cmr.id order by cust_with_id.total desc");
            List<Object[]> resultList = query.getResultList();

            entityManager.getTransaction().commit();

            for (Object[] obj : resultList) {
                Integer id = (Integer) obj[0];
                String firstName = (String)obj[1];
                String lastName = (String)obj[2];
                BigDecimal totalWorth = (BigDecimal) obj[3];

                System.out.println( "id:" + id + ", name:" + firstName + " " + lastName + ", total spent:" + totalWorth);

            }


    }

    public String sale_vw(){


        EntityTransaction transaction = entityManager.getTransaction();
        List<Object[]> resultList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();

        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }

            Query query = entityManager.createQuery(
                    "SELECT cmr.sle_id" +
                            ", cmr.sale_date" +
                            ", cmr.cmr_name " +
                            ", total_items.total_items_in_order " +
                            ", order_line.all_products_in_order " +
                            ", unique_items.amount_unique_items_in_order " +
                            ", sle_total.total_amount_sold " +
                            "FROM (" +
                            "   SELECT sle.customer.id AS cmr_id, sle.id AS sle_id, " +
                            "          date_format(sle.sale_date, '%d-%m-%Y %H:%i:%s') AS sale_date, " +
                            "          concat(cmr.firstname, ' ', cmr.lastname) AS cmr_name " +
                            "   FROM Sale sle " +
                            "   JOIN Customer cmr ON cmr.id = sle.customer.id" +
                            ") cmr, (select   sle.id as sle_id\n" +
                            "     ,        sum(sps.quantity) as total_items_in_order\n" +
                            "     from     Sale sle\n" +
                            "     join     SaleProducts sps on sle.id = sps.sale.id\n" +
                            "     group by sle.id)total_items," +
                            "(select       group_concat(concat(pdt.name, ' (', sps.quantity, ')')) as all_products_in_order\n" +
                            "     ,        sle.id as sle_id\n" +
                            "     from     Sale sle\n" +
                            "     join     SaleProducts sps on sle.id = sps.sale.id\n" +
                            "     join     Product pdt on sps.product.id = pdt.id\n" +
                            "     group by sle_id)order_line," +
                            "(select   sle.id as sle_id\n" +
                            "     ,        count(distinct sps.product.id) as amount_unique_items_in_order\n" +
                            "     from     Sale sle\n" +
                            "     join     SaleProducts sps on sle.id = sps.sale.id\n" +
                            "     group by sle.id)unique_items," +
                            "(select    sle.id as sle_id\n" +
                            "     ,         sum(sps.quantity * pdt.price) as total_amount_sold\n" +
                            "     from     Sale sle\n" +
                            "     join     SaleProducts sps on sle.id = sps.sale.id\n" +
                            "     join     Product pdt on sps.product.id = pdt.id\n" +
                            "     group by sle.id)sle_total   where cmr.sle_id=total_items.sle_id" +
                            "                                  and order_line.sle_id=total_items.sle_id" +
                            "                                  and unique_items.sle_id=total_items.sle_id" +
                            "                                  and sle_total.sle_id=total_items.sle_id order by sle_total.sle_id desc"
            );


        resultList = query.getResultList();
            jsonArray = new JSONArray();
            for (Object[] obj : resultList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", obj[0]);
                jsonObject.put("name", obj[2]);
                jsonObject.put("saleDate", obj[1]);
                jsonObject.put("items", obj[4]);
                jsonObject.put("totalAmountSold", obj[6]);
                jsonObject.put("totalItemsInOrder", obj[3]);
                jsonObject.put("uniqueItemsInOrder", obj[5]);

                jsonArray.put(jsonObject);
            }
        entityManager.getTransaction().commit();


        } catch (RollbackException re) {
            util.handleRollBackException(re, transaction);

        }catch (NullPointerException npe){
            npe.printStackTrace();
        }




        String jsonString = jsonArray.toString();

        return jsonString;

    }


    public List<SaleProducts> getSalesBasedOnDate(LocalDateTime localDateTime){
        this.entityManager.getTransaction().begin();

        java.sql.Date sqlDate = java.sql.Date.valueOf(localDateTime.toLocalDate());

        Query query = entityManager.createQuery("select sps from SaleProducts sps join Sale sle on sle.id = sps.sale.id where date(sle.sale_date)=:p1");
//        Query query = entityManager.createQuery("select date(sle.sale_date) from SaleProducts sps join Sale sle on sle.id = sps.sale.id");
        query.setParameter("p1", sqlDate);


        List<SaleProducts> resultList = query.getResultList();

        this.entityManager.getTransaction().commit();

        return resultList;
    }
}
