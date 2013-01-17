/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.processing;

import fr.utc.network.common.Constant;
import fr.utc.network.common.Utils;
import fr.utc.network.message.MessageNetwork;
import fr.utc.network.services.NetworkImplementation;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author Robin Bergère
 */
public class ThreadListenMulticast implements Runnable {
    private MulticastSocket _socket = null;
    /**
     * Put true to stop the socket from listening
     */
    private boolean _stop = false;
    private ArrayList<ThreadMessageHandlerMulticast> _threadMessageHandlerList = new ArrayList<ThreadMessageHandlerMulticast>();
    /**
     * _threadMessageHandlerList getter
     * @return List of MessageHandlerMulsticast threads
     */
    public ArrayList<ThreadMessageHandlerMulticast> getThreadMessageHandlerList() {
        return _threadMessageHandlerList;
    }
    /**
     * {@inheritDoc}
     */
    public void run() {
        byte[] buf = new byte[1024];// TODO MTU ?
        try {
            // Initializations
            InetAddress group = InetAddress.getByName(Constant.DEFAULT_MULTICAST_IP);
            DatagramPacket packet;
            packet = new DatagramPacket(buf, buf.length);
            _socket = new MulticastSocket(Constant.DEFAULT_MULTICAST_PORT);
            _socket.joinGroup(group);

            // Receive loop
            while (!_stop)
            {
                //Logger.getLogger(ThreadListenMulticast.class.getName()).log(Level.INFO, null, "Waiting for a packet...");
                System.out.println(ThreadListenMulticast.class.getName()+" : Waiting for a packet...");
                _socket.receive(packet);

                // Ignore our own messages
                if (!packet.getAddress().getHostAddress().toString().equals(InetAddress.getLocalHost().getHostAddress()))
                {
                    // Convert the bytes into an Object
                    Object o = Utils.byteArrayToObject(packet.getData());
                    if (o != null)
                    {
                        //Logger.getLogger(ThreadListenMulticast.class.getName()).log(Level.INFO, null, "Packet received : " + o.toString());
                        System.out.println(ThreadListenMulticast.class.getName()+" : Packet received : " + o.toString());
                        ThreadMessageHandlerMulticast tmh = new ThreadMessageHandlerMulticast((MessageNetwork)o);
                        _threadMessageHandlerList.add(tmh);
                        new Thread(tmh).start();
                    }
                }
            }

            // Socket cleanup
            _socket.leaveGroup(group);
            _socket.close();
        } catch (UnknownHostException ex) {
            NetworkImplementation.getIHMConnexionInterface().receiveErrorSendingMessage("ThreadMulticast.UnknownHostException: " + ex.getMessage());
        } catch (IOException ex) {
            NetworkImplementation.getIHMConnexionInterface().receiveErrorSendingMessage("ThreadMulticast.IOException: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            NetworkImplementation.getIHMConnexionInterface().receiveErrorSendingMessage("ThreadMulticast.ClassNotFound: " + ex.getMessage());
        }
        //Logger.getLogger(ThreadListenMulticast.class.getName()).log(Level.INFO, null, "Thread closed");
        System.out.println(ThreadListenMulticast.class.getName()+" : Thread closed");
    }

    /**
     * Tell the thread it has to stop (will close after the next message is received)
     */
    public void stopThread()
    {
        _stop = true;
    }
}
