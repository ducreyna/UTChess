
package fr.utc.data;

/**
 *
 * @author LI Ruochen
 */

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;
import fr.utc.data.historique.Move;
import fr.utc.data.piece.*;
import fr.utc.data.Color;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Iterator;

public class Grid implements Serializable, Cloneable {

    protected Piece[][] _board;
    protected LinkedList<Piece> _whitePieces;
    protected LinkedList<Piece> _blackPieces;
    
    public Grid(){
        _board = new Piece[8][8];   //Allocation of the array
        _whitePieces = new LinkedList<Piece>();
        _blackPieces = new LinkedList<Piece>();
        //Then we fill line by line the board
        try{
            _board[0][0] = new Rook(new Position('A', 8), Color.black);
            _board[0][1] = new Knight(new Position('B',8), Color.black);
            _board[0][2] = new Bishop(new Position('C',8), Color.black);
            _board[0][3] = new Queen(new Position('D',8), Color.black);
            _board[0][4] = new King(new Position('E',8), Color.black);
            _board[0][5] = new Bishop(new Position('F',8), Color.black);
            _board[0][6] = new Knight(new Position('G',8), Color.black);
            _board[0][7] = new Rook(new Position('H',8), Color.black);
            for(int i=0;i<8;i++)
                _board[1][i] = new Pawn(new Position(i+1,7), Color.black);
            
            for(int i=2;i<6;i++)
                for(int j=0;j<8;j++)
                    _board[i][j] = null; 
            
            for(int i=0;i<8;i++)
                _board[6][i] = new Pawn(new Position(i+1,2), Color.white);
            _board[7][0] = new Rook(new Position('A',1), Color.white);
            _board[7][1] = new Knight(new Position('B',1), Color.white);
            _board[7][2] = new Bishop(new Position('C',1), Color.white);
            _board[7][3] = new Queen(new Position('D',1), Color.white);
            _board[7][4] = new King(new Position('E',1), Color.white);
            _board[7][5] = new Bishop(new Position('F',1), Color.white);
            _board[7][6] = new Knight(new Position('G',1), Color.white);
            _board[7][7] = new Rook(new Position('H',1), Color.white);
                
        }
        catch(Exception e){
            
        }
        
        //fill the lists
        populateListsPieces();
    }

    /**
     * This constructor return a grid with a board empty. 
     * It is used to create a copy of a grid.
     * @param useless This parameter is here just to distinguish this one and the normal constructor
     */
    public Grid(boolean useless){
        _board = new Piece[8][8];   //Allocation of the array
        _whitePieces = new LinkedList<Piece>();
        _blackPieces = new LinkedList<Piece>();
        
        //fill the lists
        populateListsPieces();
    }
    
    public Piece[][] getBoard(){return this._board;}
    public LinkedList<Piece> getWhitePieces(){return this._whitePieces;}
    public LinkedList<Piece> getBlackPieces(){return this._blackPieces;}


    public void displayBoard(){
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(_board[i][j] != null)
                     System.out.print(new String(_board[i][j].getClass().toString()) + new String("\t"));
                else
                    System.out.print("\t\t\t");
            }
            System.out.print("\n");
        }
    
     };
    
    /*Le mouvement doit avoir été validé auparavant*/
    public void doMove(Position oldPos, Position newPos){
        int arr[] = convertPosToIndex(newPos);    //get the indexes for the array
        int dep[] = convertPosToIndex(oldPos);    //get the indexes for the array
        
        _board[arr[0]][arr[1]] = _board[dep[0]][dep[1]];            //transfer the content of the cell 
        _board[arr[0]][arr[1]].setPosition(newPos);
        _board[dep[0]][dep[1]] = null;                              //old cell becomes null
        
        //update the lists
        populateListsPieces();
    };
    
    /**
     * This function fills the lists of pieces blackPieces and whitePieces with the king in the first position.
     */
    public void populateListsPieces(){
        _whitePieces.clear();
        _blackPieces.clear();
        for(int i=0;i<8;i++){       //we go through the lines
            
            for(int j=0;j<8;j++){   //we go through the colums
            
                if(_board[i][j]!=null){         //if there is a piece
                
                    if(_board[i][j].getColor() == Color.black)  //if it is a black piece
                    {
                        if(_board[i][j] instanceof King)
                            _blackPieces.addFirst(_board[i][j]);
                        else
                            _blackPieces.add(_board[i][j]);             //then we add it to the list of black pieces
                    }
                    else                                        //else it is a white piece
                    {
                        if(_board[i][j] instanceof King)
                            _whitePieces.addFirst(_board[i][j]);
                        else
                            _whitePieces.add(_board[i][j]);             //then we add it to the list of white pieces
                    }
                }   
         
            }
        }
    }
    
    /**
     * Return the what is on the cell of the board. Return a piece or null if there is nothing. 
     * WARNING : be sure to check what it returns. 
     * @param pos The position you want to retrieve.
     * @return A piece or null.
     */
    public Piece getAt(Position pos){
        int dep[] = convertPosToIndex(pos);
        return _board[dep[0]][dep[1]];
    }
    
    /**
     * Convert a position to the indexes one can use to access the grid.
     * @param pos The position you want to access in the grid
     * @return  An array with firstly the line (array[0]) and secondly the column (array[1])
     */
    private int[] convertPosToIndex(Position pos){
        int result[] = new int[2];
        result[0] = 8 - pos.getLine();
        result[1] = pos.getColumnNumber() - 1;
        
        return result;
    }
    
    /**
     * Create a copy of the grid.
     * @return A new grid similar to this one.
     */
    @Override
    public Grid clone(){
        Grid copy = new Grid(true);     //we call the private constructor
        
        for(int i=0;i<8;i++)            //we go through lines   
        {
            for(int j=0;j<8;j++)        //we go through columns
            {
                if(_board[i][j] != null){   //if there is a piece
                    try{
                        copy._board[i][j] = (Piece)(_board[i][j].clone());  //we make a copy
                       
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
                         
        return copy;
    }
    
    /**
     * This function sets the piece at the position.
     * WARNING : this may removes a piece of the grid if pos is alrealdy filled.
     * @param piece The new piece to put on the board.
     * @param pos   The position at which the piece will be put.
     */
    public void setAt(Piece piece, Position pos){
        int newPos[] = convertPosToIndex(pos);
        
        _board[newPos[0]][newPos[1]] = piece;
        populateListsPieces();
    }

}
