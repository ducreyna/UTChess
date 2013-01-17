/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.network.message;

import fr.utc.data.historique.Message;
import fr.utc.grid.GridInterface;
import fr.utc.network.services.NetworkImplementation;

/**
 *
 * @author lo23a021
 */
public class MsgChatMessage extends MessageNetwork {

    private Message _message;

    public MsgChatMessage(Message message) {
        _message = message;
    }

    @Override
    public void traiterMessage()
    {
        GridInterface UIInterface = NetworkImplementation.getIHMGrilleInterface();
        UIInterface.receiveMessage(_message);
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reportError(Exception e)
    {
        NetworkImplementation.getIHMGrilleInterface().receiveError("Erreur, message de chat : " + e.getMessage());
    }

}
