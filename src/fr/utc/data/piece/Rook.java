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

public class Rook extends Piece{

    protected boolean _moved;
    protected Grid _grid;

    public Rook(Position position,boolean color){
        super(position, color);
        _moved = false;
    };
    
    public Rook(Position position, boolean color, boolean moved){
        super(position, color);
        _moved = moved;
    }

    public boolean specificRules(Position pos, Grid grid){
        _grid=grid;
        //déplacement spécifique
        
        //déplacement normal
          if(this.checkPath(this.getPosition(), pos, grid)){
             if(pos.getColumnLetter()!=this.getPosition().getColumnLetter() && pos.getLine()==this.getPosition().getLine()){
                        return true;
                    }
                                /**
                     * check if new position moved like Rook, changed only the column
                     */
              else if(pos.getColumnLetter()==this.getPosition().getColumnLetter() && pos.getLine()!=this.getPosition().getLine()){
                        return true;
                                                        /**
                     * check if new position moved like Rook, changed only the line
                     */
                    }
          }
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
        return "Rook";
    }
}
