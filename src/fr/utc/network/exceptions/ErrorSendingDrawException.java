/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.exceptions;

/**
 *
 * @author Maxime
 */
public class ErrorSendingDrawException extends NetworkException {

    public ErrorSendingDrawException()
    {
        System.err.println("Failed to ask the opponent to draw");
    }
    
}
