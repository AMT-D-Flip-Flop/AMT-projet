package com.amt.dflipflop.Repositories;

import com.amt.dflipflop.Entities.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

// This will be AUTO IMPLEMENTED by Spring into a Bean called productRepository
// CRUD refers Create, Read, Update, Delete
public interface CategoryRepository extends CrudRepository<Category, Integer> {
    Boolean existsCategoryByName(String name);
    ArrayList<Category> findAll();
}