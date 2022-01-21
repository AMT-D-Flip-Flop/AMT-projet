/**
 * Date de création     : 06.12.2021
 * Dernier contributeur : Ryan Sauge
 * Groupe               : AMT-D-Flip-Flop
 * Description          : Serialiser la réponse du serveur d'authentification
 */

package com.amt.dflipflop.Entities.authentification;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

public class UserJson implements Serializable {

    @Getter @Setter private String token;
    @Getter @Setter private String username;
    @Getter @Setter private Account account;
    @Getter @Setter private String password;
    @Getter @Setter private List<String> errors;

    //Create count
    private int id;

    //Create role
    private String role;

    public void setAccountPublic(int id, String username, String role) {setAccount(new Account(id, username, role));
    }
}
