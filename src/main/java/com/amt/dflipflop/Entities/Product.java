package com.amt.dflipflop.Entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @ManyToMany
    private Set<Category> categories;

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

    public Set<Category> getCategories() { return categories; }
    public void setCategories(Set<Category> cats) { categories = cats; }
    public void addCategory(Category cat) { categories.add(cat); }
    public void removeCategory(int index){ categories.remove(index) ;}
    public void removeCategory(Category cat){ categories.remove(cat); }
    public String getCategoriesNames() {
        StringBuilder str = new StringBuilder();

        for(Category cat : categories){
            str.append(" "); // supplementary space is needed in the template, so no problem here
            str.append(cat.getName());
        }

        return str.toString();
    }
    public ArrayList<Integer> getCategoriesId(){
        ArrayList<Integer> ids = new ArrayList<>();
        for(Category cat: categories){
            ids.add(cat.getId());
        }
        return ids;
    }

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