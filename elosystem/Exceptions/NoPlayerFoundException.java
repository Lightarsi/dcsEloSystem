/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elosystem.Exceptions;

/**
 *
 * @author Lightars
 */
public class NoPlayerFoundException extends Exception {

    public NoPlayerFoundException() {
        super();
    }

    public NoPlayerFoundException(String message) {
        super(message);
    }

    public NoPlayerFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPlayerFoundException(Throwable cause) {
        super(cause);
    }
}
