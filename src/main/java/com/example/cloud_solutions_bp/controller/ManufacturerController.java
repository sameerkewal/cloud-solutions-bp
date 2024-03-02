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


    @PUT
    @Path("/{id}")
    public Response updateManufacturer(@PathParam("id") Integer id, Manufacturer updatedManufacturer) {
        try {
            // Retrieve the existing manufacturer from the database
            Manufacturer existingManufacturer = manufacturerService.find(id);

            // Check if the manufacturer exists
            if (existingManufacturer == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Manufacturer not found").build();
            }

            // Update the properties of the existing manufacturer
            existingManufacturer.setName(updatedManufacturer.getName());
            existingManufacturer.setCountry(updatedManufacturer.getCountry());

            // Save the updated manufacturer
            manufacturerService.update(existingManufacturer);

            return Response.ok(existingManufacturer, MediaType.APPLICATION_JSON_TYPE).build();
        } catch (Exception e) {
            String errorMessage = "Error deleting manufacturer: " + e.getMessage();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + errorMessage + "\"}")
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addManufacturer(Manufacturer newManufacturer) {
        try {
            // Perform any validation or business logic as needed
            // For simplicity, let's assume the ManufacturerService has a method to add a new manufacturer
            manufacturerService.addManufacturer(newManufacturer);

            // Return a success response with the newly added manufacturer
            return Response.status(Response.Status.CREATED).entity(newManufacturer).build();
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
