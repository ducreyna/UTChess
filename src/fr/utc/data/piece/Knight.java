package fr.utc.data.piece;


import fr.utc.data.Position;
import fr.utc.data.Grid;
import java.math.*;

/*
 * Represents the piece Knight, extended from Piece.
 * @see Piece
 */
public class Knight extends Piece{

    /*
     * Constructor of Knight.
     * @param position
     *              Position of the piece.
     * @param color
     *              Color of the piece.
     */
    public Knight(Position position,boolean color){
        super(position, color);                         //Calls the constructor of Piece.
    }

    public boolean specificRules(Position pos, Grid grid){
      
        if((Math.abs(pos.getColumnNumber()-_pos.getColumnNumber())==2 && Math.abs(pos.getLine()-_pos.getLine())==1) ||
                (Math.abs(pos.getColumnNumber()-_pos.getColumnNumber())==1 && Math.abs(pos.getLine()-_pos.getLine())==2))
            return true;
        else             
            return false;
    }
    
    static public String className(){
        return "Knight";
    }
}
