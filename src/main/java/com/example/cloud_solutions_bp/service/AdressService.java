package com.example.cloud_solutions_bp.service;

import com.example.cloud_solutions_bp.configuration.JPAConfiguration;
import com.example.cloud_solutions_bp.entities.Adress;
import com.example.cloud_solutions_bp.repositories.AdresRepository;

import java.util.List;

public class AdressService {

    private final AdresRepository adresRepository;


    public AdressService() {
        this.adresRepository = new AdresRepository(JPAConfiguration.getEntityManager());

    }



    public Adress createAdress(Adress adress){
        return adresRepository.add(adress);
    }

    public Adress updateAdress(Adress adress){
        return adresRepository.update(adress);
    }

    public void deleteAdress(Adress adress){
        adresRepository.delete(adress);
    }


    public Adress findAdress(Integer id){
        return adresRepository.find(id, Adress.class);
    }


    public List<Adress> findAdressesByStreetName(String streetname){
        return adresRepository.findAdressesByStreetName(streetname);
    }

    public Adress findAdressesByStreetNameAndHouseNumber(String streetName, int houseNumber){
        return adresRepository.findAdressByStreetNameAndHouseNumber(streetName, houseNumber);
    }



}
