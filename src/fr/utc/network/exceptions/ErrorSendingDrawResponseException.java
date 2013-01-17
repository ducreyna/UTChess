/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.exceptions;

/**
 *
 * @author Maxime
 */
public class ErrorSendingDrawResponseException extends NetworkException {

    public ErrorSendingDrawResponseException()
    {
        System.err.println("Failed to send the draw request response.");
    }
    
}
