package com.example.cloud_solutions_bp.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.RollbackException;
import com.example.cloud_solutions_bp.entities.Customer;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepository extends Repository<Customer> {

    private EntityManager entityManager;


    public CustomerRepository(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }


    @Override
    public Customer update(Customer customer) {
        Customer customerToUpdate = find(customer.getId(), Customer.class);

        entityManager.getTransaction().begin();

        customerToUpdate.setAdress(customer.getAdress());
        customerToUpdate.setFirstname(customer.getFirstname());
        customerToUpdate.setLastname(customer.getLastname());
        customerToUpdate.setPhonenumber(customer.getPhonenumber());

        entityManager.getTransaction().commit();

        return find(customer.getId(), Customer.class);

    }


    public List<Customer> findCustomersByFirstAndLastName(String firstName, String lastName) {
        this.entityManager.getTransaction().begin();

        Query query = entityManager.createQuery("" +
                "select  cmr " +
                "from   Customer cmr " +
                "where  lower(firstname)=lower( :p1) " +
                "and    lower(lastname)=lower( :p2)");

        query.setParameter("p1", firstName);
        query.setParameter("p2", lastName);

        List<Customer> resultList = query.getResultList();
        entityManager.getTransaction().commit();
        return resultList;


    }

    public String getAllCustomers() {
        EntityTransaction transaction = entityManager.getTransaction();
        List<Object[]> resultList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();

        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Query query = entityManager.createQuery("select cmr.id, cmr.firstname\n" +
                    ",      cmr.lastname\n" +
                    ",      cmr.phonenumber\n" +
                    ",      total_spent.total_spent\n" +
                    ",      a.streetname\n" +
                    ",      a.housenumber\n" +
                    "from\n" +
                    "    (   SELECT\n" +
                    "    cmr.id AS customer_id,\n" +
                    "    SUM(sps.quantity * pdt.price) AS total_spent\n" +
                    "    FROM\n" +
                    "    Sale sle\n" +
                    "        JOIN SaleProducts sps ON sle.id = sps.sale.id\n" +
                    "        JOIN Product pdt ON pdt.id = sps.product.id\n" +
                    "        JOIN Customer cmr ON sle.customer.id = cmr.id\n" +
                    "    GROUP BY\n" +
                    "    cmr.id) total_spent\n" +
                    "join Customer cmr on total_spent.customer_id = cmr.id\n" +
                    "join Adress a on a.id = cmr.adress.id\n");
            resultList = query.getResultList();
            jsonArray = new JSONArray();

            for (Object[] obj : resultList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", obj[0]);
                jsonObject.put("customerFirstName", obj[1]);
                jsonObject.put("customerLastName", obj[2]);
                jsonObject.put("customerPhoneNumber", obj[3]);
                jsonObject.put("totalSpent", obj[4]);
                jsonObject.put("customerStreetName", obj[5]);
                jsonObject.put("customerHouseNumber", obj[6]);


                jsonArray.put(jsonObject);
            }

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
        String jsonString = jsonArray.toString();
        System.out.println(jsonString);
        return jsonString;


    }

}
