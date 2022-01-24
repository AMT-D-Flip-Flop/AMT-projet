package com.amt.dflipflop.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "productSelection")
@NoArgsConstructor
public class ProductSelection{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Getter
    private Integer id;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    @Getter @Setter
    private Product product;

    @Getter @Setter private Integer quantity;

    public ProductSelection(Product product, Integer quantity){
        this.product = product;
        this.quantity = quantity;
    }


}