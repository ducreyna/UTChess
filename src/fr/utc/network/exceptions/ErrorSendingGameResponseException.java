/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.exceptions;

/**
 *
 * @author Maxime
 */
public class ErrorSendingGameResponseException extends NetworkException {

    public ErrorSendingGameResponseException()
    {
        System.err.println("Failed to send the answer (game response) to the opponent");
    }
    
}
