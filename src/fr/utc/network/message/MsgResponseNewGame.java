/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.message;

import fr.utc.network.profile.IpProfile;
import fr.utc.network.services.NetworkImplementation;

/**
 *
 * @author Tarik Bensalah
 */
public class MsgResponseNewGame extends MessageNetwork {
    
    /**
     * ID of the player who send the response
     */
    private String _idOpponent;
    private String _idOpponentMe;
    
    /**
     * Value of the response, True to accept and false atherwise.
     */
    private boolean _response;
    
    /**
     * Instantiates an instance of type MsgResponseNewGame
     * @param idOpponent : ID of the player who send the response
     * @param response : Value of the response, True to accept and false atherwise.
     */
    public MsgResponseNewGame(String idOpponent, boolean response)throws IllegalArgumentException
    {
        if(idOpponent ==  null || idOpponent.trim().length() == 0)
        {
            throw new IllegalArgumentException("idOpponent");
        }
        this._idOpponentMe = NetworkImplementation.getMyIpProfile().getProfile().getId();
        this._idOpponent = idOpponent;
        this._response = response;
        System.out.println("Mon ID : "+_idOpponentMe);
        System.out.println("Son ID : "+_idOpponent);

        setDistantProfile(_idOpponent); // we start the game, we set the opponent profile

    }

    private void setDistantProfile(String id)
    {
        // If the new game was accepted
        if (_response == true)
        {
            // Then we set the distantipprofile corresponding to the idopponent
            for(IpProfile ipProfile : NetworkImplementation.getListOfUsers().getPlayerList())
            {
                if(ipProfile.getProfile().getId().equals(id))
                {
                    NetworkImplementation.setDistantPlayerProfile(ipProfile);
                }
            }
        }
    }

    @Override
    public void traiterMessage() {
        /**
         * Processes the arrival of a response of a new game request
         */

        setDistantProfile(_idOpponentMe); // the game was started, we set the opponent profile
	NetworkImplementation.getIHMConnexionInterface().receiveResponse(this._idOpponentMe, this._response);

        System.out.println("Mon ID : "+_idOpponent);
        System.out.println("Son ID : "+_idOpponentMe);
    }

    @Override
    public void reportError(Exception e)
    {
        String message = "Erreur, envoi de la réponse à la demande de nouvelle partie : " + e.getMessage();
        NetworkImplementation.getIHMConnexionInterface().receiveErrorSendingMessage(message);
    }
}
