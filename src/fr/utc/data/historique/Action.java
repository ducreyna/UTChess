
package fr.utc.data.historique;

/**
 *
 * @author li ruochen, kevorkian jocelyn
 */

import fr.utc.data.Player;
import java.util.Date;
import java.io.Serializable;

/*
 * Action is the class that representes something that happens during the game at a precise instant. 
 * It could be a message between players or the movement of a piece on the board.
 */
abstract public class Action implements Serializable {
    /*
     * Date at which the action was performed.
     */
    protected Date _timestamp;
    /*
     * Represents the player that realized the action.
     */
    protected Player _emitter;
    
    /*
     * Empty constructor of an Action.
     */
    public Action(){}
    
    /*
     * Constructor of an Action.
     * @param time
     *          Date at which the action was performed.
     * @param emitter
     *          The player who realized the action.
     */
    public Action(Date time,Player emitter){
        _timestamp = time;
        _emitter = emitter;
    }

    //Getter
    /**
     * Getter of the attribute time.
     * @return the date of creation of the action.
     */
    public Date     getTime(){return _timestamp;}
    
    /**
     * Getter of the attribute emitter.
     * @return the player who realized the action.
     */
    public Player   getEmitter(){return _emitter;}

    //setter
    /**
     * Setter of the attribute time.
     * @param time 
     */
    public void     setTime(Date time){_timestamp=time;}
    
    /**
     * Setter of the attribute emitter.
     * @param emitter 
     */
    public void     setEmitter(Player emitter){_emitter=emitter;}

}
