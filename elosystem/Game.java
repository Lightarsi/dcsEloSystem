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
import java.util.Date;

/**
 *
 * @author diivanov
 */
public class Game implements Serializable {

    private final Pair<Integer, Integer> SCORE;
    private final Player PLAYER1;
    private final Player PLAYER2;
    private final Date gameDate;
    private final boolean isOfficial;
    private final Player WINNER;

    public Game(Player player1, Player player2, Pair<Integer, Integer> score, boolean official) {
        this.PLAYER1 = player1;
        this.PLAYER2 = player2;
        this.SCORE = score;
        this.gameDate = new Date();
        this.isOfficial = official;
        this.WINNER = explainResult();
    }

    /**
     * Method to understand who of players won the game.
     * @return 
     */
    private Player explainResult() {
        Integer firstScore = SCORE.getFirstObject();
        Integer secondScore = SCORE.getSecondObject();
        if (firstScore > secondScore) {
            return PLAYER1;
        } else if (firstScore < secondScore) {
            return PLAYER2;
        } else {
            // Here could be draw
            throw new IllegalStateException("There is no draw in this game");
        }
    }

    /**
     * @return the SCORE
     */
    public Pair<Integer, Integer> getSCORE() {
        return SCORE;
    }

    /**
     * @return the PLAYER1
     */
    public Player getPLAYER1() {
        return PLAYER1;
    }

    /**
     * @return the PLAYER2
     */
    public Player getPLAYER2() {
        return PLAYER2;
    }

    /**
     * @return the gameDate
     */
    public Date getGameDate() {
        return gameDate;
    }

    /**
     * @return the isOfficial
     */
    public boolean isOfficial() {
        return isOfficial;
    }

    /**
     * @return the WINNER
     */
    public Player getWINNER() {
        return WINNER;
    }

}
