/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.profile;

import fr.utc.data.Profile;
import java.util.ArrayList;
import java.util.Observable;

/**
 * 
 * @author maxime
 */
public class ManagerListPlayer extends Observable{
    /**
     * List of Ip profiles
     */
    private ArrayList<IpProfile> _ipProfileList;
    
    /**
     * Constructor 
     */
    public ManagerListPlayer()
    {
        _ipProfileList = new ArrayList<IpProfile>();
    }
            
    /**
     * Get a reference on the list of Ip profiles
     * @return a reference on the list of Ip profiles
     */
    public ArrayList<IpProfile> getPlayerList()
    {
        return _ipProfileList;
    }
    
    /**
     * Add the IpProfile from the list
     * @param ipProfile The IpProfile object to add
     */

    public void addPlayer(IpProfile ipProfile)
    {
	_ipProfileList.add(ipProfile);
        setChanged();
        notifyObservers(_ipProfileList);
    }
    
    /**
     * Delete the IpProfile from the list
     * @param ipProfile The IpProfile object to delete
     */
    public void remPlayer(IpProfile ipProfile)
    {
        IpProfile profile_to_remove = null;
        for(IpProfile profile : _ipProfileList)
        {
            if(profile.getIp().equals(ipProfile.getIp()))
            {
                profile_to_remove = profile;

            }
        }
        _ipProfileList.remove(profile_to_remove);
        setChanged();
        notifyObservers(_ipProfileList);
    }
    /**
     * Update the profile of a player from it's ip (ip = identificator)
     * @param ip The ip of the player whose profile has to be modified
     */
    public void updateProfile(String ip, Profile prof)
    {
        for (IpProfile ipProfile : _ipProfileList)
        {
           if (ipProfile.getIp().equals(ip)) 
           {
            //update the profile if the ip matches
               ipProfile.setProfile(prof);
               setChanged();
               notifyObservers(_ipProfileList);
               break;
           }
                
        }
    }
        

}
