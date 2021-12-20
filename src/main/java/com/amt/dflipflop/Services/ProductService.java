package com.amt.dflipflop.Services;

import com.amt.dflipflop.Entities.Category;
import com.amt.dflipflop.Entities.Product;
import com.amt.dflipflop.Repositories.CategoryRepository;
import com.amt.dflipflop.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private CategoryService categoryService;

        public ArrayList<Product> getAll() {

            Iterable<Product> it = productRepository.findAll();

            ArrayList<Product> products = new ArrayList<Product>();
            it.forEach(products::add);

            return products;
        }

        public Product get(Integer id) {
            Optional<Product> product = productRepository.findById(id);
            return product.orElse(null);
        }

        public Product insert(Product product){
            return productRepository.save(product);
        }

        public Long count() {
            return productRepository.count();
        }
  
        public Product update(Product product){
            return productRepository.save(product);
        }

    /**
     * Returns a list of products related to the given category
     */
    public ArrayList<Product> getProductsByCategory(Integer cat){
        Category category = categoryService.get(cat);
        return productRepository.getProductsByCategoriesContains(category);
    }

    public void removeCategoryFromProduct(Product product, Category category){
        product.removeCategory(category);
        productRepository.save(product);
    }
}