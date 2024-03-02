package com.example.cloud_solutions_bp.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.RollbackException;
import com.example.cloud_solutions_bp.entities.Customer;
import org.hibernate.exception.ConstraintViolationException;

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

    public List<Customer> getAllCustomers() {
        EntityTransaction transaction = entityManager.getTransaction();
        List<Customer> resultList = new ArrayList<>();

        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Query query = entityManager.createQuery("select cmr from Customer  cmr");
            resultList = query.getResultList();
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
        return resultList;

    }

}
