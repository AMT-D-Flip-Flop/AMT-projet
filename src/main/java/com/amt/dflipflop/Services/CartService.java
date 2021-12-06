package com.amt.dflipflop.Services;

import com.amt.dflipflop.Entities.Cart;
import com.amt.dflipflop.Repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductSelectionService selectionService;

    public ArrayList<Cart> getAll() {

        Iterable<Cart> it = cartRepository.findAll();

        ArrayList<Cart> carts = new ArrayList<>();
        it.forEach(carts::add);

        return carts;
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
}