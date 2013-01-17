/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.exceptions;

/**
 *
 * @author Maxime
 */
public class ErrorSendingSaveRequestException extends NetworkException {

    public ErrorSendingSaveRequestException()
    {
        System.err.println("Failed to send the save request to the opponent.");
    }
    
}
