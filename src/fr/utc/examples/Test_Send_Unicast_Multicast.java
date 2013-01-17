/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.examples;

import fr.utc.data.Profile;
import fr.utc.network.profile.ManagerListPlayer;
import fr.utc.network.services.NetworkImplementation;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Clément
 */
public class Test_Send_Unicast_Multicast
{


//    public static void main(String[] args)
//    {
//        Profile test;
//        NetworkImplementation Ni;
//        try
//        {
//            test = new Profile(InetAddress.getLocalHost().toString(), "test");
//            Ni = new NetworkImplementation(test);
//            Ni.startThreadListenNetwork();
//
//
//            ManagerListPlayer list = Ni.getListOfUsers();
//            DemoObserver obs = new DemoObserver();
//            list.addObserver(obs);
//
//
//
//
//        } catch (Exception ex)
//        {
//            Logger.getLogger(Test_Send_Unicast_Multicast.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
