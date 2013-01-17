/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.exceptions;

/**
 *
 * @author Maxime
 */
public class ErrorSendingMoveException extends NetworkException {

    public ErrorSendingMoveException()
    {
        System.err.println("Failed to send the move information to the opponent.");
    }
    
}
