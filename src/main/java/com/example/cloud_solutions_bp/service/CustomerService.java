package com.example.cloud_solutions_bp.service;

import com.example.cloud_solutions_bp.configuration.JPAConfiguration;
import com.example.cloud_solutions_bp.entities.Customer;
import com.example.cloud_solutions_bp.repositories.CustomerRepository;

import java.util.List;

public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(){
        this.customerRepository = new CustomerRepository(JPAConfiguration.getEntityManagerFactory().createEntityManager());
    }

    public Customer addCustomer(Customer customer){
        return customerRepository.add(customer);
    }

    public Customer updateCustomer(Customer customer){
        return customerRepository.update(customer);
    }

    public Customer find(Integer id){
        return customerRepository.find(id, Customer.class);
    }

    public void deleteCustomer(Customer customer){
        customerRepository.delete(customer);
    }

    public List<Customer> findCustomersByFirstAndLastName(String firstName, String lastName){
        return customerRepository.findCustomersByFirstAndLastName(firstName, lastName);
    }

    public String getAllCustomers(){
        return customerRepository.getAllCustomers();
    }
}
