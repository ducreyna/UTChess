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
public class MsgDisconnectionFromGame extends MessageNetwork {

    /**
     * Disconnection from the current game.
     */
    @Override
    public void traiterMessage() {
        // Sends the information to IHMGrille.
	NetworkImplementation.getIHMGrilleInterface().receiveDisconnectionOpponent();
    }

    @Override
    public void reportError(Exception e)
    {   
        String message = "Erreur, message de déconnexion de la partie : " + e.getMessage();
        NetworkImplementation.getIHMGrilleInterface().receiveError(message);
    }
}
