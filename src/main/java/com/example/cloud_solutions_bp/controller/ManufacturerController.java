package com.example.cloud_solutions_bp.controller;

import com.example.cloud_solutions_bp.entities.Manufacturer;
import com.example.cloud_solutions_bp.service.ManufacturerService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/manufacturer")
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    public ManufacturerController() {
        this.manufacturerService = new ManufacturerService();
    }

    @Path("get-all-manufacturers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Manufacturer>getAllManufacturers(){
        return manufacturerService.getAllManufacturers();
    }


    @Path("get-manufacturer")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Manufacturer getManufacturer(@QueryParam("id")Integer id){
        return manufacturerService.find(id);
    }
    @DELETE
    @Path("/{id}")
    public Response deleteManufacturer(@PathParam("id") Integer id) {
        try {
            // Call your service to delete the manufacturer
            Manufacturer manufacturerToDelete = manufacturerService.find(id);
            manufacturerService.removeManufacturer(manufacturerToDelete);
           return Response.ok(manufacturerToDelete, MediaType.APPLICATION_JSON_TYPE).build();

        } catch (Exception e) {
            String errorMessage = "Error deleting manufacturer: " + e.getMessage();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + errorMessage + "\"}")
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }
    }
}
