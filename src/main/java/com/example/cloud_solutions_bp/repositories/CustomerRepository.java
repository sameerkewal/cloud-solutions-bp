package com.example.cloud_solutions_bp.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import com.example.cloud_solutions_bp.entities.Customer;

import java.util.List;

public class CustomerRepository extends Repository<Customer> {

    private EntityManager entityManager;


    public CustomerRepository(EntityManager entityManager){
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


    public List<Customer>findCustomersByFirstAndLastName(String firstName, String lastName){
        this.entityManager.getTransaction().begin();

        Query query = entityManager.createQuery("" +
                "select  cmr " +
                "from   Customer cmr " +
                "where  lower(firstname)=lower( :p1) " +
                "and    lower(lastname)=lower( :p2)");

        query.setParameter("p1", firstName);
        query.setParameter("p2", lastName);

        List<Customer>resultList = query.getResultList();
        entityManager.getTransaction().commit();
        return resultList;



    }

    public void getAllCustomers(){
        this.entityManager.getTransaction().begin();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);

        Root<Customer> CustomerRoot = criteriaQuery.from(Customer.class);
        criteriaQuery.select(CustomerRoot);
        Query query = entityManager.createQuery(criteriaQuery);

        List<Customer> CustomerList = query.getResultList();

        for(Customer Customer: CustomerList){
            System.out.println(Customer);
        }

        // Commit the transaction (assuming you're in a transactional context)
        entityManager.getTransaction().commit();



    }
    
}
