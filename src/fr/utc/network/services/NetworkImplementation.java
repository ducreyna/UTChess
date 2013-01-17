/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.services;

import fr.utc.data.Profile;
import fr.utc.data.historique.Message;
import fr.utc.data.historique.Move;
import fr.utc.grid.GridInterface;
import fr.utc.lobby.IHMConnexion2Reseau;
import fr.utc.network.exceptions.ErrorAskingLoadGameException;
import fr.utc.network.exceptions.ErrorAskingNewGameException;
import fr.utc.network.exceptions.ErrorCreatingThreadException;
import fr.utc.network.exceptions.ErrorSendingAbandonException;
import fr.utc.network.exceptions.ErrorSendingDisconnectionFromGameException;
import fr.utc.network.exceptions.ErrorSendingDrawException;
import fr.utc.network.exceptions.ErrorSendingDrawResponseException;
import fr.utc.network.exceptions.ErrorSendingGameResponseException;
import fr.utc.network.exceptions.ErrorSendingInGameStateException;
import fr.utc.network.exceptions.ErrorSendingMessageException;
import fr.utc.network.exceptions.ErrorSendingMoveException;
import fr.utc.network.exceptions.ErrorSendingProfileException;
import fr.utc.network.exceptions.ErrorSendingSaveRequestException;
import fr.utc.network.exceptions.ErrorSendingSaveResponseException;
import fr.utc.network.exceptions.NetworkException;
import fr.utc.network.message.MessageNetwork;
import fr.utc.network.message.MsgAbandon;
import fr.utc.network.message.MsgChatMessage;
import fr.utc.network.message.MsgDisconnectionFromGame;
import fr.utc.network.message.MsgInGame;
import fr.utc.network.message.MsgLogout;
import fr.utc.network.message.MsgMove;
import fr.utc.network.message.MsgProfileUpdate;
import fr.utc.network.message.MsgRequestDraw;
import fr.utc.network.message.MsgRequestLoadGame;
import fr.utc.network.message.MsgRequestNewGame;
import fr.utc.network.message.MsgRequestProfile;
import fr.utc.network.message.MsgRequestSaveGame;
import fr.utc.network.message.MsgResponseDraw;
import fr.utc.network.message.MsgResponseNewGame;
import fr.utc.network.message.MsgResponseSaveGame;
import fr.utc.network.processing.*;
import fr.utc.network.profile.IpProfile;
import fr.utc.network.profile.ManagerListPlayer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Les méthodes que cette classe contient correspond aux services que la partie « Reseau »
 * rend aux autres parties du projet (IHM Grille, IHM Connexion ainsi que Data).
 * @author Groupe Réseau
 */
public class NetworkImplementation implements NetworkInterface {
    private static ThreadListenMulticast _threadListenMulticast; /** @quality Je pense qu'il y en a besoin */
    private static ThreadListenUnicast _threadListenUnicast;
    private static ArrayList<ThreadListenUnicast> _threadListenUnicastList;
    private static ArrayList<ThreadSendMulticast> _threadSendMulticastList;
    private static ArrayList<ThreadSendUnicast> _threadSendUnicastList;

    /**
     * Interfaces with the UI
     */
    private static IHMConnexion2Reseau _IHMConnexionInterface;
    private static GridInterface _IHMGrilleInterface;

    /**
     * our own ip profile
     */
    private static IpProfile _myIpProfile = null;
    private static IpProfile _distantPlayerProfile;
    
    /**
     * Local list of player, empty at the beginning
     */
    private static ManagerListPlayer _listOfProfiles;
   
    /**
     * Constructor
     * this constructeur will be called only one time by the lobby interface.
     */
    public NetworkImplementation(Profile ourProfile)
    {
	_listOfProfiles = new ManagerListPlayer();

        try
        {
            _myIpProfile = new IpProfile(ourProfile, null, false);
            _myIpProfile.setIp(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException ex)
        {
            Logger.getLogger(NetworkImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     /**
     * @return the current user IpProfile
     */
    public static IpProfile getMyIpProfile()
    {
        return _myIpProfile;
    }

    /**
     * setter
     * @param p
     */
    public static void setDistantPlayerProfile(IpProfile p)
    {
        _distantPlayerProfile = p;
    }
    
    /**
     * @return the multicast listening thread instance
     */
    public static ThreadListenMulticast getThreadListenMulticast()
    {
        return _threadListenMulticast;
    }

    /**
     * @return the multicast listening threads list
     */
    public static ArrayList<ThreadListenUnicast> getThreadListenUnicastList()
    {
        return _threadListenUnicastList;
    }

    /**
     * @return the multicast sending threads list
     */
    public static ArrayList<ThreadSendMulticast> getThreadSendMulticastList()
    {
        return _threadSendMulticastList;
    }

    /**
     * @return the unicast listening thread instance
     */
    public static ArrayList<ThreadSendUnicast> getThreadSendUnicastList()
    {
        return _threadSendUnicastList;
    }

    /**
     * @return a list of the connected players' profiles
     */
    public static ManagerListPlayer getListOfProfiles()
    {
        return _listOfProfiles;
    }

    /**
     * @return the IHM Connection implemented interface instance
     */
    public static IHMConnexion2Reseau getIHMConnexionInterface()
    {
        return _IHMConnexionInterface;
    }

    /**
     * @return the IHM Grille implemented interface instance
     */
    public static GridInterface getIHMGrilleInterface()
    {
        return _IHMGrilleInterface;
    }

    /*
     * Sets the IHMConnexionInterface value
     */
    public void setIHMConnexionInterface(IHMConnexion2Reseau IHMConnexionInterface)
    {
        NetworkImplementation._IHMConnexionInterface=IHMConnexionInterface;
    }

    /*
     * Sets the IHMGrilleInterface value
     */
    public void setIHMGrilleInterface(GridInterface IHMGrilleInterface)
    {
        NetworkImplementation._IHMGrilleInterface=IHMGrilleInterface;
    }

    
    /**
     * Initializes the multicast and the unicast listening
     */
    public void startThreadListenNetwork() 
    {
        if(_threadListenMulticast==null)
        {
            _threadListenMulticast = new ThreadListenMulticast();
            new Thread(_threadListenMulticast).start();

            try
            {
                System.out.println(InetAddress.getLocalHost().getHostAddress());
                MsgRequestProfile leMsg = new MsgRequestProfile(_myIpProfile);
                ThreadSendMulticast test = new ThreadSendMulticast(leMsg);
                new Thread(test).start();

                _threadListenUnicast = new ThreadListenUnicast();
                new Thread(_threadListenUnicast).start();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Request a connected player to load a game
     * @param id_opponent : The player concerned by the request
     * @param id_game : The game id to be loaded
     */
    public void askLoadGame(String id_opponent, UUID id_game) throws ErrorAskingLoadGameException {
        
        MsgRequestLoadGame message = new MsgRequestLoadGame(_myIpProfile.getProfile().getId(), id_game);
        
        IpProfile opponentProfile;
        try
        {
            opponentProfile = getIpProfile(id_opponent);
            ThreadSendUnicast sendUnicast = new ThreadSendUnicast(opponentProfile,message);
            new Thread(sendUnicast).start();
        } catch (NetworkException ex)
        {
            Logger.getLogger(NetworkImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorAskingLoadGameException();
        } 
    }

    /**
     * Ask a player to start a new game.
     * @param id_opponent : The opponent ID
     * @param color : The opponent's pawns color
     * @param timer : Value of the timer during the game
     */
    public void askNewGame(String id_opponent, boolean color, int timer, UUID id_game) throws ErrorAskingNewGameException {
        MsgRequestNewGame message = new MsgRequestNewGame(_myIpProfile.getProfile().getId(),color,timer, id_game);
        ThreadSendUnicast sendUnicast;
        IpProfile opponentProfile;
        
        try
        {
            opponentProfile = getIpProfile(id_opponent);
            sendUnicast = new ThreadSendUnicast(opponentProfile,message);
            new Thread(sendUnicast).start();

        } catch (NetworkException ex)
        {
            Logger.getLogger(NetworkImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorAskingNewGameException();
        }
    }

    /**
     * Allows to send a game response
     * @param id_opponent : The receiver ID of the game response
     * @param response : The response the communicate
     */
    public void sendGameResponse(String id_opponent, boolean response) throws ErrorSendingGameResponseException {
        
        MsgResponseNewGame msgResponse = new MsgResponseNewGame(id_opponent, response);
        ThreadSendUnicast sendUnicast;
        
        try
        {
            sendUnicast = new ThreadSendUnicast(getIpProfile(id_opponent),msgResponse);
            new Thread(sendUnicast).start();
            
        } catch (ErrorCreatingThreadException ex)
        {
            Logger.getLogger(NetworkImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorSendingGameResponseException();
        } 
        catch (NetworkException ex)
        {
            Logger.getLogger(NetworkImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorSendingGameResponseException();
        }
    }
    
    /**
     * Retrieves the IpProfile belonging to the input id_opponent
     * @param id_opponent : The opponent's ID
     * @return IpProfile matching with the id_opponent
     * @throws NetworkException : Rised whether there is no IpProfile matching the id_opponent
     */
    public static IpProfile getIpProfile(String id_opponent) throws NetworkException
    {
        for(IpProfile ipProfile : getListOfUsers().getPlayerList())
        {
            if(ipProfile.getProfile().getId().equals(id_opponent))
            {
                return ipProfile;
            }
        }
        throw new NetworkException();
    }

    /**
     * @return List of ipProfiles
     */
    public static ManagerListPlayer getListOfUsers() {
        return _listOfProfiles;
    }
    
    /**
     * Send a disconnection request from a current game.
     */
    public void sendDisconnectionFromGame() throws ErrorSendingDisconnectionFromGameException
    {
        //Sends a message to the opponent to end the game
        MsgDisconnectionFromGame message = new MsgDisconnectionFromGame();
        ThreadSendUnicast sendUnicast;
        
        try
        {
            sendUnicast = new ThreadSendUnicast(_distantPlayerProfile,message);
            new Thread(sendUnicast).start();

        } catch (ErrorCreatingThreadException ex)
        {
            Logger.getLogger(NetworkImplementation.class.getName()).log(Level.SEVERE, null, ex);
             throw new ErrorSendingDisconnectionFromGameException();
        }

        //Sends a message to all players to update the IsInGame state
        _myIpProfile.getProfile().setInGame(false);
        MsgInGame msg = new MsgInGame(_myIpProfile.getIp(), _myIpProfile.getProfile().isInGame());
        try
        {
            sendUnicastAll(msg);
        } catch (ErrorCreatingThreadException ex)
        {
            Logger.getLogger(NetworkImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorSendingDisconnectionFromGameException();
        }
    }

    /**
     * Allows to send a message to the opponent
     * @param msg : the message content to send.
     */
    public void sendMessage(Message msg) throws ErrorSendingMessageException
    {
        MsgChatMessage message = new MsgChatMessage(msg);
        ThreadSendUnicast sendUnicast;
        try
        {
            sendUnicast = new ThreadSendUnicast(_distantPlayerProfile,message);
            new Thread(sendUnicast).start();

        } catch (ErrorCreatingThreadException ex)
        {
            Logger.getLogger(NetworkImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorSendingMessageException();
        }
    }

    /*
     *  Allows to inform the other players about the logout.
     */
    public static void sendLogout()
    {
        if(_myIpProfile!=null)
        {
             MsgLogout message = new MsgLogout(_myIpProfile);
            try
            {
                sendUnicastAll(message);
            } catch (ErrorCreatingThreadException ex)
            {
                Logger.getLogger(NetworkImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
            //TODO : stop listening threads
        }
    }

    /**
     * @param message 
     */
    private static void sendUnicastAll(MessageNetwork message) throws ErrorCreatingThreadException
    {
        for(IpProfile ipProfile : getListOfUsers().getPlayerList())
        {    
            ThreadSendUnicast sendUnicast;
            
                System.out.println(""+ipProfile.getIp());
                sendUnicast = new ThreadSendUnicast(ipProfile,message);
                new Thread(sendUnicast).start();
                
           
        }
    }

    /**
     * Notify the opponent about our move during a game 
     * @param move : Motion undergoing
     */
    public void sendMove(Move move) throws ErrorSendingMoveException
    {
        MsgMove message = new MsgMove(move);
        ThreadSendUnicast sendUnicast;
        try
        {
            sendUnicast = new ThreadSendUnicast(_distantPlayerProfile,message);
            new Thread(sendUnicast).start();

        } catch (ErrorCreatingThreadException ex)
        {
            Logger.getLogger(NetworkImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorSendingMoveException();
        }
    }

    /*
     * Notify the opponent about the decision to abandon the current game.
     */
    public void sendAbandon() throws ErrorSendingAbandonException {
        try
        {
            ThreadSendUnicast sendUnicast = new ThreadSendUnicast(_distantPlayerProfile, new MsgAbandon());
            new Thread(sendUnicast).start();

        } catch (ErrorCreatingThreadException ex)
        {
            Logger.getLogger(NetworkImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorSendingAbandonException();
        }
    }

    /**
     * Notify the opponent to save the current game.
     */
    public void sendSaveRequest() throws ErrorSendingSaveRequestException {
        MsgRequestSaveGame message = new MsgRequestSaveGame();
        ThreadSendUnicast sendUnicast;
        try
        {
            sendUnicast = new ThreadSendUnicast(_distantPlayerProfile,message);
            new Thread(sendUnicast).start(); 
        } catch (ErrorCreatingThreadException ex)
        {
            Logger.getLogger(NetworkImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorSendingSaveRequestException();
        }     
    }

    /**
     * Send a response to saving request.
     * @param response
     * @throws ErrorSendingSaveResponseException 
     */
    public void sendSaveResponse(boolean response) throws ErrorSendingSaveResponseException
    {
        MsgResponseSaveGame message = new MsgResponseSaveGame(response);
        ThreadSendUnicast sendUnicast;
        try
        {
            sendUnicast = new ThreadSendUnicast(_distantPlayerProfile,message);
            new Thread(sendUnicast).start();
        } catch (ErrorCreatingThreadException ex)
        {
            Logger.getLogger(NetworkImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorSendingSaveResponseException();
        }
    }

    /**
     * Notify that the player wants to draw during a game
     */
    public void sendDraw() throws ErrorSendingDrawException
    {
        MsgRequestDraw message = new MsgRequestDraw();
        ThreadSendUnicast sendUnicast;
        try
        {
            sendUnicast = new ThreadSendUnicast(_distantPlayerProfile,message);
            new Thread(sendUnicast).start();

        } catch (ErrorCreatingThreadException ex)
        {
            Logger.getLogger(NetworkImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorSendingDrawException();
        }
    }
    
    /**
     * Draw request response
     * @param response : response value, True to accept the draw and false otherwise
     * @throws ErrorSendingDrawResponseException 
     */
    public void sendDrawResponse(boolean response) throws ErrorSendingDrawResponseException
    {
        MsgResponseDraw message = new MsgResponseDraw(response);
        ThreadSendUnicast sendUnicast;
        try
        {
            sendUnicast = new ThreadSendUnicast(_distantPlayerProfile,message);
            new Thread(sendUnicast).start();

        } catch (ErrorCreatingThreadException ex)
        {
            Logger.getLogger(NetworkImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Modifies the local profile and send it to other players
     */
    public void sendProfileUpdate(Profile ourProfile) throws ErrorSendingProfileException {
        try
        {
            _myIpProfile = new IpProfile(ourProfile, null, false);
            _myIpProfile.setIp(InetAddress.getLocalHost().getHostAddress());
            MsgProfileUpdate msg = new MsgProfileUpdate(_myIpProfile);
            try
            {
                sendUnicastAll(msg);
            } catch (ErrorCreatingThreadException ex)
            {
                Logger.getLogger(NetworkImplementation.class.getName()).log(Level.SEVERE, null, ex);
                throw new ErrorSendingProfileException();
            }
        } catch (UnknownHostException ex)
        {
            Logger.getLogger(NetworkImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Allows to notify all the connected players about the disconnexion
     */
    public void sendGameState(Boolean state) throws ErrorSendingInGameStateException
    {
        _myIpProfile.getProfile().setInGame(state);
        MsgInGame msg = new MsgInGame(_myIpProfile.getIp(), _myIpProfile.getProfile().isInGame());
        try
        {
            sendUnicastAll(msg);
        } catch (ErrorCreatingThreadException ex)
        {
            Logger.getLogger(NetworkImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorSendingInGameStateException();
        }
    }

}
