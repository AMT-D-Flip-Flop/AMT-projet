package com.amt.dflipflop.Repositories;

import com.amt.dflipflop.Entities.Category;
import com.amt.dflipflop.Entities.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Set;

// This will be AUTO IMPLEMENTED by Spring into a Bean called productRepository
// CRUD refers Create, Read, Update, Delete

public interface ProductRepository extends CrudRepository<Product, Integer> {
    ArrayList<Product> findAll();
    Integer countByCategoriesContains(Category cat);
    ArrayList<Product> getProductsByCategoriesContains(Category cat);

    Product findByName(String desc);

    // Exemples
    // Product findByCategory(Category category);
    // ArrayList<Product> findAllByDescriptionContains(String desc);

    @Query("SELECT p.categories FROM Product p")
    ArrayList<Set<Category>> getCategories();
}