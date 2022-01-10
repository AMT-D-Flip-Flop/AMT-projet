package com.amt.dflipflop;


import com.amt.dflipflop.Entities.Cart;
import com.amt.dflipflop.Entities.Product;
import com.amt.dflipflop.Entities.ProductSelection;
import com.amt.dflipflop.Repositories.CartRepository;
import com.amt.dflipflop.Repositories.ProductRepository;
import com.amt.dflipflop.Repositories.ProductSelectionRepository;
import com.amt.dflipflop.Services.CartService;
import com.amt.dflipflop.Services.ProductSelectionService;
import com.amt.dflipflop.Services.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.test.annotation.Rollback;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(true)
public class CartServiceTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSelectionRepository selectionRepository;


    @Test
    public void testCartCreation() throws Exception {
        Cart cart = new Cart(0);
        Cart saved = cartRepository.save(cart);

        Assertions.assertNotNull(saved);
        Cart existCart = entityManager.find(Cart.class, saved.getId());
        Assertions.assertNotNull(existCart);
    }

    @Test
    public void testSelectionAdd() throws Exception {
        Product p = new Product("Honda", "Civic", (float) 12.5, "2", 5);
        productRepository.save(p);

        Cart cart = new Cart(0);
        ProductSelection sel = new ProductSelection();
        sel.setProduct(p);
        sel.setQuantity(2);
        selectionRepository.save(sel);
        cart.addSelection(sel);
        Cart saved = cartRepository.save(cart);

        Cart newCart = entityManager.find(Cart.class, saved.getId());

        Assertions.assertNotNull(newCart);

        Assertions.assertEquals(newCart.getSelections().get(0).getProduct().getId(), p.getId());
        Assertions.assertEquals(newCart.getSelections().get(0).getQuantity(), 2);
    }

}
