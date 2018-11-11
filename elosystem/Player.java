/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elosystem;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author diivanov
 */
public class Player implements Serializable {

    private final String nick;
    private final String name;
    private final String surname;

    private int officialRating = 1500;
    private int tempOfficialRating = 0;
    private int hidedRating = 1500;
    private int tempHidedRating = 0;

    private final ArrayList<Game> listOfPlayedGames = new ArrayList<>();

    public Player(String nick, String name, String surname) {
        this.nick = nick;
        this.name = name;
        this.surname = surname;
    }

    public void addGame(Game game) {
        listOfPlayedGames.add(game);
        changeRating(game);
    }

    private void changeRating(Game game) {
        // possible mistake: you should use applyResults before trying next game.
        if((tempOfficialRating != 0) || (tempHidedRating != 0)) {
            throw new IllegalStateException("Results of previous game wasn't applied.");
        }
        double result = getResult(game);
        if(game.isOfficial()) {
            tempOfficialRating = officialRating
                    + (int) Math.round( this.getKFactor() * (result - getExpectedValueForGame(game)) );
        } else {
            tempHidedRating = hidedRating
                    + (int) Math.round( this.getKFactor() * (result - getExpectedValueForGame(game)) );
        }
    }
    
    public void applyResults() {
        officialRating = tempOfficialRating;
        tempOfficialRating = 0;
        hidedRating = tempHidedRating;
        tempHidedRating = 0;
    }

    private double getExpectedValueForGame(Game game) {
        Player anotherPlayer = getAnotherPlayerInGame(game);
        int R1;
        int R2;
        if (game.isOfficial()) {
            R1 = this.getOfficialRating();
            R2 = anotherPlayer.getOfficialRating();
        } else {
            R1 = this.getHidedRating();
            R2 = anotherPlayer.getHidedRating();
        }
        double expectedValue = 1 / (1 + Math.pow(10, (R2 - R1) / 400));
        return expectedValue;
    }

    private Player getAnotherPlayerInGame(Game game) {
        Player player1 = game.getPLAYER1();
        Player player2 = game.getPLAYER2();
        if (this == player1) {
            return player2;
        } else {
            return player1;
        }
    }

    /**
     * Result is 1 for win, 0 for lose and possible 0.5 for draw (not
     * implemented).
     *
     * @param game
     * @return
     */
    private double getResult(Game game) {
        if (this == game.getWINNER()) {
            return 1;
        } else {
            // no draw in this game
            return 0;
        }
    }

    private int getKFactor() {
        if (getGamesTotal() < 11) {
            return 40;
        } else if (getOfficialRating() <= 2400) {
            return 20;
        } else if (getOfficialRating() > 2400) {
            return 10;
        } else {
            throw new IllegalStateException("That shouldn't be like that.");
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @return the officialRating
     */
    public int getOfficialRating() {
        return officialRating;
    }

    /**
     * @return the hidedRating
     */
    public int getHidedRating() {
        return hidedRating;
    }

    /**
     * @return the gamesTotal
     */
    public int getGamesTotal() {
        return listOfPlayedGames.size();
    }
}
