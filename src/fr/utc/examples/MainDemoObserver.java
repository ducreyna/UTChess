package fr.utc.examples;

import fr.utc.data.Profile;
import fr.utc.network.profile.IpProfile;
import fr.utc.network.profile.ManagerListPlayer;
import fr.utc.network.services.NetworkImplementation;
import fr.utc.network.services.NetworkInterface;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author maxime
 */
public class MainDemoObserver
{

//    public static void main(String [ ] args){
//
//         /**
//         * Create the netWork utilities, just use the reference you got at the begining in the "real application"
//         */
//        NetworkImplementation netUtilities = new NetworkImplementation(new Profile());
//        /**
//         * Get the list of player from the netWork utilities
//         */
//        ManagerListPlayer list = netUtilities.getListOfUsers();
//
//        /**
//         * Create an observer object
//         */
//        DemoObserver obs = new DemoObserver();
//
//        /**
//         * Add this Observer to the notified objects list
//         */
//        list.addObserver(obs);
//        /**
//         * Let's create some Profiles and iPprofiles for the example
//         */
//        Profile profile1 = new Profile("max", "max");
//        Profile profile2 = new Profile("tar", "tar");
//        Profile profile3 = new Profile("clem", "clem");
//
//        IpProfile ipProfile1 = new IpProfile(profile1, "0.0.0.0", true);
//        IpProfile ipProfile2 = new IpProfile(profile2, "1.1.1.1", true);
//        IpProfile ipProfile3 = new IpProfile(profile3, "2.2.2.2", true);
//
//        /**
//         * Some interaction with the list
//         */
//        list.addPlayer(ipProfile1);
//        list.addPlayer(ipProfile2);
//        list.addPlayer(ipProfile3);
//        list.remPlayer(ipProfile1);
//
//    }
}
