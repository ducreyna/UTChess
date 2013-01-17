/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.message;

import fr.utc.data.historique.Move;
import fr.utc.grid.GridInterface;
import fr.utc.network.services.NetworkImplementation;

/**
 *
 * @author Robi
 */
public class MsgMove extends MessageNetwork {
    
    private Move _move;
    
    public MsgMove(Move move) {
        _move = move;
    }
    
    
    @Override
    public void traiterMessage() {
        GridInterface UIInterface = NetworkImplementation.getIHMGrilleInterface();
        UIInterface.receiveMove(_move);
	//throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reportError(Exception e)
    {
        String message = "Erreur, envoi du déplacement : " + e.getMessage();
        NetworkImplementation.getIHMGrilleInterface().receiveError(message);
    }
    
}
