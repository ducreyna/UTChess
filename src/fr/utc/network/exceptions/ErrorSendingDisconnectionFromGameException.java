/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.exceptions;

/**
 *
 * @author Maxime
 */
public class ErrorSendingDisconnectionFromGameException extends NetworkException {

    public ErrorSendingDisconnectionFromGameException()
    {
        System.err.println("Failed to notify your opponent of your disconnection");
    }
    
}
