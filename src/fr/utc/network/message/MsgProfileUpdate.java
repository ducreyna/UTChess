/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.message;

import fr.utc.network.profile.IpProfile;
import fr.utc.network.profile.ManagerListPlayer;
import fr.utc.network.services.NetworkImplementation;

/**
 *
 * @author maxime
 */
public class MsgProfileUpdate extends MessageNetwork {
    /**
     * The local profile to communicate to other computers
     */
    private IpProfile _profile;
    /**
     * Constructor, called when the message is created before to be sent
     * @param _profile
     */
    public MsgProfileUpdate(IpProfile _profile)
    {
        this._profile = _profile;
    }

    @Override
    public void traiterMessage() {
	/**
         * update the profile
         */
        System.out.println("Profil reçu :" + _profile.getProfile().getName());
        NetworkImplementation.getListOfProfiles().updateProfile(_profile.getIp(), _profile.getProfile());
 
    }

    @Override
    public void reportError(Exception e)
    {
        String message = "Erreur, mise à jour du profil : " + e.getMessage();
        NetworkImplementation.getIHMConnexionInterface().receiveErrorSendingMessage(message);
    }
}
    
