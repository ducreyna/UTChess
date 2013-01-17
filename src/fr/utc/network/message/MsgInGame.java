/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.message;

import fr.utc.network.profile.IpProfile;
import fr.utc.network.services.NetworkImplementation;

/**
 *
 * @author Robin Bergère
 */
public class MsgInGame extends MessageNetwork {
    private String _ip;
    private boolean _isInGame;
    /**
     *
     * @param p The id of the player who is now InGame
     * @param isInGame Whether the player is in game or not
     */
    public MsgInGame(String ip, boolean isInGame) {
        _ip = ip;
        _isInGame = isInGame;
    }
    /*
     * Finds the player in our player list and change his InGame state if he is found
     */
    public void traiterMessage() {
        for (IpProfile p : NetworkImplementation.getListOfProfiles().getPlayerList()){
            if (p.getIp().equals(_ip))
            {
                p.getProfile().setInGame(_isInGame);
                NetworkImplementation.getListOfProfiles().updateProfile(_ip, p.getProfile());
                break;
            }
        }
    }

    @Override
    public void reportError(Exception e)
    {
        String message = "Erreur, message mise à jour InGame : " + e.getMessage();
        NetworkImplementation.getIHMConnexionInterface().receiveErrorSendingMessage(message);
    }
    
}
