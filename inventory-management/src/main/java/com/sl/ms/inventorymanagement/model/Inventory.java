package com.sl.ms.inventorymanagement.model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sl_inv")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "inv_date")
    private Date date;

    @Transient
    private String fileName;

    @Lob
    @Column(name = "data_file", columnDefinition="BLOB")
    private byte[] file;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL)
    private Set<Product> products = new HashSet<>();



    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;

        for(Product b : products) {
            b.setInventory(this);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

}
