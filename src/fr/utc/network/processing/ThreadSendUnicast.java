/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.processing;

import fr.utc.network.common.Constant;
import fr.utc.network.exceptions.ErrorCreatingThreadException;
import fr.utc.network.message.MessageNetwork;
import fr.utc.network.profile.IpProfile;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @quality Envoi = Send
 * @author Robi
 */
public class ThreadSendUnicast implements Runnable {

    private IpProfile _profile = null;
    private MessageNetwork _message = null;


    public ThreadSendUnicast(IpProfile leProfilDistant, MessageNetwork leMessage) throws ErrorCreatingThreadException
    {
        if(leMessage!=null)
        {
            this._message = leMessage;
        }
        else
        {
            throw new ErrorCreatingThreadException();
        }

        if(leProfilDistant!=null)
        {
            this._profile = leProfilDistant;
        }
        else
        {
            throw new ErrorCreatingThreadException();
        }
    }





    public void run() 
    {
        Socket socket;
	PrintWriter out = null;
        try
        {

             socket = new Socket(_profile.getIp(),Constant.DEFAULT_PORT);

             OutputStream os = socket.getOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(os);

             oos.writeObject(_message);
             oos.close();
             os.close();

             socket.close();

        }catch (Exception e)
        {
            this._message.reportError(e);
        }
    }
    
}
