/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.grid.board;

import fr.utc.data.Color;
import fr.utc.data.piece.Piece;
import java.io.Serializable;


/**
 * PieceView is a representation of a pawn with a simple image and linked to a BoardCell
 * @author Rémi Durancet & Cédric Gobillot
 */
public class PieceView implements Serializable{
    /**
     * Link to the data representation of the pwn
     */
    private Piece _piece;
    
    /**
     * Link to the cell of the board
     */
    private BoardCell _boardCell;
    
     /**
     * Default constructor
     * @param p The Piece linked to this view
     */
    public PieceView(Piece p)
    {
        this._piece = p;
    }
    
    /**
     * Constructor used when a PieceView has no Piece on it (Board Cell empty)
     */    
    public PieceView()
    {
        
    }

    /**
     * Function associating 
     */        
    public String imageForPiece(boolean isIcon)
    {
          
        String pieceColor = "white";
        String iconExt = "";
    
        if(this._piece.getColor() == Color.black)
        {
            pieceColor = "black";
        }
        
        String pieceName = this._piece.getClass().getName();
        
        if (isIcon)
        {
            iconExt = "_32";
        }
System.out.println("fr/utc/ressources/piece/"+(pieceName.toLowerCase()).replaceAll("^.+\\.", "")+"_"+pieceColor+iconExt+".png");
        return "fr/utc/ressources/piece/"+(pieceName.toLowerCase()).replaceAll("^.+\\.", "")+"_"+pieceColor+iconExt+".png";
    }

    public String imageForPiece()
    {
        return imageForPiece(false);
    }    
    
    /**
     * @return the pawn
     */
    public Piece getPawn() {
        return _piece;
    }

    /**
     * @param pawn the pawn to set
     */
    public void setPawn(Piece pawn) {
        this._piece = pawn;
    }

    public void deletePawn(){
        this._piece = null;
    }
    
    /**
     * @return the boardCell
     */
    public BoardCell getBoardCell() {
        return _boardCell;
    }

    /**
     * @param boardCell the boardCell to set
     */
    public void setBoardCell(BoardCell boardCell) {
        this._boardCell = boardCell;
    }
    
    
}
