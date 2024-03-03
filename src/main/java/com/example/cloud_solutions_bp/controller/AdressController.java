package com.example.cloud_solutions_bp.controller;

import com.example.cloud_solutions_bp.service.AdressService;
import com.example.cloud_solutions_bp.entities.Adress;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/adress")
public class AdressController {

    private final AdressService adressService;

    public AdressController() {
        this.adressService = new AdressService();
    }

    @Path("get-all-addresses")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Adress> getAllAddresses(){
        return adressService.getAllAddresses();
    }
}
