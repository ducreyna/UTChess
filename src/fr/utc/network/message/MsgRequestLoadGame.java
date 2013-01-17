/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.message;

import fr.utc.network.services.NetworkImplementation;
import java.util.UUID;

/**
 *
 * @author Tarik Bensalah
 */
public class MsgRequestLoadGame extends MessageNetwork {
    
     /**
     * ID of the player who send the response
     */
    private String _idOpponent;
    
    /**
     * ID of the game to load
     */
    private UUID _idGame;
    
    /**
     * Instanciates an instance of type MsgRequestLoadGame
     * @param idOpponent : ID of the player who send the response
     * @param idGame : ID of the game to load
     */
    public MsgRequestLoadGame(String idOpponent, UUID idGame)
    {
        if(idOpponent ==  null || idOpponent.trim().length() == 0)
        {
            throw new IllegalArgumentException("idOpponent");
        }
        
        if(idGame ==  null)
        {
            throw new IllegalArgumentException("idGame");
        }
        
        this._idGame = idGame;
        this._idOpponent =idOpponent;
    }
    
    @Override
    public void traiterMessage() {
        /**
         * Processes the arrival of a load game request
         */
	NetworkImplementation.getIHMConnexionInterface().loadGameEvent(this._idOpponent,this._idGame);
    }

    @Override
    public void reportError(Exception e)
    {
        String message = "Erreur, envoi de la demande de chargement : " + e.getMessage();
        NetworkImplementation.getIHMConnexionInterface().receiveErrorSendingMessage(message);
    }
    
}
