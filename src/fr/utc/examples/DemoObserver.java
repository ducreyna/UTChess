package fr.utc.examples;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author maxime
 */

import fr.utc.network.profile.IpProfile;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
        
public class DemoObserver implements Observer
{


    public void update(Observable o, Object o1)
    {  
        System.out.println("Liste :");
        ArrayList<IpProfile> array = ((ArrayList<IpProfile>)o1);

        for(IpProfile profiles : array)
        {
            System.out.println("=> "+profiles.getIp() +" =>"+profiles.getProfile().getPseudo());
        }
    }
    
    
}
