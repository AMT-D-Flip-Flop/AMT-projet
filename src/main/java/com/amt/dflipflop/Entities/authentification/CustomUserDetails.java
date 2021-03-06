/**
 * Date de création     : 06.12.2021
 * Groupe               : AMT-D-Flip-Flop
 * Description          : DAO for authenticated user
 * Remarque             : -
 * Sources :
 * https://www.codejava.net/frameworks/spring-boot/user-registration-and-login-tutorial
 */


package com.amt.dflipflop.Entities.authentification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {


    private UserJson user;
    List<GrantedAuthority> authorities
            = new ArrayList<>();


    public CustomUserDetails(UserJson user) {
        this.user = user;
        if(user.getAccount() != null){
            authorities.add(new SimpleGrantedAuthority(user.getAccount().getRole()));
        }
    }

    public boolean userIsNull() {
        return user == null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
        //return user.getPassword();
    }

    public int getId() {
        return user.getAccount().getId();
    }

    public String getToken() {
        return user.getToken();
    }

    @Override
    public String getUsername() {
        return user.getAccount().getUsername();
    }

    public String getRole() { return user.getAccount().getRole();}

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
