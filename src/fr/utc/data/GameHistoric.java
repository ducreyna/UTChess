/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.data;

/**
 *
 * @author cnzengan, kevorkian jocelyn
 */

import fr.utc.data.historique.*;
import java.util.LinkedList;

/*
 * GameHistoric contains the list of Action performed during the game. It reprents the historic.
 */
public class GameHistoric implements java.io.Serializable
{
    /*
     * List of Action performed (Message and Move).
     */
    protected LinkedList<Action> _historic;


    /*
     * Empty constructor of GameHistoric. Creates an empty list of Action.
     */
     public GameHistoric () {
         _historic = new LinkedList<Action>();
     }
     
     /*
      * Adds a new Action to the historic.
      * @param  newAction
      *             The new action to add to the list.
      */
     public void pushBack(Action newAction){
         _historic.addLast(newAction);
     }
     
     /*
      * Extracts the older action of the historic and removes it from the list.
      * @return The older action of the game.
      * 
      */
     public Action popFront(){
         return _historic.removeFirst();
     }
}


