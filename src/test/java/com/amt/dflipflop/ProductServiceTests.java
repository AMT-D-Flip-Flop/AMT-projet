/**
 * Date de création     : 16.10.2021
 * Groupe               : AMT-D-Flip-Flop
 * Description          : Tester ProductService et StoreController
 * Remarque             : -
 * Sources :
 * https://www.baeldung.com/junit-5-runwith
 * https://www.codejava.net/frameworks/spring-boot/user-registration-and-login-tutorial
 * junit test : https://www.arhohuttunen.com/junit-5-assertions/
 */

package com.amt.dflipflop;


import com.amt.dflipflop.Entities.Product;
import com.amt.dflipflop.Repositories.ProductRepository;
import com.amt.dflipflop.Services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.test.annotation.Rollback;
import org.junit.jupiter.api.Assertions;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(true)
public class ProductServiceTests {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ProductRepository productRepository;

    @MockBean
    private ProductService productService;


    @Test
    public void testExample() throws Exception {
        Product p = new Product("Honda", "Civic", (float) 12.5, "2", 5);
        //Product savedUser = this.productService.insert(p);
        Product savedUser = this.productRepository.save(p);

        Assertions.assertNotNull(savedUser);
        Product existUser = entityManager.find(Product.class, savedUser.getId());
        Assertions.assertNotNull(existUser);
    }

}
