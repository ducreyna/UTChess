/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.grid;

import fr.utc.data.Color;
import fr.utc.data.Position;
import fr.utc.data.historique.Message;
import fr.utc.data.historique.Move;
import fr.utc.data.piece.Piece;
import java.util.ArrayList;

/**
 * Interface allowing to access to the public functions of IHM Grid
 */
public interface GridInterface
{   
    public void receiveDisconnectionOpponent();
    
    public void receiveMessage(Message msg);
    
    public void receiveMove(Move deplacement);
    
    public void receiveAbandon();
    
    public void receiveDraw();

    public void receiveDrawResponse(boolean response);

    public void receiveSaveRequest();

    public void receiveSaveResponse(boolean response);

    public void receiveTimeout();

    public void transformPawn(Position oldPos, Position newPos, boolean color);

    public void receiveError(String msg);

}
