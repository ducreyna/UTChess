/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.data.piece;
import fr.utc.data.Color;

/**
 *
 * @author liruoche
 */
import fr.utc.data.Position;
import fr.utc.data.Grid;

public class Pawn extends Piece{

    protected boolean _moved;

    public Pawn(Position position,boolean color){
        super(position, color);
        _moved = false;
    };

    public boolean specificRules(Position pos, Grid grid){
        
        if(!checkPath(this._pos, pos, grid))        //if there is piece on the path, then bad move.
            return false;
        
        if(_color == Color.black && pos.getLine()>_pos.getLine()) //a black piece which tries go backwards
            return false;
        
        if(_color == Color.white && pos.getLine()<_pos.getLine())   //a white piece which tries to go backwards
            return false;
        
        int moveX = Math.abs(pos.getColumnNumber()-this.getPosition().getColumnNumber());
        int moveY = Math.abs(pos.getLine()-this.getPosition().getLine());
        
        if(moveY==1 && moveX==0 && grid.getAt(pos)==null)
            return true;
        else if(moveY==2 && moveX==0 && _moved==false &&grid.getAt(pos)==null)
            return true;
        else if(moveY==1 && moveX==1 && grid.getAt(pos)!=null)
            return true;
        else
            return false;
    };
    
    @Override
    public void setPosition(Position pos){
        this._pos = pos;
        _moved = true;
    }

    static public String className(){
        return "Pawn";
    }
}
