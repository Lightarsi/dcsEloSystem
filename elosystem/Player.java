/*
 * Copyright (C) 2018 Lightars
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
        //Log.log("R1: " + String.valueOf(R1));
        //Log.log("R2: " + String.valueOf(R2));
        double expectedValue = 1.0 / (1.0 + Math.pow(10.0, (double) (R2 - R1) / 400.0));
        //Log.log(String.valueOf(expectedValue));
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
