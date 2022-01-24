package com.amt.dflipflop.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity @NoArgsConstructor
public class ItemCart {

    public ItemCart(Integer qty, Product product){
        this.product = product;
        this.qty = qty;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Getter @Setter
    private Integer id;

    @Getter @Setter
    private Integer qty;

    @OneToOne
    @Getter
    private Product product;

}