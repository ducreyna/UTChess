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
public class MsgResponseSaveGame extends MessageNetwork {
    private boolean _response;
    /**
     * Create a message to inform of the response of a save game request
     * @param response Whether the save game request was accepted or not by the opponent
     */
    public MsgResponseSaveGame(boolean response)
    {
        _response = response;
    }

    /**
     * Tell IHMGrille our opponent's response of our request to save game
     */
    public void traiterMessage() {
	NetworkImplementation.getIHMGrilleInterface().receiveSaveResponse(_response);
    }

    @Override
    public void reportError(Exception e)
    {
        String message = "Erreur, envoi de la réponse à la demande de sauvegarde : " + e.getMessage();
        NetworkImplementation.getIHMGrilleInterface().receiveError(message);
    }
    
}
