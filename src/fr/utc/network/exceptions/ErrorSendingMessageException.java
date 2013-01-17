/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.exceptions;

/**
 *
 * @author Maxime
 */
public class ErrorSendingMessageException extends NetworkException {

    public ErrorSendingMessageException()
    {
        System.err.println("Failed to send the message.");
    }
    
}
