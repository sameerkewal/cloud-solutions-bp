package com.example.cloud_solutions_bp.controller;

import com.example.cloud_solutions_bp.entities.Manufacturer;
import com.example.cloud_solutions_bp.entities.Product;
import com.example.cloud_solutions_bp.entities.Sale;
import com.example.cloud_solutions_bp.entities.SaleProducts;
import com.example.cloud_solutions_bp.observerpattern.EventManager;
import com.example.cloud_solutions_bp.observerpattern.SmsNotificationListener;
import com.example.cloud_solutions_bp.service.CustomerService;
import com.example.cloud_solutions_bp.service.ProductService;
import com.example.cloud_solutions_bp.service.SaleProductsService;
import com.example.cloud_solutions_bp.service.SaleService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/sale")
public class SaleController {


    private final SaleService saleService;
    private final SaleProductsService saleProductsService;
    private final CustomerService customerService;
    private final ProductService productService;

    private final EventManager events;


    public SaleController() {
        this.saleService = new SaleService();
        this.saleProductsService = new SaleProductsService();
        this.customerService = new CustomerService();
        this.productService = new ProductService();

        this.events = new EventManager("Add Sale");
        this.events.subscribe("Add Sale", new SmsNotificationListener());
    }

    @Path("/get-all-sales")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllSales(){
        return saleProductsService.sale_vw();

    }


@POST
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response addSale(Sale saleToAdd) {
    try {
        Sale newSale = new Sale();
        newSale.setCustomer(customerService.find(saleToAdd.getCustomer().getId()));
        System.out.println(saleToAdd.getCustomer());
        Sale addedSale = saleService.add(newSale);


        for (SaleProducts saleProductDTO : saleToAdd.getSaleProducts()) {
            Product product = productService.find(saleProductDTO.getProduct().getId());


            SaleProducts saleProduct = new SaleProducts();
            saleProduct.setSale(addedSale);  // Set the Sale reference here
            saleProduct.setProduct(product);
            saleProduct.setQuantity(saleProductDTO.getQuantity());

            // Add the SaleProducts to the Sale
            saleProductsService.add(saleProduct);
        }

        StringBuilder boughtItems = new StringBuilder(addedSale.getCustomer().getFirstname() + " " + addedSale.getCustomer().getLastname() + ", you bought: ");
        for(SaleProducts saleProducts: saleService.getProductsFromSale(addedSale)){
            boughtItems.append("\n" + saleProducts.getProduct().getName() + "(" + saleProducts.getQuantity() + ") ");
        }
        events.notify("Add Sale", String.valueOf(boughtItems), addedSale.getCustomer().getPhonenumber());



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





