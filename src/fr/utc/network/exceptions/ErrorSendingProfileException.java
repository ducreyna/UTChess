/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.exceptions;

/**
 *
 * @author Maxime
 */
public class ErrorSendingProfileException extends NetworkException {

    public ErrorSendingProfileException()
    {
        System.err.println("Failed to send the profile to the network.");
    }
    
}
