package com.amt.dflipflop.Controllers;

import com.amt.dflipflop.Entities.Cart;
import com.amt.dflipflop.Entities.ProductSelection;
import com.amt.dflipflop.Services.CartService;
import com.amt.dflipflop.Services.ProductSelectionService;
import com.amt.dflipflop.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Controller
public class CartController {


    // DPE - Vous connaissez lombok ? @AllArgsConstructor
    private final CartService cartService;

    private final ProductService productService;

    private final ProductSelectionService selectionService;

    @Autowired
    public CartController(CartService cartService, ProductService productService, ProductSelectionService selectionService){
        this.cartService = cartService;
        this.productService = productService;
        this.selectionService = selectionService;
    }

    /**
     * Displays the user's cart
     * @return the cart page
     */
    @GetMapping("/cart")
    public String displayCart(Model model, HttpServletRequest req) {

        //DPE - Même si c'est que deux ligne vous avez de la duplication dans toutes vos fonctions
        HttpSession session = req.getSession(true);
        Integer userId =  (Integer) session.getAttribute("id");


        Cart userCart = cartService.getUserCart(userId);
        if(userCart == null)
            return "redirect:/login";
        model.addAttribute("cart", userCart);
        return "cart";
    }

    /**
     * Empties the cart
     * @return the cart page
     */
    @GetMapping("/cart/empty")
    public String emptyCart(HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        Integer userId =  (Integer) session.getAttribute("id");
        cartService.emptyUserCart(userId);
        return "redirect:/cart";
    }

    /**
     * Saves the cart selection
     * @param cart the new cart to save
     * @return Cart page
     * @throws IOException If fails to write the cart
     */
    @PostMapping(path="/cart")
    public String saveCart (@ModelAttribute Cart cart, HttpServletRequest req) throws IOException {
        HttpSession session = req.getSession(true);
        Integer userId =  (Integer) session.getAttribute("id");
        Cart userCart = cartService.updateCart(cart, userId);
        if(userCart == null)
            return "redirect:/login";
        return "redirect:/cart";
    }

    /** Adds a product to the cart
     *
     * @param productId Id of the product to add
     * @param quantity Quantity to add
     * @return
     * @throws IOException
     */
    @PostMapping(path="/cart/add")
    public String addProduct (Integer productId, Integer quantity, HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        Integer userId =  (Integer) session.getAttribute("id");
        if(userId == null)
            return "redirect:/login";
        Cart userCart = cartService.addProduct(productId, quantity, userId);
        return "redirect:/store/product/" + productId;
    }

    /** Removes a product to the cart
     *
     * @param productId Id of the product to remove
     * @return
     * @throws IOException
     */
    @GetMapping(path="/cart/remove/{id}")
    public String removeProduct (@PathVariable("id") Integer productId, HttpServletRequest req) throws IOException {
        HttpSession session = req.getSession(true);
        Integer userId =  (Integer) session.getAttribute("id");
        Cart userCart = cartService.removeProduct(productId, userId);
        return "redirect:/cart";
    }
}

