package com.amt.dflipflop.Entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "carts")
public class Cart {

    public Cart(){
    // Add user

        this.selections = new ArrayList<ProductSelection>();
        this.submitted = false;
    }

    // Use the user as the id
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private boolean submitted;

    @OneToMany
    @JoinColumn(name = "selection_id")
    private List<ProductSelection> selections;

    public boolean isSubmitted() {
        return submitted;
    }

    public float getTotal(){
        float total = 0.f;
        for (ProductSelection selection: this.selections) {
            total += selection.getProduct().getPrice() * selection.getQuantity();
        }
        return total;
    }

    public Integer getId(){
        return this.id;
    }

    public List<ProductSelection> getSelections() {
        return this.selections;
    }

    public void empty(){
        this.selections.clear();
    }

    public void addSelection(ProductSelection selection) {
        this.selections.add(selection);
    }

    public void setSelections(List<ProductSelection> selections) {
        this.selections = selections;
    }
}
