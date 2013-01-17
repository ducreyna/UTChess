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
import fr.utc.data.Position;
import java.util.Date;

/*
 * Move is a class extended from Action. It represents a displacement on the board.
 * It contains the old position of the piece and its new position. Plus attributes of Action. 
 * @see Action
 */

public class Move extends Action{
    /*
     * Represents the old position of the piece before the displacement.
     */
    protected Position _oldPos;
    /*
     * Represents the new position of the piece after the displacement.
     */
    protected Position _newPos;
    
    /*
     * Constructor of Move.
     * @param time
     *          Date at which the move was performed.
     * @param emitter
     *          The player who realized the displacement.
     * @param oldPosition
     *          Represents the old position of the piece, before the move.
     * @param newPosition
     *          Represents the new position of the piece, after the move.
     */
    public Move(Date time,Player emitter,Position oldPosition,Position newPosition){
        _timestamp = time;
        _emitter = emitter;
        _oldPos = oldPosition;
        _newPos = newPosition;
    }
    
    public Move(Move m){
        _timestamp = m._timestamp;
        _emitter = m._emitter;
        _oldPos = m._oldPos;
        _newPos = m._newPos;  
    }

    //getter
    public Position getOldPos(){return _oldPos;}
    public Position getNewPos(){return _newPos;}

    //setter
    public void setOldPos(Position oldPos){_oldPos=oldPos;}
    public void setNewPos(Position newPos){_newPos=newPos;}
}
