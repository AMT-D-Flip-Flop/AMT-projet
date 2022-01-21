package com.amt.dflipflop.Services;

import com.amt.dflipflop.Entities.Category;
import com.amt.dflipflop.Entities.Product;
import com.amt.dflipflop.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    public Product insert(Product product) {
        return productRepository.save(product);
    }

    public Long count() {
        return productRepository.count();
    }

    public Product update(Product product) {
        return productRepository.save(product);
    }

    /**
     * Returns a list of products related to the given category
     */
    public ArrayList<Product> getProductsByCategory(Integer cat) {
        Category category = categoryService.get(cat);
        return productRepository.getProductsByCategoriesContains(category);
    }

    public void removeCategoryFromProduct(Product product, Category category) {
        product.removeCategory(category);
        productRepository.save(product);
    }

    public List<Product> getLast3Products(){
        if (count() >= 3) {
            return getAll().subList(0, 3);
        }
        return getAll();
    }

    public Product getRandom(){
        List<Product> all = getAll();
        if(!all.isEmpty()){
            int chosen = (int)(Math.random() * (all.size() - 1));
            return all.get(chosen);
        }
        return null;
    }

    /**
     * Check if the name is already in the database
     *
     * @param name The name to check
     * @return The product with the name if already stored, null otherwise
     */
    public Product nameExist(String name) {
        return productRepository.findByName(name);
    }

    public Product nameExistAndDifferFromId(String name, int id) {
        return productRepository.findByNameAndIdIsNot(name, id);
    }

    public void updateProductCategories(Product product, List<Integer> categoriesId){
        HashSet<Category> categories = new HashSet<Category>();
        for(int id: categoriesId){
            Category category = categoryService.get(id);
            if(category != null){
                categories.add(category);
            }
        }

        product.setCategories(categories);
        update(product);

    }
}