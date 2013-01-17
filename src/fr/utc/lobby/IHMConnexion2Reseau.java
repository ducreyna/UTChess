/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lobby;

import java.util.UUID;
import fr.utc.network.services.NetworkImplementation;

/**
 * communication interface : IHMConnexion(Lobby) with Reseau(Network)
 *
 * @author lo23a014
 */
public interface IHMConnexion2Reseau
{
    /**
     * Inform that a new game want to be creat with you
     *
     * @param id, opponent id
     * @param color, color selected
     * @param timer, timer for each player
     * @param IDGame, game id
     */
    void newGameEvent(String id, boolean color, int timer, UUID IDGame);

    /**
     * Inform the response to the last game request
     *
     * @param idOpponent, opponent id
     * @param response, responce for the question ask to the other player
     */
    void receiveResponse(String idOpponent, boolean response);

    /**
     * Inform that a game have to be load
     *
     * @param idOpponent, opponent id
     * @param IDGame, game id
     */
    void loadGameEvent(String idOpponent, UUID idGame);

    /**
     * informed that there was an error with a send message
     *
     * @param description, error description
     */
    void receiveErrorSendingMessage(String description);
}
