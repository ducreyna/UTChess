package fr.utc.network.message;

import fr.utc.network.services.NetworkImplementation;
import java.util.UUID;

/**
 *
 * @author Tarik Bensalah
 */
public class MsgRequestNewGame extends MessageNetwork {
    
    private String _idOpponent;
    private boolean _color;
    private int _timer;
    private UUID _id_game;
    
    /**
     * 
     * @param idOpponent : The opponent ID 
     * @param color : The opponent's pawns color
     * @param timer : Value of the timer during the game
     * @throws IllegalArgumentException : if any arguments is incorrect
     */
    public MsgRequestNewGame(String idOpponent,boolean color,int timer, UUID id_game)throws IllegalArgumentException
    {
        if(idOpponent ==  null || idOpponent.trim().length() == 0)
        {
            throw new IllegalArgumentException("idOpponent");
        }
        
        /*if(color < 0 || color > 0)
        {
            throw new IllegalArgumentException("color < 0 or color > 1");
        }*/
        
        if(timer < -1)
        {
            throw new IllegalArgumentException("timer < -1");
        }

        this._color = color;
        this._idOpponent = idOpponent;
        this._timer = timer;
        this._id_game = id_game;
    }
    
    @Override
    public void traiterMessage() {
        /*
         * Processes the arrival of a demand for a new game
         */
	NetworkImplementation.getIHMConnexionInterface().newGameEvent(this._idOpponent, this._color, this._timer, this._id_game);
    }

    @Override
    public void reportError(Exception e)
    {
        String message = "Erreur, envoi de la demande de nouvelle partie : " + e.getMessage();
        NetworkImplementation.getIHMConnexionInterface().receiveErrorSendingMessage(message);
    }
}
