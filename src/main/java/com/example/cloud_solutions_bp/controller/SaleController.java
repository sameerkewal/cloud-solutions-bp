package com.example.cloud_solutions_bp.controller;

import com.example.cloud_solutions_bp.entities.Manufacturer;
import com.example.cloud_solutions_bp.entities.Product;
import com.example.cloud_solutions_bp.entities.Sale;
import com.example.cloud_solutions_bp.entities.SaleProducts;
import com.example.cloud_solutions_bp.service.CustomerService;
import com.example.cloud_solutions_bp.service.ProductService;
import com.example.cloud_solutions_bp.service.SaleProductsService;
import com.example.cloud_solutions_bp.service.SaleService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/sale")
public class SaleController {


    private final SaleService saleService;
    private final SaleProductsService saleProductsService;
    private final CustomerService customerService;
    private final ProductService productService;


    public SaleController() {
        this.saleService = new SaleService();
        this.saleProductsService = new SaleProductsService();
        this.customerService = new CustomerService();
        this.productService = new ProductService();
    }

    @Path("/get-all-sales")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllSales(){
        return saleProductsService.sale_vw();
//        return "js";
    }


//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response addSale(Sale newSale) {
//        try {
//            // Perform any validation or business logic as needed
//            // For simplicity, let's assume the ManufacturerService has a method to add a new manufacturer
//            Sale addedSale = saleService.add(newSale);
//
//            // Return a success response with the newly added manufacturer
//            return Response.status(Response.Status.CREATED).entity(addedSale).build();
//        } catch (Exception e) {
//            // Handle exceptions and return an error response
//            String errorMessage = "Error adding sale: " + e.getMessage();
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity("{\"error\": \"" + errorMessage + "\"}")
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .build();
//        }
//    }
@POST
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response addSale(Sale newSaleDTO) {
    try {
        Sale newSale = new Sale();
        newSale.setCustomer(customerService.find(newSaleDTO.getCustomer().getId()));
        System.out.println(newSaleDTO.getCustomer());
        Sale addedSale = saleService.add(newSale);


        // Assuming SaleDTO has a List<SaleProductDTO> field named saleProducts
        for (SaleProducts saleProductDTO : newSaleDTO.getSaleProducts()) {
//            System.out.println("prods: " + newSaleDTO.getSaleProducts());
            Product product = productService.find(saleProductDTO.getProduct().getId());

            System.out.println("product: " + product);

            SaleProducts saleProduct = new SaleProducts();
            saleProduct.setSale(addedSale);  // Set the Sale reference here
            saleProduct.setProduct(product);
            saleProduct.setQuantity(saleProductDTO.getQuantity());

            // Add the SaleProducts to the Sale
            saleProductsService.add(saleProduct);
        }


        return Response.status(Response.Status.CREATED).entity(addedSale).build();
    } catch (Exception e) {
        String errorMessage = "Error adding sale: " + e.getMessage();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"" + errorMessage + "\"}")
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}


}
