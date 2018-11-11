/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
