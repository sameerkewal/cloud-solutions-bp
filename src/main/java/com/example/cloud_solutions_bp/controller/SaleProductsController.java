package com.example.cloud_solutions_bp.controller;

import com.example.cloud_solutions_bp.entities.SaleProducts;
import com.example.cloud_solutions_bp.service.SaleProductsService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("saleproduct")
public class SaleProductsController {

    private final SaleProductsService saleProductsService;

    public SaleProductsController() {
        this.saleProductsService = new SaleProductsService();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSaleProduct(SaleProducts newSaleproduct) {
        try {
            // Perform any validation or business logic as needed
            // For simplicity, let's assume the ManufacturerService has a method to add a new manufacturer
            SaleProducts addedSaleProduct = saleProductsService.add(newSaleproduct);

            // Return a success response with the newly added manufacturer
            return Response.status(Response.Status.CREATED).entity(addedSaleProduct).build();
        } catch (Exception e) {
            // Handle exceptions and return an error response
            String errorMessage = "Error adding manufacturer: " + e.getMessage();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + errorMessage + "\"}")
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }
    }
}
