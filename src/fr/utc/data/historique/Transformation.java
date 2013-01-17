/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.data.historique;
import fr.utc.data.Player;
import fr.utc.data.Position;
import fr.utc.data.historique.Move;
import java.util.Date;

/**
 *
 * @author jocelyn
 */
public class Transformation extends Move
{
    String _newPiece;
    /** 
     *
     * @param time
     * @param emitter
     * @param oldPosition
     * @param newPosition
     */
    public Transformation(Date time,Player emitter,Position oldPosition,Position newPosition, String newPiece){
        super(time,emitter,oldPosition,newPosition);
        _newPiece = newPiece;
    }
    
    public Transformation(Move m){
        super(m);
    }
    
    public void setNewPieceName(String newPieceName){
        _newPiece = newPieceName;
    }
    
    public String getNewPieceName(){
        return _newPiece;
    }
}
