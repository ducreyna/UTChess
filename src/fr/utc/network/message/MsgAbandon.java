/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.message;

import fr.utc.network.services.NetworkImplementation;

/**
 *
 * @author Robin B
 */
public class MsgAbandon extends MessageNetwork {

    /**
     * Our opponent abandoned the game.
     */
    @Override
    public void traiterMessage() {
        // Sends the information to IHMGrille.
	NetworkImplementation.getIHMGrilleInterface().receiveAbandon();
    }

    @Override
    public void reportError(Exception e)
    {
        NetworkImplementation.getIHMGrilleInterface().receiveError("Erreur, message d'abandon : " + e.getMessage());
    }
}
