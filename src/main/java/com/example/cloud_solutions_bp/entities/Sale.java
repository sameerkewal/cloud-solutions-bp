package com.example.cloud_solutions_bp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    @Column(nullable = false)

    private Timestamp sale_date;


    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;


    @OneToMany(mappedBy = "sale")
    @JsonManagedReference //owning side of the relationship, basically the parent
    private Set<SaleProducts> saleProducts = new HashSet<>();


    public Set<SaleProducts> getSaleProducts() {
        return saleProducts;
    }

    public void setSaleProducts(Set<SaleProducts> saleProducts) {
        this.saleProducts = saleProducts;
    }


    public void addSaleProduct(SaleProducts saleProduct) {
        saleProducts.add(saleProduct);
        saleProduct.setSale(this);
    }

    public Sale(Timestamp sale_date, Customer customer) {
        this.sale_date = sale_date;
        this.customer = customer;
//        this.products = products;
    }

    public Sale() {

    }

    public Timestamp getSale_date() {
        return sale_date;
    }

    public void setSale_date(Timestamp sale_date) {
        this.sale_date = sale_date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @PrePersist
    public void prePersist() {
        if (sale_date == null) {
            sale_date = Timestamp.from(Instant.now());
        }
    }



    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

//    public Set<Product> getProducts() {
//        return products;
//    }

    @Override
    public String toString() {
        return "Sales{" +
                "id=" + id +
                ", sales_date=" + sale_date +
                ", customer=" + customer +
//                ", products=" + products +
                '}';
    }




//    public void addProducts(Product product){
//        boolean added = products.add(product);
//
//        if(added){
//            product.
//        }
//    }



}


