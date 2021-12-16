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
// DPE - De manière générale, il serait intéressant de cacher la logique métier du controller,
// Du coup vos services ferais un peu plus que juste appeler les repositories
// Une piste à explorer : https://herbertograca.com/2017/11/16/explicit-architecture-01-ddd-hexagonal-onion-clean-cqrs-how-i-put-it-all-together/
public class CartController {


    // DPE - Utilisez plutôt un constructeur : https://stackoverflow.com/a/40620318
    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductSelectionService selectionService;

    /**
     * Displays the user's cart
     * @return the cart page
     */
    @GetMapping("/cart")
    public String displayCart(Model model, HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        Integer userId =  (Integer) session.getAttribute("id");
        if(userId == null)
            return "redirect:/login";
        Cart userCart = cartService.getUserCart(userId);
        if(userCart == null){
            userCart= new Cart(userId);
            cartService.save(userCart);
        }
        model.addAttribute("cart", userCart);
        return "cart";
    }

    /**
     * Empties the cart
     * @return the cart page
     */
    @GetMapping("/cart/empty")
    public String emptyCart(Model model, HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        Integer userId =  (Integer) session.getAttribute("id");
        Cart userCart = cartService.getUserCart(userId);
        for ( ProductSelection sel : userCart.getSelections()){
            selectionService.delete(sel);
        }
        userCart.empty();
        cartService.save(userCart);
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
        if(userId == null)
            return "redirect:/login";
        Cart userCart = cartService.getUserCart(userId);
        Integer index = 0;
        for (ProductSelection sel: userCart.getSelections()){
            if(cart.getSelections().get(index).getQuantity() == 0){
                selectionService.delete(sel);
            }
            else{
                sel.setQuantity(cart.getSelections().get(index).getQuantity());
            }
            index++;
            selectionService.save(sel);
        }
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
    public String addProduct (Integer productId, Integer quantity, HttpServletRequest req) throws IOException {
        // Sanity check
        if(quantity < 1)
            quantity = 1;

        // Let's check if we already have a selection for that product
        HttpSession session = req.getSession(true);
        Integer userId =  (Integer) session.getAttribute("id");
        if(userId == null)
            return "redirect:/login";
        Cart userCart = cartService.getUserCart(userId);;
        for ( ProductSelection sel : userCart.getSelections()){
            if(sel.getProduct().getId() == productId){
                sel.setQuantity(sel.getQuantity() + quantity);
                cartService.save(userCart);
                return "redirect:/store/product/" + productId;
            }
        }
        ProductSelection newSel = new ProductSelection();
        newSel.setProduct(productService.get(productId));
        newSel.setQuantity(quantity);
        selectionService.save(newSel);
        userCart.addSelection(newSel);
        cartService.save(userCart);
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
        Cart userCart = cartService.getUserCart(userId);

        for ( ProductSelection sel : userCart.getSelections()){
            if(sel.getProduct().getId() == productId){
                selectionService.delete(sel);
            }
            userCart.getSelections().remove(sel);
            cartService.save(userCart);
            return "redirect:/cart";
        }
        return "redirect:/cart";
    }
}

