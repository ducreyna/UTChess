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
import java.lang.Math;

public class Bishop extends Piece{

    public Bishop(Position position,boolean color){
        super(position, color);
    };

    /**
     * This function check is bishop did a mouvement good movement on the grid
     *
     * @param pos position choose by the player
     * @return return true if the position is correct and false if it's incorrect
     */
    public boolean specificRules(Position pos, Grid grid){
               
        int moveY = Math.abs(pos.getColumnNumber()-this.getPosition().getColumnNumber());
        int moveX = Math.abs(pos.getLine()-this.getPosition().getLine());
        if(moveX!=moveY)    //The bishop moves in diagonal
            return false;
        
        if(!checkPath(this._pos, pos, grid))
            return false;
        
        return true;
    };
    
    static public String className(){
        return "Bishop";
    }
}
