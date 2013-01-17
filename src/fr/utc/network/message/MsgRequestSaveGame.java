/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.message;

import fr.utc.network.services.NetworkImplementation;

/**
 *
 * @author Tarik Bensalah
 */
public class MsgRequestSaveGame extends MessageNetwork {
    @Override
    public void traiterMessage() {
        /**
         * Receive a save request
         */
        NetworkImplementation.getIHMGrilleInterface().receiveSaveRequest();
    }

    @Override
    public void reportError(Exception e)
    {
        String message = "Erreur, envoi de la demande de sauvegarde : " + e.getMessage();
        NetworkImplementation.getIHMGrilleInterface().receiveError(message);
    }
    
}
