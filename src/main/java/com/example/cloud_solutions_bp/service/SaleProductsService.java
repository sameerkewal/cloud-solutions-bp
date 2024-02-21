package com.example.cloud_solutions_bp.service;

import com.example.cloud_solutions_bp.configuration.JPAConfiguration;
import com.example.cloud_solutions_bp.entities.Sale;
import com.example.cloud_solutions_bp.entities.SaleProducts;
import com.example.cloud_solutions_bp.repositories.SaleProductsRepository;

import java.time.LocalDateTime;
import java.util.List;

public class SaleProductsService {

    private final SaleProductsRepository saleProductsRepository;


    public SaleProductsService() {
        this.saleProductsRepository = new SaleProductsRepository(JPAConfiguration.getEntityManager());

    }

    public SaleProducts add(SaleProducts saleProducts) {

        return saleProductsRepository.add(saleProducts);

    }

    public boolean checkIfSaleHasProductsAddedToIt(Sale sale) {
        return saleProductsRepository.checkIfSaleHasProductsAddedToIt(sale);
    }


    public void deleteSaleProducts(Sale sale) {
        saleProductsRepository.deleteSaleProducts(sale);
    }

    public void getSalesBasedOnProducts() {
        saleProductsRepository.getSalesBasedOnProducts();
    }

    public void getMostValuableCustomers() {
        saleProductsRepository.getMostValuableCustomers();
    }

    public List<SaleProducts> getSalesBasedOnDate(LocalDateTime localDateTime) {
        return saleProductsRepository.getSalesBasedOnDate(localDateTime);
    }

}
