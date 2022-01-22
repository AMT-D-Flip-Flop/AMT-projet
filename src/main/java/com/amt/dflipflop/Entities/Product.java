package com.amt.dflipflop.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.amt.dflipflop.Constants.IS_PROD;

@Entity // This tells Hibernate to make a table out of this class
@NoArgsConstructor
public class Product {

    public Product(String name, String description, Float price,  String imageName, Integer quantity){
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageName = imageName;
        this.quantity = quantity;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Getter @Setter
    private Integer id;

    @ManyToMany
    @Getter @Setter
    private Set<Category> categories;

    @Getter @Setter private String name;
    @Getter @Setter private String description;
    @Getter @Setter private Float price;
    @Getter @Setter private String imageName;
    @Getter @Setter private Integer quantity;


    public void addCategory(Category cat) { categories.add(cat); }
    public void removeCategory(int index){ categories.remove(index) ;}
    public void removeCategory(Category cat){ categories.remove(cat); }
    public String getCategoriesNames() {
        StringBuilder str = new StringBuilder();

        for(Category cat : categories){
            str.append(" "); // supplementary space is needed in the template, so no problem here
            str.append(cat.getName());
            str.append(",");
        }
        str.setLength(str.length()-1); // remove last comma

        return str.toString();
    }
    public ArrayList<Integer> getCategoriesId(){
        ArrayList<Integer> ids = new ArrayList<>();
        for(Category cat: categories){
            ids.add(cat.getId());
        }
        return ids;
    }

    public String getImageRelativePath() {
        if (IS_PROD){
            return "img/" + imageName;
        } else {
            return "images/" + imageName;
        }
    }
}