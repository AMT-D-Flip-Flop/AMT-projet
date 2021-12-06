package com.amt.dflipflop.Controllers;

import com.amt.dflipflop.Entities.authentification.CustomAuthenticationProvider;
import com.amt.dflipflop.Entities.authentification.CustomUserDetails;
import com.amt.dflipflop.Entities.authentification.UserJson;
import com.amt.dflipflop.Services.CustomUserDetailsService;
import com.amt.dflipflop.Entities.authentification.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
public class UserController {
    private final CustomUserDetailsService cs = new CustomUserDetailsService();
    private CustomUserDetails authenticatedUser;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private AuthenticationManager authenticationManager = new AuthenticationManager() {
        CustomAuthenticationProvider cp = new CustomAuthenticationProvider();
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            return cp.authenticate(authentication);
        }
    };

    @GetMapping("/user/orders")
    public String getUserOrders(Model model) { return "orders"; }

    @GetMapping("/user/addresses")
    public String getAddressesPage(Model model) {
        return "addresses";
    }

    @GetMapping("/user/add-address")
    public String getAddAddressPage(Model model) {
        return "add-address";
    }

    @PostMapping(path="/user/add-address") // Map ONLY POST Requests
    public @ResponseBody
    String addNewAddress () {
        return "add-address";
    }


    @GetMapping("/login")
    public String login(Model model) {
        //model.addAttribute("name", name);
        model.addAttribute("user", new User());
        return "authentification/signin_form";

    }

    @Value("${serverAuthentication.login}")
    private String serverAuthentication;

    @Value("${serverAuthentication.register}")
    private String serverAuthenticationRegister;

    @PostMapping("/login")
    //@ResponseBody
    public String login(@RequestParam("username") String username, @RequestParam("password") String pwd,
                        Model model, HttpServletResponse response, HttpServletRequest req) {

        authenticatedUser = cs.signin(username,pwd, serverAuthentication);
        //https://www.baeldung.com/manually-set-user-authentication-spring-security
        //https://stackoverflow.com/questions/4664893/how-to-manually-set-an-authenticated-user-in-spring-security-springmvc
        UsernamePasswordAuthenticationToken authRequest
                = new UsernamePasswordAuthenticationToken(username, pwd);

        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(authRequest);
        SecurityContext securityContext = SecurityContextHolder.getContext();

        securityContext.setAuthentication(authentication);

        // Create a new session and add the security context.
        HttpSession session = req.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        session.setAttribute("id", authenticatedUser.getId());

                /*.orElseThrow(()->
                new HttpServerErrorException(HttpStatus.FORBIDDEN, "Login Failed"));*/
       // model.addAttribute("user", u);
        Cookie cookie = new Cookie("bearer",this.authenticatedUser.getToken());

        // expires in 7 days
        cookie.setMaxAge(7 * 24 * 60 * 60);

        // optional properties
        cookie.setSecure(true);
        //cookie.setHttpOnly(true);
        cookie.setPath("/");

        // add cookie to response
        response.addCookie(cookie);

        // return response entity
       // return new ResponseEntity<>(this.authenticatedUser.getToken(), HttpStatus.OK);

//add cookie to response
        /*
        Accéder réponse http
        création cookie jwt
        return une page web
         */
        //return  "authentification/test";
        return "redirect:/";
        //return new ResponseEntity<>(this.authenticatedUser.getToken(), HttpStatus.OK);

    }

           /* User e = result.getBody();
            System.out.println("(Client Side) Employee Created: "+ e.getUsername());


    }*/



    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "authentification/signup_form";
    }

    @GetMapping("/register_success")
    public String showRegisterSuccessForm() {
        return "authentification/register_success";
    }

    /*
    https://www.codejava.net/frameworks/spring-boot/user-registration-and-login-tutorial
    https://devstory.net/11647/spring-boot-restful-client-avec-resttemplate#a13889219
    https://www.baeldung.com/spring-resttemplate-post-json
     */
    @PostMapping("/process_register")
    public String processRegister(User user) {
        /*
        */
        //
        //String createPersonUrl = "http://mobile.iict.ch/api/json";");
        String createPersonUrl = "http://localhost:3000/accounts/register";

        RestTemplate restTemplate = new RestTemplate();

        // Data attached to the request.
        HttpEntity<User> requestBody = new HttpEntity<>(user);

        // Send request with POST method.
        ResponseEntity<User> result
                = restTemplate.postForEntity(createPersonUrl, requestBody, User.class);

        User u = result.getBody();

        System.out.println("Status code:" + result.getStatusCode());

        // Code = 200.
        if (result.getStatusCode() == HttpStatus.OK) {
           /* User e = result.getBody();
            */
            return "authentification/register_success";
            //return "redirect:register_success";
        }else{
            return "authentification/signup_form";
            //return "signup_form";
        }
    }
}
