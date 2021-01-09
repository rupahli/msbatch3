package com.sl.ms.ordermanagement.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
//import javax.validation.constraints.NotNull;

@Entity
@Table(name = "SL_ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

   // @NotNull
    private String name;

    @Column(name = "total_amount")
    private double amount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<Item> items = new HashSet<>();

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;

        for(Item b : items) {
            b.setOrder(this);
        }
    }
}
