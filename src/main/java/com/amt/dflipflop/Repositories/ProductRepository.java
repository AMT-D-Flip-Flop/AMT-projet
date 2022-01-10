package com.amt.dflipflop.Repositories;

import com.amt.dflipflop.Entities.Product;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called productRepository
// CRUD refers Create, Read, Update, Delete

public interface ProductRepository extends CrudRepository<Product, Integer> {
    Product findByName(String desc);

    // Exemples
    // Product findByCategory(Category category);
    // ArrayList<Product> findAllByDescriptionContains(String desc);

}