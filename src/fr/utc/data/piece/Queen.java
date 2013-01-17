/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.data.piece;

/**
 *
 * @author liruoche
 */

import fr.utc.data.Position;
import fr.utc.data.Grid;

public class Queen extends Piece{

    public Queen(Position position,boolean color){
        super(position, color);
    };

    /**
     * chack if Queen movement is valid
     * @param pos new position sent by the player
     * @return return true if the new position is on the grid and valid else return false
     */
    public boolean specificRules(Position pos, Grid grid){
        
        int moveY = Math.abs(pos.getColumnNumber()-this.getPosition().getColumnNumber());
        int moveX = Math.abs(pos.getLine()-this.getPosition().getLine());
        
        if(!checkPath(this._pos, pos, grid))        //if there is piece on the path, then bad move.
            return false;
        
        if(moveY==moveX)    //If the queen moves in diagonal
            return true;
        else if(pos.getColumnLetter()!=this._pos.getColumnLetter() && pos.getLine()==this._pos.getLine())
            return true;    //If the queen moves only on the line
        else if(pos.getColumnLetter()==this._pos.getColumnLetter() && pos.getLine()!=this._pos.getLine())
            return true;    //If the queen moves only on the column
        else                
            return false;   //Impossible move

    };

    static public String className(){
        return "Queen";
    }
}
