/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.message;

import fr.utc.grid.GridInterface;
import fr.utc.network.services.NetworkImplementation;

/**
 *
 * @author Frédéric Jauffrit
 */
public class MsgRequestDraw extends MessageNetwork {
    
    @Override
    public void traiterMessage() {
        /**
         * Receive a draw request.
         */
        GridInterface UIInterface = NetworkImplementation.getIHMGrilleInterface();
        UIInterface.receiveDraw();
    }

    @Override
    public void reportError(Exception e)
    {
        String message = "Erreur, envoi de la demande de match nul : " + e.getMessage();
        NetworkImplementation.getIHMGrilleInterface().receiveError(message);
    }
    
}
