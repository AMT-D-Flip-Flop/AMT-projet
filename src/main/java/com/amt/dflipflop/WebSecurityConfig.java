package com.amt.dflipflop;


import com.amt.dflipflop.Services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import security.TokenAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {



    private CustomUserDetailsService userDetailsService  = new CustomUserDetailsService();

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()// Disable csrf for now
                .authorizeRequests()
                    .antMatchers("/store/add-product", "/store/manage-product/*", "/categories").hasRole("ADMIN")
                    .antMatchers("/**", "/css/*", "/js/*", "/images/*", "/demo/*").permitAll()
                    .and()
                    .addFilterBefore(new TokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .logout()
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .deleteCookies("bearer");


        // No session will be created or used by spring security
        //??DIsuc
        //http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }



    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
