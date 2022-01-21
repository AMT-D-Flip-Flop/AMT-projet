package com.amt.dflipflop.Entities.authentification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import java.io.Serializable;

@NoArgsConstructor
public class Account implements Serializable {

    @Setter private int id;
    @Getter @Setter private String username;
    @Getter @Setter  private String role;

    public Account(int id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    @Id
    public int getId() {
        return id;
    }

}
