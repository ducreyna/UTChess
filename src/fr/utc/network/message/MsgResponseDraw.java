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
public class MsgResponseDraw extends MessageNetwork {
    /**
     * Draw request response
     */
    boolean _response;
    
    /**
     * Instantiates an instance of type MsgResponseDraw
     * @param response 
     */
    public MsgResponseDraw(boolean response) {
        _response = response;
    }

    @Override
    public void traiterMessage() {
        /**
         * Receive the response to the draw request 
         */
        GridInterface UIInterface = NetworkImplementation.getIHMGrilleInterface();
        UIInterface.receiveDrawResponse(this._response);
    }

    @Override
    public void reportError(Exception e)
    {
        String message = "Erreur, envoi de la réponse à la demande de match nul : " + e.getMessage();
        NetworkImplementation.getIHMGrilleInterface().receiveError(message);
    }
    
}
