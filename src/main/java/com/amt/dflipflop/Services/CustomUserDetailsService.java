package com.amt.dflipflop.Services;

import com.amt.dflipflop.Entities.authentification.CustomUserDetails;
import com.amt.dflipflop.Entities.authentification.UserJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    private AuthenticationManager authenticationManager;

    public class AuthManagerException extends Exception {
        public List<String> errors;

        public AuthManagerException(List<String> errors) {
            super();
            this.errors = errors;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("User not found");
    }

    /**
     * Sign in a user into the application, with JWT-enabled authentication
     *
     * @param username username
     * @param password password
     * @return Optional of the Java Web Token, empty otherwise
     */

    public CustomUserDetails signin(String username, String password, String serverAuthentication) throws AuthManagerException {
        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        RestTemplate restTemplate = new RestTemplate();
        UserJson user = new UserJson();
        user.setPassword(password);
        user.setUsername(username);

        // Data attached to the request.
        HttpEntity<UserJson> requestBody = new HttpEntity<>(user, headers);

        // Send request with POST method.
        ResponseEntity<UserJson> result = null;
        try {
            result = restTemplate.postForEntity(serverAuthentication, requestBody, UserJson.class);
        } catch (Exception e) {
            throw e;
        }

        UserJson userJsonResponse = result.getBody();

        // Error
        if (userJsonResponse == null) {
            List<String> errors = new ArrayList<>();
            errors.add("Null Response");
            throw new AuthManagerException(errors);
        }
        if (userJsonResponse.getErrors().size() > 0) {
            throw new AuthManagerException(userJsonResponse.getErrors());
        }
        if (result.getStatusCode() == HttpStatus.OK) {
            CustomUserDetails cs = new CustomUserDetails(userJsonResponse);
            return cs;
        } else {
            List<String> errors = new ArrayList<>();
            errors.add("Request failed");
            throw new AuthManagerException(errors);
        }

    }

    public CustomUserDetails signup(String username, String password, String serverRegister) throws AuthManagerException {
        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        RestTemplate restTemplate = new RestTemplate();
        UserJson user = new UserJson();
        user.setPassword(password);
        user.setUsername(username);

        // Data attached to the request.
        HttpEntity<UserJson> requestBody = new HttpEntity<>(user, headers);

        // Send request with POST method.
        ResponseEntity<UserJson> result = null;
        try {
            result = restTemplate.postForEntity(serverRegister, requestBody, UserJson.class);
        } catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            throw new AuthManagerException(errors);
        }

        UserJson userJsonResponse = result.getBody();

        if (userJsonResponse == null) {
            List<String> errors = new ArrayList<>();
            errors.add("Null Response");
            throw new AuthManagerException(errors);
        }
        if (userJsonResponse.getErrors().size() > 0) {
            throw new AuthManagerException(userJsonResponse.getErrors());
        }
        if (result.getStatusCode() == HttpStatus.CREATED) {
            CustomUserDetails cs = new CustomUserDetails(userJsonResponse);
            return cs;
        } else {
            List<String> errors = new ArrayList<>();
            errors.add("Request Failed");
            throw new AuthManagerException(errors);
        }
    }


}
