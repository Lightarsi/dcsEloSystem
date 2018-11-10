/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elosystem;

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

    /**
     * Lock external file to avoid simultaneous access from several threads or
     * apps.
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
            try (FileChannel channel = new RandomAccessFile(file, "rw").getChannel()) {
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
