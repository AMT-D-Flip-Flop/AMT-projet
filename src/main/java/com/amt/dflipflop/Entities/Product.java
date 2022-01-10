package com.amt.dflipflop.Entities;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
public class Product {

    // needed for SpringBoot Hibernate to create objects
    public Product() {}

    public Product(String name, String description, Float price,  String imageName, Integer quantity){
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageName = imageName;
        this.quantity = quantity;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String name;

    private String description;

    @OneToOne
    private Category category;

    private Float price;

    private String imageName;

    private Integer quantity;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }
    public String getCategoryName() {
        return this.category != null ? category.getName() : "";
    }
    public void setCategory(Category category) {
        this.category = category;
    }

    public String getImageName() {
        return imageName;
    }
    public String getImageRelativePath() {
        return "images/" + imageName;
    } //images pour dev
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Float getPrice() {
        return price;
    }
    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}