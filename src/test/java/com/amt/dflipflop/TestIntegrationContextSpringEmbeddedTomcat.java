/**
 * Date de création     : 16.10.2021
 * Groupe               : AMT-D-Flip-Flop
 * Description          : Tester la connexion avec tomcat
 * Remarque             : -
 * Sources :
 * https://www.baeldung.com/spring-boot-testresttemplate
 */

package com.amt.dflipflop;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestIntegrationContextSpringEmbeddedTomcat {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    /**
     * Test that the server tomcat works
     */
    @Test
    void goodTest(){
        ResponseEntity<String> response = restTemplate.
                getForEntity("http://localhost:" + port + "/", String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }


    /**
     * Test that a request to a non-existent page returns a 4040 error
     */
    @Test
    void badTest(){
        ResponseEntity<String> response = restTemplate.
                getForEntity("http://localhost:" + port + "/unknown", String.class);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
