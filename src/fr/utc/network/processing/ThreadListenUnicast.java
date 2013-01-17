/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.processing;

import fr.utc.network.common.Constant;
import fr.utc.network.services.NetworkImplementation;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author Bensalah Tarik
 * 
 * The ThreadListenUnicast class allows to put in place
 * a thread for unicast listening.
 */
public class ThreadListenUnicast implements Runnable {

    /**
     * List containing all the the current ThreadMessageHandler instances
     */
    private ArrayList<ThreadMessageHandler> _threadMessageHandlerList;
    
    /**
     * Unicats listening socket
     */
    private ServerSocket _listen_socket;
    
    /**
     * Put to True to stop the current thread process
     */
    private boolean _stop;
    
    /**
     * Default constructor for ThreadListenUnicast
     * Sets a new ServerSocket listening the DEFAULT_PORT.
     */
    public ThreadListenUnicast()
    {
        try
        {
            this._listen_socket = new ServerSocket(Constant.DEFAULT_PORT);
            this._threadMessageHandlerList = new ArrayList<ThreadMessageHandler>();

            this._stop = false;
        }
        catch(IOException ex)
        {
            System.err.println("Pb lors de la création de la socket d'écoute au port :" + Constant.DEFAULT_PORT);
            System.err.println(ex.getMessage());
        }                 
    }
    
    /**
     * {@inheritDoc}
     */
    public void run() {
	try {
            while(this._stop == false) {
                ThreadMessageHandler messageHandler = getHandledMessage();
                new Thread(messageHandler).start();
                this._threadMessageHandlerList.add(messageHandler);
            }
        }
        catch (IOException e) {
            NetworkImplementation.getIHMConnexionInterface().receiveErrorSendingMessage("Erreur, ThreadUnicast : " + e.getMessage());
        }
        finally 
        {
            try {_listen_socket.close();} catch (Exception e) {}
	}
    }
    
    /**
     * Allows to stop the listening thread and to close
     * the listening socket
     */
    public void stopThread()
    {
        this._stop = true;
    }
    
    /**
     * Waits for incoming network request and generates a message
     * @return ThreadMessageHandler : the received message to be processed
     * @throws IOException 
     */
    private ThreadMessageHandler getHandledMessage()throws IOException
    {
        Socket _client_socket = this._listen_socket.accept();
        
        return new ThreadMessageHandler(_client_socket);			
    }
    
    /**
     * Allows to close the the current socket.
     */
    public void close() {
        try {this._listen_socket.close();} catch (IOException e) {}	
    }
    
}
