package com.amt.dflipflop.Services;

import com.amt.dflipflop.Entities.Cart;
import com.amt.dflipflop.Entities.ProductSelection;
import com.amt.dflipflop.Repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;

    private final ProductSelectionService selectionService;

    private final ProductService productService;

    @Autowired
    public CartService(CartRepository cartRepository, ProductSelectionService selectionService, ProductService productService){
        this.cartRepository = cartRepository;
        this.selectionService = selectionService;
        this.productService = productService;
    }

    public ArrayList<Cart> getAll() {

        Iterable<Cart> it = cartRepository.findAll();

        ArrayList<Cart> carts = new ArrayList<>();
        it.forEach(carts::add);

        return carts;
    }

    public Cart getUserCart(Integer userId) {
        if(userId == null)
            return null;
        Iterable<Cart> it = cartRepository.findAll();

        for (Cart cart : it) {
            if (Objects.equals(cart.getUserId(), userId))
                return cart;
        }

        return save(new Cart(userId));
    }

    // This needs to be replaced to a User
    public Cart get(Integer id) {
        Optional<Cart> cart = cartRepository.findById(id);
        return cart.orElse(null);
    }

    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    public void remove(Integer id){
        cartRepository.deleteById(id);
    }

    public Long count() {
        return cartRepository.count();
    }

    public void emptyUserCart(Integer userId){
        Cart userCart = getUserCart(userId);
        for ( ProductSelection sel : userCart.getSelections()){
            selectionService.delete(sel);
        }
        userCart.empty();
        save(userCart);
    }

    public Cart updateCart(Cart cart, Integer userId){
        if(userId == null)
            return null;
        Cart userCart = getUserCart(userId);
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
        return userCart;
    }

    public Cart addProduct(Integer productId, Integer quantity, Integer userId){
        // Sanity check
        if(quantity < 1)
            quantity = 1;

        Cart userCart = getUserCart(userId);;
        for ( ProductSelection sel : userCart.getSelections()){
            if(sel.getProduct().getId() == productId){
                sel.setQuantity(sel.getQuantity() + quantity);
                save(userCart);
                return null;
            }
        }
        ProductSelection newSel = new ProductSelection();
        newSel.setProduct(productService.get(productId));
        newSel.setQuantity(quantity);
        selectionService.save(newSel);
        userCart.addSelection(newSel);
        save(userCart);
        return userCart;
    }

    public Cart removeProduct(Integer productId, Integer userId){
        Cart userCart = getUserCart(userId);
        for ( ProductSelection sel : userCart.getSelections()){
            if(sel.getProduct().getId() == productId){
                selectionService.delete(sel);
            }
            userCart.getSelections().remove(sel);
            save(userCart);
            break;
        }
        return userCart;
    }
}