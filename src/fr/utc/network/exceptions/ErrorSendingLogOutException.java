/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.exceptions;

/**
 *
 * @author Maxime
 */
public class ErrorSendingLogOutException extends NetworkException {

    public ErrorSendingLogOutException()
    {
        System.err.println("Failed to send logout information to the network.");
    }
    
}
