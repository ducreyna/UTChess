/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.processing;

import fr.utc.network.message.MessageNetwork;

/**
 *
 * @author Robin Bergère
 */
public class ThreadMessageHandlerMulticast implements Runnable
{
    private MessageNetwork _msg = null;
    /**
     * Create a thread to handle the MessageNetwork msg
     * @param msg MessageNetwork object to handle
     */
    public ThreadMessageHandlerMulticast(MessageNetwork msg)
    {
        _msg = msg;
    }
    /**
     * {@inheritDoc}
     */
    public void run()
    {
        _msg.traiterMessage();
    }
}
