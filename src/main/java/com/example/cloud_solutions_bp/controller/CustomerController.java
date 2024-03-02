package com.example.cloud_solutions_bp.controller;

import com.example.cloud_solutions_bp.entities.Customer;
import com.example.cloud_solutions_bp.service.CustomerService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import javax.print.attribute.standard.Media;
import java.util.List;

@Path("/customer")
public class CustomerController {

    private final CustomerService customerService;


    public CustomerController(){
        this.customerService = new CustomerService();
    }

    @Path("/get-all-customers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Customer>getAllCustomers(){
        return customerService.getAllCustomers();
    }

}
