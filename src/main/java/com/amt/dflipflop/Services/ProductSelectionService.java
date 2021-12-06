package com.amt.dflipflop.Services;

import com.amt.dflipflop.Entities.Product;
import com.amt.dflipflop.Entities.ProductSelection;
import com.amt.dflipflop.Repositories.ProductRepository;
import com.amt.dflipflop.Repositories.ProductSelectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductSelectionService {

    @Autowired
    private ProductSelectionRepository productSelectionRepository;

    public ArrayList<ProductSelection> getAll() {

        Iterable<ProductSelection> it = productSelectionRepository.findAll();

        ArrayList<ProductSelection> selections = new ArrayList<>();
        it.forEach(selections::add);

        return selections;
    }

    public ProductSelection get(Integer id) {
        Optional<ProductSelection> selection = productSelectionRepository.findById(id);
        return selection.orElse(null);
    }

    public void delete(ProductSelection selection) {
        productSelectionRepository.delete(selection);
    }

    // should we make it return void ?
    public Boolean insert(ProductSelection selection){
        productSelectionRepository.save(selection);
            /*try {
                productRepository.save(product);
            }
            catch(Exception e){
                return false;
            }*/
        return true;
    }

    public Long count() {
        return productSelectionRepository.count();
    }

    public ProductSelection save(ProductSelection selection){
        return productSelectionRepository.save(selection);
    }

}