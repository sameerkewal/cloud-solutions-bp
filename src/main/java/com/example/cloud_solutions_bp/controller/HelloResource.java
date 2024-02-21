package com.example.cloud_solutions_bp.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;


@Path("/hello-world")
public class HelloResource {
    @GET
    @Produces("text/plain")
    public String hello() {
//        CustomerRepository customerRepository = new CustomerRepository(JPAConfiguration.getEntityManager());
//        return customerRepository.findCustomersByFirstAndLastName("Sameer", "Kewal").toString();
            return "wt";

    }
}