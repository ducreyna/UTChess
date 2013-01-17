/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.processing;

import fr.utc.network.message.MessageNetwork;
import fr.utc.network.services.NetworkImplementation;
import java.io.InputStream;
import java.net.Socket;
import java.io.ObjectInputStream;

/**
 *
 * @author Robi
 */
public class ThreadMessageHandler implements Runnable
{
    private Socket _theSocket = null; // La Socket de message

    /**
     * Constructeur de ThreadMessageHandler
     * @param theSocket => la socket recue
     * @exception ErrorCreatingThreadException en cas de message null
     */
    public ThreadMessageHandler( Socket theSocketToProcess)
    {
        this._theSocket  = theSocketToProcess;
    }
    
      /*
     * 
     */
    public Socket getTheSocket()
    {
        return this._theSocket;
    }


    /**
     * TODO : dsfdsg
     */
    
    /**
     * Méthode RUN du Thread ThreadMessageHandler
     */
    public void run()
    {
        MessageNetwork theMessage = null;

        try
        {
            InputStream is = _theSocket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            theMessage = (MessageNetwork)ois.readObject();
            theMessage.traiterMessage();
        }
        catch (Exception ex)
        { 
            NetworkImplementation.getIHMConnexionInterface().receiveErrorSendingMessage("Erreur, ThreadMessageHandler : " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
}
