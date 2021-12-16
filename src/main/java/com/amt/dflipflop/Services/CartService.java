package com.amt.dflipflop.Services;

import com.amt.dflipflop.Entities.Cart;
import com.amt.dflipflop.Entities.authentification.User;
import com.amt.dflipflop.Repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    // DPE - Supprimer les choses que vous n'utilisez plus
    private ProductSelectionService selectionService;

    public ArrayList<Cart> getAll() {

        Iterable<Cart> it = cartRepository.findAll();

        ArrayList<Cart> carts = new ArrayList<>();
        it.forEach(carts::add);

        return carts;
    }

    public Cart getUserCart(Integer userId) {

        Iterable<Cart> it = cartRepository.findAll();

        // DPE - Normalement intellij vous fit qu'il y a un refactor à faire ici ;)
        for (Iterator<Cart> i = it.iterator(); i.hasNext(); ){
            Cart cart = i.next();
            // DPE - Ici aussi ;)
            if(cart.getUserId() == userId)
                return cart;
        }
        return null;
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