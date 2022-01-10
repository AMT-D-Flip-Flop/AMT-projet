package com.amt.dflipflop.Services;

import com.amt.dflipflop.Entities.Category;
import com.amt.dflipflop.Repositories.CategoryRepository;
import com.amt.dflipflop.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.HashSet;
import java.util.Set;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public ArrayList<Category> getAll() {
        return categoryRepository.findAll();
    }

    public ArrayList<Category> getNonEmpty() {
        ArrayList<Set<Category>> categoriesLists = productRepository.getCategories();
        HashSet<Category> categoriesNonEmpty = new HashSet<>();
        for(Set<Category> cats : categoriesLists){
            categoriesNonEmpty.addAll(cats);
        }
        return new ArrayList<Category>(categoriesNonEmpty);
    }

    public Category get(Integer id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElse(null);
    }

    public Category insert(Category category){
        return categoryRepository.save(category);
    }

    public void remove(Integer id){
        categoryRepository.deleteById(id);
    }

    public boolean categoryExists(String name) {
        return categoryRepository.existsCategoryByName(name);
    }

    public boolean isCategoryEmpty(Integer id){
        Category cat = this.get(id);
        return (productRepository.countByCategoriesContains(cat) == 0);
    }

    public Long count() {
        return categoryRepository.count();
    }
}