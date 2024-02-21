package com.example.cloud_solutions_bp.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Adress", uniqueConstraints = @UniqueConstraint(columnNames = {"streetname", "housenumber"}))
public class Adress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "streetname", nullable = false, length = 100)

    private String streetname;

    @Column(name = "housenumber", nullable = false)
    private Integer housenumber;

    @Column(name = "neighborhood", nullable = true, length = 100)
    private String neighborhood;

    @OneToMany(mappedBy = "adress")
    private List<Customer> customers;

    public List<Customer> getCustomers() {
        return customers;
    }



    public Adress(String streetname, Integer housenumber, String neighborhood) {
        this.streetname = streetname;
        this.housenumber = housenumber;
        this.neighborhood = neighborhood;
    }





    public Adress() {
    }

    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreetname() {
        return streetname;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    public Integer getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(Integer housenumber) {
        this.housenumber = housenumber;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String toString(){
        return this.getId()+": " +this.streetname+" " + this.housenumber+" in neighborhood " + this.neighborhood;

    }
}
