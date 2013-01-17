/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.exceptions;

/**
 *
 * @author Maxime
 */
public class ErrorAskingNewGameException extends NetworkException {

    public ErrorAskingNewGameException()
    {
        System.err.println("Failed to ask the opponent to play a new game");
    }
    
}
