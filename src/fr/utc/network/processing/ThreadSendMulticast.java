/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.processing;

import fr.utc.network.common.Constant;
import fr.utc.network.common.Utils;
import fr.utc.network.exceptions.ErrorCreatingThreadException;
import fr.utc.network.message.MessageNetwork;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * @quality Envoi = Send
 * @author Robi
 */
public class ThreadSendMulticast implements Runnable {

    private MessageNetwork _msg;

    /**
     * Constructor of ThreadSendMulticast
     * @param msg : The message to be sent to all players
     */
    public ThreadSendMulticast(MessageNetwork msg) throws ErrorCreatingThreadException {
        if(msg != null) {
            _msg = msg;
        }
        else {
            throw new ErrorCreatingThreadException();
        }
    }

    /**
     * Launches the thread. The message is sent to all the players thanks to the
     * multicast group IP address
     */
    public void run() {
        try {
            byte[] buf = Utils.objectToByteArray(_msg);
            System.out.println("sending multicast message...");
            InetAddress group = InetAddress.getByName(Constant.DEFAULT_MULTICAST_IP);
            System.out.println(buf.length);
            DatagramPacket packet;
            packet = new DatagramPacket(buf, buf.length, group, Constant.DEFAULT_MULTICAST_PORT);
            MulticastSocket socket = new MulticastSocket(Constant.DEFAULT_MULTICAST_PORT);
            socket.send(packet);
            socket.close();
            System.out.println("sending multicast message OK");
            //System.out.println("envoi terminé");

        }
        catch (IOException e) {
            //use error notification interface
            _msg.reportError(e);
        }
        catch (SecurityException e) {
            //use error notification interface
            _msg.reportError(e);
        }
    }

    
}
