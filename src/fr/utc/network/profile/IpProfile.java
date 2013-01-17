/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.profile;

import fr.utc.data.Profile;
import java.io.Serializable;
/**
 * 
 * @author maxime
 */
public class IpProfile implements Serializable{
    
    /**
     * Profile from the package "Data"
     */
    private Profile _profile;
    
    /**
     * Ip adress of the player
     */
    private String _ip; // TODO : Transformer cet attribut en InetAdress
    
    /**
     * State of the player profile
     */
    private boolean _isInGame;


    
    public IpProfile(Profile _profile, String _ip, boolean _isInGame) {
        this._profile = _profile;
        this._ip = _ip;
        //this._isInGame = _isInGame;
    }




    public String getIp() {
        return _ip;
    }

    public void setIp(String _ip) {
        this._ip = _ip;
    }

    /*public boolean getIsInGame() {
        return _isInGame;
    }

    public void setIsInGame(boolean _isInGame) {
        this._isInGame = _isInGame;
    }*/

    public Profile getProfile() {
        return _profile;
    }

    public void setProfile(Profile _profile) {
        this._profile = _profile;
    }



}
