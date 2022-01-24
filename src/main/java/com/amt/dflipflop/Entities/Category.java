/*
 Date de création : 01.11.2021
 Groupe : AMT-D-Flip-Flop
 Description :
 Remarques :
*/

package com.amt.dflipflop.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity @NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Getter @Setter
    private Integer id;

    @Getter @Setter
    private String name;


    @Override
    public String toString() {
        return name;
    }
}
