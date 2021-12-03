package com.amt.dflipflop.Repositories;

import com.amt.dflipflop.Entities.Category;
import com.amt.dflipflop.Entities.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called productRepository
// CRUD refers Create, Read, Update, Delete

public interface ProductRepository extends CrudRepository<Product, Integer> {
    //Product findByCategory(Category category);
    //List<Product> findAllByDescriptionContainsAndCategory();

}