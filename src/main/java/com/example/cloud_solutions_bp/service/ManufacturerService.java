package com.example.cloud_solutions_bp.service;

import com.example.cloud_solutions_bp.configuration.JPAConfiguration;
import com.example.cloud_solutions_bp.entities.Manufacturer;
import com.example.cloud_solutions_bp.repositories.ManufacturerRepository;

import java.util.List;

public class ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerService() {
        this.manufacturerRepository = new ManufacturerRepository(JPAConfiguration.getEntityManagerFactory().createEntityManager());


    }
    public Manufacturer addManufacturer(Manufacturer manufacturer){
        return manufacturerRepository.add(manufacturer);
    }

    public Manufacturer find(Integer id){
        return manufacturerRepository.find(id, Manufacturer.class);
    }

    public Manufacturer update(Manufacturer manufacturer){
        return manufacturerRepository.update(manufacturer);
    }

    public Manufacturer findManufacturerByName(String name){
        return manufacturerRepository.findManufacturerByName(name);
    }


    public void removeManufacturer(Manufacturer manufacturer){
        manufacturerRepository.delete(manufacturer);
    }

    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.getAllManufacturers();
    }
}
