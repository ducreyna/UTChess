/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.message;

import java.io.Serializable;

/**
 *
 * @author Robi
 */
public abstract class MessageNetwork implements Serializable {

    private static final long serialVersionUID = 1;
    /**
     * Permet par polymorphisme de lancer le traitement qui correspond à un des messages envoyé.
     */
    public abstract void traiterMessage();

    /*
     * Reports error to the UI
     * @param e : the exception raised.
     */
    public abstract void reportError(Exception e);
}
