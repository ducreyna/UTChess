/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.data.piece;
import fr.utc.data.historique.Move;
import fr.utc.data.piece.Piece;


/**
 *
 * @author Group DATA
 */

import fr.utc.data.Position;
import fr.utc.data.Grid;

public abstract class Piece implements Cloneable,java.io.Serializable{

    protected Position _pos;
    protected boolean _color;

    public Piece(){};

    public Piece(Position position,boolean color){
        _pos=position;
        _color=color;
    };
    
    public abstract boolean specificRules(Position pos, Grid grid);

    //permet de savoir si il y a une pièce sur le chemin de celle-ci
    
    public boolean checkPath(Position begin, Position end, Grid grid){
        Position currentPos= new Position(begin);
        int stepColumn, stepLine;
        Piece piece;
        
        //First we check if it's a linear displacement (same line or same column or diagonal)
        if(!(begin.getLine() == end.getLine() || begin.getColumnNumber()==end.getColumnNumber() || 
                Math.abs(begin.getLine()-end.getLine())==Math.abs(begin.getColumnNumber()-end.getColumnNumber())))
            return false;
        
        //column
        if(begin.getColumnNumber()-end.getColumnNumber()>0)
            stepColumn=-1;
        else if(begin.getColumnNumber()-end.getColumnNumber()<0)
            stepColumn=1;
        else
            stepColumn=0;
    
         //line
        if(begin.getLine()-end.getLine()>0)
            stepLine=-1;
        else if(begin.getLine()-end.getLine()<0)
            stepLine=1;
        else
            stepLine=0;
        
        //mise à jour de la première position après calcule des pas
          try{
            currentPos.setColumn(currentPos.getColumnNumber()+stepColumn);
            currentPos.setLine(currentPos.getLine()+stepLine);
        }
        catch(Exception e){System.out.println(e.getMessage());}//car la position est protégée
       
        while(!currentPos.equals(end)){
            piece=grid.getAt(currentPos);
            if(piece==null){//si pas de pièce
                try{
                    currentPos.setColumn(currentPos.getColumnNumber()+stepColumn);
                    currentPos.setLine(currentPos.getLine()+stepLine);
                }
                catch(Exception e){System.out.println(e.getMessage());}

            }
            else return false;      //si piece

        }
        return true;
    }


    public boolean getColor(){return _color;}

    
    public void setPosition(Position newPos){
        _pos = newPos;
    };

    /**
     * Permet l'accès à la position de la pièce
     * @return la position de la pièce
     */
    public Position getPosition(){
        return this._pos;
    }
    
    @Override
    public Piece clone() throws CloneNotSupportedException{
        return (Piece)super.clone();
    }
    
}

