package com.amt.dflipflop.Controllers;

import com.amt.dflipflop.Entities.Category;
import com.amt.dflipflop.Entities.Product;
import com.amt.dflipflop.Services.CategoryService;
import com.amt.dflipflop.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.nio.file.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import static com.amt.dflipflop.Constants.*;

@Controller
public class CategoriesController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/categories")
    public String displayCategoriesAndForm(Model model, RedirectAttributes redirectAttrs) {
        ArrayList<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        model.addAttribute("category", new Category());

        if(redirectAttrs.containsAttribute(SUCCESS_MSG_KEY)){
            model.addAttribute(SUCCESS_MSG_KEY, redirectAttrs.getAttribute(SUCCESS_MSG_KEY));
        }
        if(redirectAttrs.containsAttribute(ERROR_MSG_KEY)){
            model.addAttribute(ERROR_MSG_KEY, redirectAttrs.getAttribute(ERROR_MSG_KEY));
        }

        return "categories";
    }

    /**
     * @param category The category get from the form (front-end)
     * @return The redirection to a page
     * @throws IOException If write fail
     */
    @PostMapping(path="/categories/add-category") // Map ONLY POST Requests
    public String addNewCategory (@ModelAttribute Category category, RedirectAttributes redirectAttrs) throws IOException {


        if(categoryService.categoryExists(category.getName())){
            redirectAttrs.addFlashAttribute(ERROR_MSG_KEY, "Category already exists");
            return "redirect:/categories";
        }

        redirectAttrs.addFlashAttribute(SUCCESS_MSG_KEY, "Category was added successfully");

        // Add the category via a category service
        categoryService.insert(category);
        return "redirect:/categories";
    }

    /**
     * Called with /categories/remove?id=N
     * @return The redirection to a page
     * @throws IOException If suppress fail
     */
    @GetMapping(path="/categories/remove")
    public String removeCategory (
            @RequestParam(value = "id") Integer id,
            @RequestParam(value = "confirm", required = false) Boolean confirm,
            RedirectAttributes redirectAttrs,
            Model model
    ) throws IOException {

        // Ask for confirmation if the category is used in articles
        if(!categoryService.isCategoryEmpty(id)){

            Category category = categoryService.get(id);
            ArrayList<Product> products = productService.getProductsByCategory(id);
            if(confirm == null || !confirm){
                model.addAttribute("products", products);
                model.addAttribute("category", category);
                redirectAttrs.addFlashAttribute(ERROR_MSG_KEY, "Category is not empty");
                return "categories_del_confirmation";
            }
            else{
                for(Product product : products){
                    productService.removeCategoryFromProduct(product, category);
                }
            }
        }

        // Add the category via a category service
        categoryService.remove(id);
        redirectAttrs.addFlashAttribute(SUCCESS_MSG_KEY, "Category was removed successfully");
        return "redirect:/categories";
    }

    /**
     * Assign the categories to the product
     * @return The redirection to a page
     * @throws IOException If suppress fail
     */
    @PostMapping(path="/categories/set_categories")
    public String setCategory (
            @RequestParam(value = "product") Integer productId,
            @RequestParam(value = "cat") List<Integer> categoriesId,
            RedirectAttributes redirectAttrs) throws IOException {

        Product product = productService.get(productId);

        if (product == null)
        {
            redirectAttrs.addFlashAttribute(ERROR_MSG_KEY, "This product doesn't exist");
            return "redirect:/store";
        }

        HashSet<Category> categories = new HashSet<Category>();
        for(int id: categoriesId){
            Category category = categoryService.get(id);
            if(category != null){
                categories.add(category);
            }
        }

        product.setCategories(categories);
        productService.update(product);

        redirectAttrs.addFlashAttribute(SUCCESS_MSG_KEY, "Updated the product successfully");
        return "redirect:/store/product/" + productId;
    }

}

