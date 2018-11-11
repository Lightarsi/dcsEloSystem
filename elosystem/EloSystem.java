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

import elosystem.Exceptions.NoPlayerFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author diivanov
 */
public class EloSystem implements Serializable {

    public static EloSystem eloSystem;
    private FileLock lock;

    private final HashMap<String, Player> playersMap = new HashMap<>();
    private final ArrayList<Game> listOfGames = new ArrayList<>();

    private EloSystem() {

    }

    public static EloSystem getEloSystem() {
        if (eloSystem == null) {
            eloSystem = new EloSystem();
        }
        return eloSystem;
    }

    public void addGame(String player1Name, String player2Name, int scoreOfPlayer1,
            int scoreOfPlayer2, boolean official)
            throws NoPlayerFoundException {
        Player player1 = getPlayerByNick(player1Name);
        if (player1 == null) {
            throw new NoPlayerFoundException();
        }
        Player player2 = getPlayerByNick(player2Name);
        if (player1 == null) {
            throw new NoPlayerFoundException();
        }
        addGame(player1, player2, scoreOfPlayer1, scoreOfPlayer2, official);
    }

    public void addGame(Pair<String, String> player1NamePair, Pair<String, String> player2NamePair,
            int scoreOfPlayer1, int scoreOfPlayer2, boolean official)
            throws NoPlayerFoundException {
        Player player1 = getPlayer(player1NamePair);
        if (player1 == null) {
            throw new NoPlayerFoundException();
        }
        Player player2 = getPlayer(player2NamePair);
        if (player1 == null) {
            throw new NoPlayerFoundException();
        }
        addGame(player1, player2, scoreOfPlayer1, scoreOfPlayer2, official);
    }

    /**
     * Method to add this game to the list of games and to both players.
     * Each player process game by himself.
     *
     * @param player1
     * @param player2
     * @param scoreOfPlayer1
     * @param scoreOfPlayer2
     * @param official
     */
    private void addGame(Player player1, Player player2, int scoreOfPlayer1, int scoreOfPlayer2, boolean official) {
        Game game = new Game(player1, player2,
                new Pair<>(scoreOfPlayer1, scoreOfPlayer2),
                official);
        listOfGames.add(game);
        player1.addGame(game);
        player2.addGame(game);
        // New rating should be calculated referenced to ratings BEFORE game,
        // thats why results are applied after.
        player1.applyResults();
        player2.applyResults();
    }

    /**
     * Method to get player with nick.
     *
     * @param playerNick
     * @return
     */
    private Player getPlayerByNick(String playersNick) {
        return playersMap.get(playersNick);
    }

    /**
     * Method to get player by name and surname.
     *
     * @param playerName
     * @param playerSurname
     * @return
     */
    private Player getPlayer(Pair<String, String> playerNamePair) {
        return getPlayerByNameAndSurname(playerNamePair.getFirstObject(), playerNamePair.getSecondObject());
    }

    /**
     * Method to create player if noone was found.
     */
    private void createPlayer(String nick, String name, String surname) {
        Player player = new Player(nick, name, surname);
        playersMap.put(nick, player);
    }

    /**
     * Get player from playersMap. Returns null if player is not found.
     *
     * @param name
     * @param surname
     * @return
     */
    private Player getPlayerByNameAndSurname(String name, String surname) {
        Collection<Player> values = playersMap.values();
        for (Player player : values) {
            if ((player.getName().equals(name)) && (player.getSurname().equals(surname))) {
                return player;
            }
        }
        return null;
    }

    /**
     * Lock external file ( getEloSystemFile() ) to avoid simultaneous access
     * from several threads or apps. You must unlock file after action.
     *
     * @return
     */
    private boolean lockExternalFile() {
        if (lock != null) {
            Log.log("Lock is already used");
            return false;
        }
        try {
            File file = getEloSystemFile();
            try ( FileChannel channel = new RandomAccessFile(file, "rw").getChannel()) {
                try {
                    lock = channel.tryLock();
                } catch (OverlappingFileLockException e) {
                    return false;
                }
            }
        } catch (IOException e) {
            Log.log("There is no elo file");
            return false;
        }
        return true;
    }

    private boolean unlockExternalFile() {
        try {
            if (lock != null) {
                lock.release();
            }
            return true;
        } catch (IOException ex) {
            Log.log("There is no elo file");
            return false;
        }
    }

    /**
     * Method to serialize any object.
     */
    private void serializeObject(String pathWhereSerialize, Object objectToSerialize) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(pathWhereSerialize));
        out.writeObject(objectToSerialize);
    }

    /**
     * Method to deserialize any object. Use (objectType) object.
     */
    private Object deserializeObject(String pathFromDeserialize) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(pathFromDeserialize));
        return in.readObject();
    }

    public static File getEloSystemFile() {
        return new File("system.elo");
    }

}
