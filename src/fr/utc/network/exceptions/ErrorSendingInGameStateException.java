/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.exceptions;

/**
 *
 * @author Maxime
 */
public class ErrorSendingInGameStateException extends NetworkException {

    public ErrorSendingInGameStateException()
    {
        System.err.println("Failed to send the InGame state");
    }
    
}
