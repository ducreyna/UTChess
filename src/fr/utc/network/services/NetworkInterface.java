/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.services;

//import fr.utc.data.historique.Message;
import fr.utc.data.Profile;
import fr.utc.data.historique.Message;
import fr.utc.data.historique.Move;
import fr.utc.network.exceptions.*;
import fr.utc.network.profile.ManagerListPlayer;
import java.util.UUID;


/**
 *
 * @author Groupe Réseau
 */
public interface NetworkInterface {
    public void startThreadListenNetwork() throws ErrorCreatingThreadException;
    
    /**
     * Request a connected player to load a game
     * @param id_opponent : The player concerned by the request
     * @param id_game : The game id to be loaded
     * @throws ErrorAskingLoadGameException
     */
    public void askLoadGame(String id_opponent, UUID id_game) throws ErrorAskingLoadGameException;

    /**
     * Send a game to load for the opponent
     */
    //public void sendGame(Game game)
    
    /**
     * Ask a player to start a new game.
     * @param id_opponent : The opponent ID
     * @param color : The opponent's pawns color
     * @param timer : Value of the timer during the game
     * @throws ErrorAskingNewGameException 
     */
    public void askNewGame(String id_opponent, boolean color, int timer, UUID id_game) throws ErrorAskingNewGameException;
    
    /**
     * Allows to send a game response
     * @param id_opponent : The receiver ID of the game response
     * @param response : The response the communicate
     * @throws ErrorSendingGameResponseException 
     */
    public void sendGameResponse(String id_opponent, boolean response) throws ErrorSendingGameResponseException;
   
    /**
     * @return the connected players list
     */
    //public ManagerListPlayer getListOfUsers();
    
    /**
     * Send a disconnection request from a current game.
     * @throws ErrorSendingDisconnectionFromGameException 
     */
    public void sendDisconnectionFromGame() throws ErrorSendingDisconnectionFromGameException;

    /**
     * Allows to notify all the connected players about our Game state.
     * @throws ErrorSendingInGameStateException 
     */
    public void sendGameState(Boolean state) throws ErrorSendingInGameStateException;

    /**
     * Allows to inform the other players about the logout.
     * @throws ErrorSendingLogOutException 
     */
    //public void sendLogout() throws ErrorSendingLogOutException;
    
    /**
     * @quality Deplacement = Move
     * Notify the opponent about our move during a game 
     * @param move : Motion undergoing
     * @throws ErrorSendingMoveException 
     */
    public void sendMove(Move move) throws ErrorSendingMoveException;

    /**
     *
     * @param msg message to send to the opponent
     * @throws ErrorSendingMessageException
     */
    public void sendMessage(Message msg) throws ErrorSendingMessageException;
    
    /**
     * Notify the opponent about the decision to abandon the current game.
     * @throws ErrorSendingAbandonException 
     */
    public void sendAbandon() throws ErrorSendingAbandonException;
    
    /**
     * Notify the opponent to save the current game.
     * @throws ErrorSendingSaveRequestException 
     */
    public void sendSaveRequest() throws ErrorSendingSaveRequestException;

    /**
     * * Send a response to saving request.
     * @param response
     * @throws ErrorSendingSaveResponseException 
     */
    public void sendSaveResponse(boolean response) throws ErrorSendingSaveResponseException;
    
   /**
     * Notify that the player wants to draw during a game
     * @throws ErrorSendingDrawException 
     */
    public void sendDraw() throws ErrorSendingDrawException;
    
    /**
     * Draw request response
     * @param response : response value, True to accept the draw and false otherwise
     * @throws ErrorSendingDrawResponseException 
     */
    public void sendDrawResponse(boolean response) throws ErrorSendingDrawResponseException;
    
    /**
     * Allows to notify the other players when the profile was modified
     * @throws ErrorSendingProfileException 
     */
    public void sendProfileUpdate(Profile ourProfile) throws ErrorSendingProfileException;
}
