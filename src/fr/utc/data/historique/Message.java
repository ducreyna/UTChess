/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.data.historique;

/**
 *
 * @author Administrator
 */

import fr.utc.data.Player;
import java.util.Date;

/*
 * Message is a class extended from Action. It represents a message exchanged between players.
 * It contains the message exchanged plus the attributes from Action.
 * @see Action
 */

public class Message extends Action{
    /*
     * Represents the message exchanged between the players.
     */
    protected String _msg;
    
    /*
     * Constructor of Message.
     * @param time
     *          Date at which the message was created (or received).
     * @param emitter
     *          Represents the player who sends the message.
     * @param message
     *          Message exchanged.
     */
    public Message(Date time,Player emitter,String message){
        _timestamp = time;
        _emitter = emitter;
        _msg = message;
    }

    /**
     * Getter of the attribute message
     * @return the message stored in the Message
     */
    public String getMsg(){return _msg;}

    /**
     * Setter of the attrbute message
     * @param msg 
     */
    public void setMsg(String msg){_msg=msg;}
}
