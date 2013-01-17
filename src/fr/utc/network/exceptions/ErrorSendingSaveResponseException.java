/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.exceptions;

/**
 *
 * @author Maxime
 */
public class ErrorSendingSaveResponseException extends NetworkException {

    public ErrorSendingSaveResponseException()
    {
        System.err.println("Failed to send the answer (Save Response) to the opponent");
    }
    
}
