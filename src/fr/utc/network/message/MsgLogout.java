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
public class MsgLogout extends MessageNetwork {
    
    private IpProfile _ipProfile;
    
    /**
     * Instantiates an instance of type MsgLogout
     * @param ipProfile of the player wanting to leave the game
     */
    public MsgLogout(IpProfile ipProfile)
    {
        this._ipProfile = ipProfile;
    }
    
    @Override
    public void traiterMessage() {
	if(! this._ipProfile.getIp().equals(NetworkImplementation.getMyIpProfile().getIp()))
        {
            NetworkImplementation.getListOfProfiles().remPlayer(this._ipProfile);
        }
    }

    @Override
    public void reportError(Exception e)
    {
        String message = "Erreur, message de déconnexion : " + e.getMessage();
        NetworkImplementation.getIHMConnexionInterface().receiveErrorSendingMessage(message);
    }
}
