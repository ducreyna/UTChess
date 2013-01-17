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
import java.math.*;

public class King extends Piece{

    protected boolean _moved;
    protected Grid _grid;

    public King(Position position,boolean color){
        super(position, color);
        _moved = false;
    };

    public boolean specificRules(Position pos, Grid grid){
        _grid=grid;
        
        //We check if the deplacement is in a square around the piece
        if(Math.abs(pos.getColumnNumber()-this._pos.getColumnNumber())==1 && Math.abs(pos.getLine()-this._pos.getLine())==0)
            return true;
        
        else if(Math.abs(pos.getColumnNumber()-this._pos.getColumnNumber())==1 && Math.abs(pos.getLine()-this._pos.getLine())==1)
            return true;

        else if(Math.abs(pos.getColumnNumber()-this._pos.getColumnNumber())==0 && Math.abs(pos.getLine()-this._pos.getLine())==1)
            return true;
         //if the moving is just one box 
        
        return false;
    };
      
      public boolean getSpecificMoved(){
          return _moved;
      }
      
    @Override
    public void setPosition(Position pos){
        this._pos = pos;
        _moved = true;
    }
    
    static public String className(){
        return "King";
    }
}

