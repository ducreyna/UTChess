/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.exceptions;

/**
 *
 * @author Maxime
 */
public class ErrorSendingAbandonException extends NetworkException {

    public ErrorSendingAbandonException()
    {
        System.err.println("Failed to prevent the opponent for your abandon.");
    }
    
}
