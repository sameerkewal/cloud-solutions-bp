package com.example.cloud_solutions_bp.controller;

import com.example.cloud_solutions_bp.service.ProductService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController() {
        this.productService = new ProductService();
    }


    @Path("/get-all-products")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllProducts(){
        return productService.getAllProducts();
    }

    @Path("/get-product")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getProduct(@QueryParam("id")Long id){
        return productService.getProduct(id);
    }
}
