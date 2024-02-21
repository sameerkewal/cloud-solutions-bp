package com.example.cloud_solutions_bp.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import com.example.cloud_solutions_bp.entities.Sale;
import com.example.cloud_solutions_bp.entities.SaleProducts;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SaleProductsRepository extends Repository<SaleProducts>{

    private final EntityManager entityManager;
    public SaleProductsRepository(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
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

            System.out.println(STR. "product: \{ name }, price: \{ price }, total items sold: \{ sum }, value of total sold: \{ totalSold }" );

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

                System.out.println(STR."id:\{id}, name:\{firstName} \{lastName}, total spent:\{totalWorth}");
        }


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
