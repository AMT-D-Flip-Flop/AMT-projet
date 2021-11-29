package com.amt.dflipflop.Entities;

import javax.persistence.*;
import java.util.List;

@Entity // This tells Hibernate to make a table out of this class
public class Product {

    // needed for SpringBoot Hibernate to create objects
    public Product() {}

    public Product(String name, String description, Float price,  String imageName){
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageName = imageName;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String name;

    private String description;

    @OneToOne
    private Category category;

    @OneToMany
    private List<Category> categories;

    private Float price;

    private String imageName;

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

    public List<Category> getCategories() { return categories; }
    public void getCategories(List<Category> cats) { categories = cats; }
    public void addCategory(Category cat) { categories.add(cat); }
    public void removeCategory(Integer id){ }

    public String getImageName() {
        return imageName;
    }
    public String getImageRelativePath() {
        return "images/" + imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Float getPrice() {
        return price;
    }
    public void setPrice(Float price) {
        this.price = price;
    }

}