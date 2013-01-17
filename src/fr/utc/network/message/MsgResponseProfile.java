/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.message;

import fr.utc.network.profile.IpProfile;
import fr.utc.network.services.NetworkImplementation;

/**
 *
 * @author Robi
 */
public class MsgResponseProfile extends MessageNetwork {

    private IpProfile _profile;

    public MsgResponseProfile()
    {
        _profile = NetworkImplementation.getMyIpProfile();
        System.out.println("bla"+_profile.getIp());
    }



    @Override
    public void traiterMessage()
    {
        /*
         * updates the list of players with the received profile
         */
	NetworkImplementation.getListOfProfiles().addPlayer(_profile);
    }

    @Override
    public void reportError(Exception e)
    {
        String message = "Erreur, envoi du profil sur le réseau : " + e.getMessage();
        NetworkImplementation.getIHMConnexionInterface().receiveErrorSendingMessage(message);
    }
    
}
