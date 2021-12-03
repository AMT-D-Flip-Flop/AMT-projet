package com.amt.dflipflop.Services;

import com.amt.dflipflop.Entities.authentification.CustomUserDetails;
import com.amt.dflipflop.Entities.authentification.User;
import com.amt.dflipflop.Entities.authentification.UserJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;
import security.JwtProvider;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.security.core.userdetails.User.withUsername;

public class CustomUserDetailsService implements UserDetailsService {

    //@Autowired
    //private UserRepository userRepo;
    /*@Value("${serverAuthentication.login}")
    private String serverAuthentication;*/

    JwtProvider jwtProvider;

    @Autowired
    private AuthenticationManager authenticationManager;



   // @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);*/
       throw new UsernameNotFoundException("User not found");
    }

    /**
     * Extract username and roles from a validated jwt string.
     *
     * @param jwtToken jwt string
     * @return UserDetails if valid, Empty otherwise
     */
    public Optional<UserDetails> loadUserByJwtToken(String jwtToken) {
        if (jwtProvider.isValidToken(jwtToken)) {
            return Optional.of(
                    withUsername(jwtProvider.getUsername(jwtToken))
                            .authorities(jwtProvider.getRoles(jwtToken))
                            .password("") //token does not have password but field may not be empty
                            .accountExpired(false)
                            .accountLocked(false)
                            .credentialsExpired(false)
                            .disabled(false)
                            .build());
        }
        return Optional.empty();
    }

    /**
     * Sign in a user into the application, with JWT-enabled authentication
     *
     * @param username  username
     * @param password  password
     * @return Optional of the Java Web Token, empty otherwise
     */

    public CustomUserDetails signin(String username, String password, String serverAuthentication) {


        //LOGGER.info("New user attempting to sign in");
        Optional<String> token = Optional.empty();
       /* Optional<User> user = userRepo.findByUsername(username);
        if (user.isPresent()) {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                token = Optional.of(jwtProvider.createToken(username, user.get().getRoles()));
            } catch (AuthenticationException e){
                LOGGER.info("Log in failed for user {}", username);
            }
        }*/
        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));


       // String createPersonUrl = "http://localhost:3000/auth/login";
        String createPersonUrl = serverAuthentication;
        RestTemplate restTemplate = new RestTemplate();
        UserJson user = new UserJson();
        user.setPassword(password);
        user.setUsername(username);
        // Data attached to the request.
        HttpEntity<UserJson> requestBody = new HttpEntity<>(user, headers);

        // Send request with POST method.
        ResponseEntity<UserJson> result
                = restTemplate.postForEntity(createPersonUrl, requestBody, UserJson.class);

        System.out.println("Status code:" + result.getStatusCode());
        UserJson t = result.getBody();
        // Code = 200.
        if (result.getStatusCode() == HttpStatus.OK) {
            CustomUserDetails cs = new CustomUserDetails(user);
            User u = new User();
            u.setUsername(username);
            u.setToken(t.getToken());
            //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            // Optional.of(jwtProvider.createToken(username, user.get().getRoles()));
            token = Optional.ofNullable(user.getToken());
            return cs;
        }else{
            throw new UsernameNotFoundException("User not found");
        }

    }


}