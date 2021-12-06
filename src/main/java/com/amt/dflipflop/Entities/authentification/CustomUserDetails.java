/**
 * Date de création     : 16.10.2021
 * Dernier contributeur : Ryan Sauge
 * Groupe               : AMT-D-Flip-Flop
 * Description          : Tester la connexion avec tomcat
 * Remarque             : -
 * Sources :
 * https://www.baeldung.com/spring-boot-testresttemplate
 */


package com.amt.dflipflop.Entities.authentification;

/*
Source : https://www.codejava.net/frameworks/spring-boot/user-registration-and-login-tutorial
 represent an authentication user
 */

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {



    private UserJson user;

    public CustomUserDetails( UserJson  user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return "";
       //return user.getPassword();
    }

    public int getId() {
        return user.getAccount().getId();
    }

    public String getToken(){
        return user.getToken();
    }
    @Override
    public String getUsername() {
        return user.getUsername();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getFullName() {
        //return user. + " " + user.getLastName();
        //TODO : set firstname
        return "";
    }

}
