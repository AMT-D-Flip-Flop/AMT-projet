package com.amt.dflipflop.Controllers;

import com.amt.dflipflop.Entities.Product;
import com.amt.dflipflop.Entities.ProductSelection;
import com.amt.dflipflop.Services.ProductSelectionService;
import com.amt.dflipflop.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class HomeController {

    final static Integer NB_LATEST_PRODUCTS = 3;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductSelectionService productSelectionService;

    @GetMapping("/")
    public String index(Model model) {
        List<Product> latest_products = productService.getLast3Products();
        List<Product> trends = productSelectionService.getTop2Products();
        Product favorite = productService.getRandom();
        model.addAttribute("latest_products", latest_products);
        model.addAttribute("trends", trends);
        model.addAttribute("favorite", favorite);
        return "index";
    }

}
