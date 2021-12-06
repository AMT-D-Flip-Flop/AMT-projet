package com.amt.dflipflop.Entities;

import javax.persistence.*;

@Entity
public class ItemCart {

    // needed for SpringBoot Hibernate to create objects
    public ItemCart() {}

    public ItemCart(Integer qty, Product product){
        this.product = product;
        this.qty = qty;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private Integer qty;

    @OneToOne
    private Product product;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct(){
        return product;
    }

    public Integer getQty(){
        return qty;
    }
    public void setQty(Integer qty){
        this.qty = qty;
    }

}