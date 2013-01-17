/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.exceptions;

/**
 *
 * @author Maxime
 */
public class ErrorCreatingThreadException extends NetworkException {

    public ErrorCreatingThreadException()
    {
        System.err.println("Failed to create a thread.");
    }
    
}
