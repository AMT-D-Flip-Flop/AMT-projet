package com.amt.dflipflop.Repositories;

import com.amt.dflipflop.Entities.Product;
import com.amt.dflipflop.Entities.ProductSelection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface ProductSelectionRepository extends CrudRepository<ProductSelection, Integer> {
    @Query("SELECT p.product FROM ProductSelection p")
    ArrayList<Product> getDistinctTop2();
}
