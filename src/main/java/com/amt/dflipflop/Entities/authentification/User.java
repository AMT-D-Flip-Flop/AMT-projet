/**
 * Date de création     : 06.12.2021
 * Dernier contributeur : Ryan Sauge
 * Groupe               : AMT-D-Flip-Flop
 * Description          : représente un utilisateur
 */

package com.amt.dflipflop.Entities.authentification;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String email;


    private String firstName;

    private String lastName;

    private String token;
    private String role;
    private String password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    //Role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    //firstname
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    //lastname
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    //password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    //ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //username
    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }


    //Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
