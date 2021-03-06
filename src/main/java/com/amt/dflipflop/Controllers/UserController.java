package com.amt.dflipflop.Controllers;

import com.amt.dflipflop.Entities.authentification.CustomAuthenticationProvider;
import com.amt.dflipflop.Entities.authentification.CustomUserDetails;
import com.amt.dflipflop.Services.CustomUserDetailsService;
import com.amt.dflipflop.Entities.authentification.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.amt.dflipflop.Constants.*;


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
    public String getUserOrders(Model model) {
        return "orders";
    }

    @GetMapping("/user/addresses")
    public String getAddressesPage(Model model) {
        return "addresses";
    }

    @GetMapping("/user/add-address")
    public String getAddAddressPage(Model model) {
        return "add-address";
    }

    @PostMapping(path = "/user/add-address") // Map ONLY POST Requests
    public @ResponseBody
    String addNewAddress() {
        return "add-address";
    }

    @Value("${devServerAuthentication.login}")
    private String devServerAuthentication;

    @Value("${devServerAuthentication.register}")
    private String devServerAuthenticationRegister;

    @Value("${prodServerAuthentication.login}")
    private String prodServerAuthentication;

    @Value("${prodServerAuthentication.register}")
    private String prodServerAuthenticationRegister;

    /*
    Source :
     //https://www.baeldung.com/manually-set-user-authentication-spring-security
    //https://stackoverflow.com/questions/4664893/how-to-manually-set-an-authenticated-user-in-spring-security-springmvc
     */
    @PostMapping("/login")
    //@ResponseBody
    public String login(User user, HttpServletResponse response, HttpServletRequest req, RedirectAttributes redirectAttrs) {
        try {
            authenticatedUser = cs.signin(user.getUsername(), user.getPassword(), IS_PROD ?  prodServerAuthentication : devServerAuthentication);
            if (authenticatedUser.userIsNull()) {
                return "authentification/signin_form";
            }
            UsernamePasswordAuthenticationToken authRequest
                    = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(),
                    authenticatedUser.getAuthorities());

            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(authRequest);
            SecurityContext securityContext = SecurityContextHolder.getContext();

            securityContext.setAuthentication(authentication);

            // Create a new session and add the security context.
            HttpSession session = req.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
            session.setAttribute("id", authenticatedUser.getId());
            session.setAttribute("user", authenticatedUser);

            Cookie cookie = new Cookie("bearer", this.authenticatedUser.getToken());

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
        } catch (CustomUserDetailsService.AuthManagerException e) {
            redirectAttrs.addFlashAttribute(ERROR_MSG_KEY, e.errors);
            return "redirect:/login";
        } catch (Exception e) {
            return "redirect:/login";
        }

        return "redirect:/";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "authentification/signup_form";
    }

    @GetMapping("/login")
    public String login(Model model, RedirectAttributes redirectAttrs) {
        //model.addAttribute("name", name);
        model.addAttribute("user", new User());
        return "authentification/signin_form";

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
    public String processRegister(User user, RedirectAttributes redirectAttrs) {
        try {
            CustomUserDetails register = cs.signup(user.getUsername(), user.getPassword(), IS_PROD ? prodServerAuthenticationRegister : devServerAuthenticationRegister);
            return "redirect:/login";
        } catch (CustomUserDetailsService.AuthManagerException e) {
            redirectAttrs.addFlashAttribute(ERROR_MSG_KEY, e.errors);
            return "redirect:/register";
        } catch (Exception e) { //OK
            System.out.println(e.toString());
            return "redirect:/register";
        }
    }

    @GetMapping("/profile")
    public String profilePage(Model model) {
        if (authenticatedUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", authenticatedUser);

        return "authentification/profile";
    }
}
