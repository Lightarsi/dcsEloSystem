/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elosystem;

import java.io.Serializable;

/**
 *
 * @author diivanov
 */
public class Player implements Serializable {

    private final String nick;
    private final String name;
    private final String surname;

    private int officialRating = 1500;
    private int hidedRating = 1500;
    private int gamesTotal = 0;

    public Player(String nick, String name, String surname) {
        this.nick = nick;
        this.name = name;
        this.surname = surname;
    }

    private int getKFactor() {
        if (gamesTotal < 11) {
            return 40;
        } else if (officialRating <= 2400) {
            return 20;
        } else if (officialRating > 2400) {
            return 10;
        } else {
            throw new IllegalStateException("That shouldn't be like that.");
        }
    }
}
