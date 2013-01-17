/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.message;

import fr.utc.network.exceptions.ErrorCreatingThreadException;
import fr.utc.network.processing.ThreadSendUnicast;
import fr.utc.network.profile.IpProfile;
import fr.utc.network.services.NetworkImplementation;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maxime
 */
public class MsgRequestProfile extends MessageNetwork {
    /**
     * The local profile to communicate to other computers
     */
    private IpProfile _profile;
    
    /**
     * Constructor, called when the message is created before to be sent
     * @param _profile 
     */
    public MsgRequestProfile(IpProfile _myProfile)
    {
        this._profile = _myProfile;
    }
    
    @Override
    public void traiterMessage() {
	/**
         * add the profile to the local list
         */
        NetworkImplementation.getListOfProfiles().addPlayer(_profile);
        /**
         * send the local profile to the asker
         */
        MsgResponseProfile resp = new MsgResponseProfile();
        ThreadSendUnicast thread;
        try
        {
            System.out.println(_profile);
            thread = new ThreadSendUnicast(_profile, resp);
            new Thread(thread).start(); 
        } catch (ErrorCreatingThreadException ex)
        {
            Logger.getLogger(MsgRequestProfile.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void reportError(Exception e)
    {
        String message = "Erreur, envoi du profil sur le réseau : " + e.getMessage();
        NetworkImplementation.getIHMConnexionInterface().receiveErrorSendingMessage(message);
    }
}
